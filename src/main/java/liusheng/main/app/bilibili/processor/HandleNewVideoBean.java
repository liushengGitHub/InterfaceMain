package liusheng.main.app.bilibili.processor;

import liusheng.main.app.bilibili.donwload.RetryDownloader;
import liusheng.main.app.bilibili.entity.DashBean;
import liusheng.main.app.bilibili.entity.DownloadEntity;
import liusheng.main.app.bilibili.entity.NewVideoBean;
import liusheng.main.app.bilibili.executor.MergeAudioAndVideoFile;
import liusheng.main.listener.ProcessListener;
import liusheng.main.process.AbstractLinkedListableProcessor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class HandleNewVideoBean extends AbstractLinkedListableProcessor<NewVideoBean,HandleNewVideoBean> {

    private List<? extends RetryDownloader.DownloaderListener> downloaderListeners;
    private final ExecutorService stepExecutorService;
    private int retry = 3;

    public HandleNewVideoBean(List<? extends RetryDownloader.DownloaderListener> downloaderListeners,ExecutorService stepExecutorService, int retry) {
        this.downloaderListeners = downloaderListeners;

        this.stepExecutorService = stepExecutorService;
        this.retry = retry;
        //this.addListener(new DefaultProcessListener(retry));
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
    static  class SatetCountDownLatch extends CountDownLatch{
        volatile  boolean error= false;
       public SatetCountDownLatch(int count) {
           super(count);
       }
   }
    @Override
    protected void doProcess(NewVideoBean newVideoBean, List<Object> returnData) throws Throwable {
        Optional.ofNullable(newVideoBean).map(NewVideoBean::getData)
                .map(NewVideoBean.DataBean::getDash)
                .ifPresent(dashBean -> {
                    try {
                            List<DashBean.AudioBean> audioBeanList = dashBean.getAudio();

                            List<DashBean.VideoBean> videoBeanList = dashBean.getVideo();

                            if ( audioBeanList == null || audioBeanList.size() == 0) {
                                return ;
                            }

                            if ( videoBeanList == null || videoBeanList.size() == 0) {
                                return ;
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
                                videoBeanList.stream().filter(video-> video.getId()== Integer.getInteger(quality)).findFirst();
                            }



                            List<String> vbUrls = videoBean.getBackupUrl();
                            String vbUrl = videoBean.getBaseUrl();

                            Path dirPath = dirFile.toPath();
                            Path flvPath=  dirPath.resolve(fileName + ".flv.temp");
                            Path mp3Path=  dirPath.resolve(fileName + ".mp3.temp");

                            stepExecutorService.execute(()->{
                                try {
                                    RetryDownloader retryDownloader = new RetryDownloader();
                                    retryDownloader.getDownloaderListeners().addAll(downloaderListeners);
                                    retryDownloader .process(new DownloadEntity(refererUrl,vbUrl,vbUrls,flvPath,dirPath,retry));
                                }catch (Throwable e) {
                                    state.error = true ;
                                    throw  new RuntimeException(e);
                                }finally {
                                    state.countDown();
                                }
                            });

                            String aBUrl = audioBeanList.get(0).getBaseUrl();
                            List<String> aBUrls = audioBeanList.get(0).getBackupUrl();
                            RetryDownloader retryDownloader = new RetryDownloader();
                            retryDownloader.getDownloaderListeners().addAll(downloaderListeners);
                            retryDownloader .process(new DownloadEntity(refererUrl,aBUrl,aBUrls,mp3Path,dirPath,retry));

                            state.await();

                            if (!state.error){
                                new MergeAudioAndVideoFile(flvPath,mp3Path,fileName).run();
                            }

                    }catch (Throwable throwable){
                        throw  new RuntimeException(throwable);
                    }
                });
    }

}
