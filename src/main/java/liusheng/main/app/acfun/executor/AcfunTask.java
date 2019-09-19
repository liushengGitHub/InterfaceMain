package liusheng.main.app.acfun.executor;

import liusheng.main.app.acfun.entity.DataBean;
import liusheng.main.app.acfun.entity.M3u8Bean;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class AcfunTask implements Runnable {

    private final Logger logger = Logger.getLogger(AcfunTask.class);
    private final M3u8Bean m3u8Bean;
    private final Path filePath;
    private final static String FIRST = "#EXTINF:";

    public AcfunTask(M3u8Bean m3u8Bean, Path filePath) {

        this.m3u8Bean = m3u8Bean;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        OutputStream outputStream = null;
        Path tsFilePath = filePath.resolve(UUID.randomUUID().toString() + ".ts");
        try {
            DataBean dataBean = m3u8Bean.getDataBean();

            String m3u8 = Optional.of(dataBean)
                    .map(DataBean::getAdaptationSet)
                    .map(DataBean.AdaptationSetBean::getRepresentation)
                    .map(list -> {
                        return list.stream().map(DataBean.AdaptationSetBean.RepresentationBean::getM3u8)
                                .findFirst().orElseThrow(RuntimeException::new);
                    }).orElseThrow(RuntimeException::new);

            outputStream = Files.newOutputStream(tsFilePath);
            // 每行扫秒
            Scanner scanner = new Scanner(m3u8);
            // 下载文件
            readAndWrite("", scanner, outputStream);
        } catch (Exception e) {
            if (Objects.nonNull(outputStream)) {
                try {
                    outputStream.close();
                } catch (IOException e1) {
                } finally {
                    outputStream = null;
                }

            }
            if (Objects.nonNull(tsFilePath) && Files.exists(tsFilePath)) {
                try {
                    Files.delete(tsFilePath);
                } catch (IOException ex) {
                    logger.info("下载失败");
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (Objects.nonNull(outputStream))
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }


    // 解析M3u8文件并下载文件
    private void readAndWrite(String pre, Scanner scanner, OutputStream outputStream) throws IOException {
        // bug就是其中一个下载失败就会导致文件下载失败
        int i = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.startsWith(FIRST)) {

                String newLine = scanner.nextLine();
                String videoUrl = pre + newLine;

                // 下载文件重试 3次
                download(outputStream, videoUrl, 3, null);
            }

        }
    }

    private void download(OutputStream outputStream, String videoUrl, int n, Exception exception) throws IOException {
        if (n == 0) throw new RuntimeException(exception);
        try {
            doDownload(outputStream, videoUrl);
        } catch (Exception e) {
            download(outputStream, videoUrl, n - 1, e);
        }
    }

    private void doDownload(OutputStream outputStream, String videoUrl) throws IOException {
        InputStream inputStream = new URL(videoUrl)
                .openStream();

        byte[] bytes = new byte[102400];

        int length = -1;

        while ((length = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, length);
        }
        try {
            outputStream.flush();
        } catch (Exception e) {

        }
        try {
            inputStream.close();
        } catch (Exception e) {

        }
    }
}
