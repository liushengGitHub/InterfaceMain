package liusheng.main.app.bilibili.processor;

import liusheng.main.annotation.ThreadSafe;
import liusheng.main.app.bilibili.donwload.DefaultDownloaderController;
import liusheng.main.app.bilibili.donwload.RetryDownloader;
import liusheng.main.app.bilibili.entity.DownloadEntity;
import liusheng.main.app.bilibili.entity.OldVideoBean;
import liusheng.main.app.bilibili.executor.MergeFile;
import liusheng.main.process.AbstractLinkedListableProcessor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;

@ThreadSafe
public class HandleOldVideoBean extends AbstractLinkedListableProcessor<OldVideoBean, HandleOldVideoBean> {
    private final Logger logger = Logger.getLogger(HandleOldVideoBean.class);

    private final int retry ;
    private final List<? extends RetryDownloader.DownloaderListener> downloaderListeners;
    private final Semaphore semaphore;
    private final RetryDownloader.DownloaderController controller;

    public HandleOldVideoBean(List<? extends RetryDownloader.DownloaderListener> downloaderListeners, int retry, Semaphore semaphore, RetryDownloader.DownloaderController controller) {
        this.downloaderListeners = downloaderListeners;

        this.retry = retry;
        //this.addListener(new DefaultProcessListener(retry));
        this.semaphore = semaphore;
        this.controller = controller;
    }

    @Override
    protected void doProcess(OldVideoBean oldVideoBean, List<Object> returnData) throws Throwable {
        Optional.ofNullable(oldVideoBean.getData())
                .map(OldVideoBean.DataBean::getDurl)
                .ifPresent(durlBeans -> {
                    List<String> paths = new LinkedList<>();
                    String name = oldVideoBean.getName();
                    Path dirPath = oldVideoBean.getDirFile().toPath();
                    try {
                        if (durlBeans.isEmpty()) return;
                        boolean b = durlBeans.size() > 1;
                        String refererUrl = oldVideoBean.getUrl();
                        // 下载这个视频的所有分段
                        durlBeans.forEach(durlBean -> {

                            try {
                                int order = durlBean.getOrder();
                                String videoUrl = durlBean.getUrl();

                                List<String> backup_url = durlBean.getBackup_url() == null ? Collections.emptyList() : (List<String>) durlBean.getBackup_url();

                                Path filePath = dirPath.resolve((b ? order + "_" : "") + name + ".flv");

                                paths.add(filePath.toString());

                                RetryDownloader retryDownloader = new RetryDownloader(controller, new DefaultDownloaderController());
                                retryDownloader.getDownloaderListeners().addAll(downloaderListeners);
                                retryDownloader.process(new DownloadEntity(refererUrl, videoUrl, backup_url, filePath, dirPath, retry));

                            } catch (Throwable e) {
                                throw new RuntimeException(e);
                            }

                        });
                        logger.info(refererUrl + "  Download Completed ");
                        if (b) {
                            try {
                                //限流,防止OOM
                                semaphore.acquire();
                                new MergeFile(paths, name, dirPath.toString(), semaphore).run();
                            } finally {
                                semaphore.release();
                            }
                        }

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        // 删除所有临时文件
                        int[] fails = new int[1];
                        try {
                            Path pathTxt = dirPath.resolve(name + ".txt");
                            if (Files.exists(pathTxt)) {
                                Files.delete(pathTxt);
                            }
                            paths.forEach(pathStr -> {
                                Path path = Paths.get(pathStr);
                                if (Files.exists(path)) {
                                    try {
                                        Files.delete(path);
                                    } catch (IOException e) {
                                        fails[0]++;
                                    }
                                }
                            });
                        } catch (Exception e) {
                            fails[0]++;
                        }
                        if (fails[0] > 0) {
                            logger.debug("删除文件失败");
                        }
                    }
                });
    }

}
