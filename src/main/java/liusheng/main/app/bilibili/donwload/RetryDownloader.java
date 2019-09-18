package liusheng.main.app.bilibili.donwload;

import liusheng.main.app.bilibili.entity.DownloadEntity;
import liusheng.main.app.bilibili.util.ConnectionUtils;
import liusheng.main.process.AbstractLinkedListableProcessor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RetryDownloader extends AbstractLinkedListableProcessor<DownloadEntity, RetryDownloader> {


    private final Logger logger = Logger.getLogger(RetryDownloader.class);

    private final List<DownloaderListener> downloaderListeners = new LinkedList<>();
    private final DownloaderController globalController;
    private final DefaultDownloader singletonDownloader;

    public RetryDownloader(DownloaderController globalController, DefaultDownloader singletonDownloader) {
        this.globalController = globalController;
        this.singletonDownloader = singletonDownloader;
    }

    public List<DownloaderListener> getDownloaderListeners() {
        return downloaderListeners;
    }


    public DownloaderController getGlobalController() {
        return globalController;
    }

    public DefaultDownloader getSingletonDownloader() {
        return singletonDownloader;
    }

    public interface DownloaderListener {

        void listen(int length);

    }

    public interface DownloaderController {
        boolean isCancel();

        boolean isPause();

        void cancel();

        void pauseOrStart();
    }

    @Override
    protected void doProcess(DownloadEntity input, List<Object> returnData) throws Throwable {
        int retry = input.getRetry();
        List<String> urls = input.getbUrls();
        String url = input.getUrl();
        Path filePath = input.getFilePath();
        String refererUrl = input.getRefererUrl();

        try {
            // 下载
            retryDownload(retry, url, filePath, refererUrl);
        } catch (IOException e) {
            // 如歌上述失败,则选择备用urls
            Exception t = null;

            if (urls == null || urls.isEmpty()) throw e;
            for (String u :
                    urls) {
                try {
                    retryDownload(3, u, filePath, refererUrl);
                    // 如果下载成功,就没有异常
                    t = null;
                } catch (Exception e1) {
                    t = e1;
                }
            }
            if (t != null) {
                t.addSuppressed(e);
                throw t;
            }
        }
    }


    private void retryDownload(int retry, String url, Path filePath, String refererUrl) throws IOException {
        for (int i = 0; i < retry; i++) {
            try {
                downloadFile(refererUrl, url, filePath);

                break;
            } catch (Exception e) {
                if (i == retry - 1) throw e;
            }
        }
    }

    public void downloadFile(String url, String videoUrl, Path path) throws IOException {
        HttpURLConnection connection = ConnectionUtils.getNativeConnection(videoUrl);

        connection.setRequestMethod("GET");
        connection.addRequestProperty("Referer", url);

        logger.debug(videoUrl + "  Download Started ");
        long len = connection.getContentLengthLong();
        if (len <= 0) throw new IllegalStateException();
        InputStream inputStream = connection.getInputStream();
        try (OutputStream outputStream = Files.newOutputStream(path)) {

            byte[] bytes = new byte[10240];
            long sum = 0;
            while (sum < len) {

                if (singletonDownloader.isCancel() || globalController.isCancel()  ) {
                    // 取消的话删除
                    outputStream.close();
                    Files.delete(path);
                    break;
                }
                if (singletonDownloader.isPause() || globalController.isPause()  ) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        outputStream.close();
                        Files.delete(path);
                    }
                    continue;
                }
                int length = inputStream.read(bytes);
                if (length == -1 || length == 0) {

                    try {
                        // 关闭失败也没有关系
                        inputStream.close();
                        connection.disconnect();
                    } catch (Exception e) {

                    }
                    connection = ConnectionUtils.getNativeConnection(videoUrl);

                    connection.setRequestMethod("GET");
                    connection.addRequestProperty("Referer", url);
                    connection.addRequestProperty("Range", "bytes=" + sum + "-" + (len - 1));

                    inputStream = connection.getInputStream();

                    continue;
                }

                outputStream.write(bytes, 0, length);
                sum += length;

                downloaderListeners.forEach(l -> l.listen(length));
            }

        } catch (IOException e) {
            logger.debug(videoUrl + "  Download Error ");
            throw e;
        } finally {
            inputStream.close();
        }
    }
}
