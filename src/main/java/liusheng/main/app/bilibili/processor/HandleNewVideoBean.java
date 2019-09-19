package liusheng.main.app.bilibili.processor;

import liusheng.main.annotation.ThreadSafe;
import liusheng.main.app.bilibili.donwload.DefaultDownloaderController;
import liusheng.main.app.bilibili.donwload.RetryDownloader;
import liusheng.main.app.bilibili.entity.DashBean;
import liusheng.main.app.bilibili.entity.DownloadEntity;
import liusheng.main.app.bilibili.entity.NewVideoBean;
import liusheng.main.app.bilibili.executor.MergeAudioAndVideoFile;
import liusheng.main.process.AbstractLinkedListableProcessor;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

@ThreadSafe
public class HandleNewVideoBean extends AbstractLinkedListableProcessor<NewVideoBean, HandleNewVideoBean> {

    private final List<? extends RetryDownloader.DownloaderListener> downloaderListeners;
    private final ExecutorService stepExecutorService;
    private final int retry ;
    private final Semaphore semaphore;
    private final RetryDownloader.DownloaderController controller;
    private final Logger logger = Logger.getLogger(HandleNewVideoBean.class);

    public HandleNewVideoBean(List<? extends RetryDownloader.DownloaderListener> downloaderListeners, ExecutorService stepExecutorService, int retry, Semaphore semaphore, RetryDownloader.DownloaderController controller) {
        this.downloaderListeners = downloaderListeners;

        this.stepExecutorService = stepExecutorService;
        this.retry = retry;
        //this.addListener(new DefaultProcessListener(retry));
        this.semaphore = semaphore;
        this.controller = controller;
    }

    /*static class DefaultProcessListener implements ProcessListener<NewVideoBean>{
        private int retry;
         private Throwable t ;
        public DefaultProcessListener(int retry) {

            this.retry = retry;
        }

        private  int n = 0;
        @Override
        public void errorDo(Object source, NewVideoBean target, Throwable throwable) {
             if (n < retry ) {
                 HandleNewVideoBean newVideoBean = (HandleNewVideoBean) source;
                 try {
                     newVideoBean.process(target);
                 } catch (Throwable e) {
                     t = e;
                 }
             }else {
                 throw  new RuntimeException(t);
             }
        }
    }*/
    static class SatetCountDownLatch extends CountDownLatch {
        volatile boolean error = false;

        public SatetCountDownLatch(int count) {
            super(count);
        }
    }

    @Override
    protected void doProcess(NewVideoBean newVideoBean, List<Object> returnData) throws Throwable {
        Optional.ofNullable(newVideoBean).map(NewVideoBean::getData)
                .map(NewVideoBean.DataBean::getDash)
                .ifPresent(dashBean -> {
                    Path flvPath = null;
                    Path mp3Path = null;
                    DefaultDownloaderController singletonDownloader1 = new DefaultDownloaderController();
                    DefaultDownloaderController singletonDownloader2 = new DefaultDownloaderController();
                    try {
                        List<DashBean.AudioBean> audioBeanList = dashBean.getAudio();

                        List<DashBean.VideoBean> videoBeanList = dashBean.getVideo();

                        if (audioBeanList == null || audioBeanList.size() == 0) {
                            return;
                        }

                        if (videoBeanList == null || videoBeanList.size() == 0) {
                            return;
                        }

                        String fileName = newVideoBean.getName();
                        String refererUrl = newVideoBean.getUrl();
                        File dirFile = newVideoBean.getDirFile();
                        SatetCountDownLatch state = new SatetCountDownLatch(1);

                        String quality = System.getProperty("quality");

                        DashBean.VideoBean videoBean = null;
                        if (quality == null) {
                            videoBean = videoBeanList.stream().max(Comparator.comparing(DashBean.VideoBean::getId)).get();
                        } else {
                            videoBeanList.stream().filter(video -> video.getId() == Integer.getInteger(quality)).findFirst();
                        }


                        List<String> vbUrls = videoBean.getBackupUrl();
                        String vbUrl = videoBean.getBaseUrl();

                        Path dirPath = dirFile.toPath();
                        flvPath = dirPath.resolve(fileName + ".flv.temp");
                        mp3Path = dirPath.resolve(fileName + ".mp3.temp");
                        // 下载视频文件
                        Path flvPathTemp = flvPath;

                        stepExecutorService.execute(() -> {
                            try {
                                RetryDownloader retryDownloader = new RetryDownloader(controller, singletonDownloader1);
                                retryDownloader.getDownloaderListeners().addAll(downloaderListeners);
                                retryDownloader.process(new DownloadEntity(refererUrl, vbUrl, vbUrls, flvPathTemp, dirPath, retry));
                            } catch (Throwable e) {
                                state.error = true;
                                throw new RuntimeException(e);
                            } finally {
                                state.countDown();
                            }
                        });
                        // 下载音频文件
                        String aBUrl = audioBeanList.get(0).getBaseUrl();
                        List<String> aBUrls = audioBeanList.get(0).getBackupUrl();
                        RetryDownloader retryDownloader = new RetryDownloader(controller, singletonDownloader2);
                        retryDownloader.getDownloaderListeners().addAll(downloaderListeners);
                        retryDownloader.process(new DownloadEntity(refererUrl, aBUrl, aBUrls, mp3Path, dirPath, retry));

                        // 等待 异步线程的完成
                        state.await();

                        // 其中一个下载失败,则两个重新下载,因这种处理方式简单
                        if (state.error) {
                            // 取消视频的下载,并删除,,如果下载完成则无效
                            singletonDownloader2.cancel();
                            throw new RuntimeException();
                        }
                        // 限流
                        try {
                            semaphore.acquire();
                            new MergeAudioAndVideoFile(flvPath, mp3Path, fileName).run();
                        } finally {
                            // 必须放到finally 里面
                            semaphore.release();
                        }

                    } catch (Throwable throwable) {

                        // 取消音频的下载,并删除 ,如果下载完成则无效
                        singletonDownloader1.cancel();
                        // 失败则删除文件 和抛出异常
                        try {
                            if (Objects.nonNull(flvPath) && Files.exists(flvPath))
                                Files.delete(flvPath);
                            if (Objects.nonNull(mp3Path) && Files.exists(mp3Path))
                                Files.delete(mp3Path);
                        } catch (IOException e) {
                            logger.debug("文件删除失败");
                        }
                        throw new RuntimeException(throwable);
                    }
                });
    }

}
