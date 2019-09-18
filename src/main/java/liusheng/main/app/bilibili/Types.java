package liusheng.main.app.bilibili;

import liusheng.main.app.bilibili.listener.DownloadSpeedListener;
import liusheng.main.app.bilibili.processor.*;
import liusheng.main.app.bilibili.processor.animal.AnimalPagesBeanToVideoBean;
import liusheng.main.app.bilibili.processor.animal.UrlToAnimalPagesBean;
import liusheng.main.app.bilibili.processor.av.PagesBeanToVideoBean;
import liusheng.main.app.bilibili.processor.av.UrlToPagesBean;
import liusheng.main.pipeline.DefaultPipeline;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Types {

  /*  private static final Logger logger = Logger.getLogger(Types.class);

    // 下载所有一个avID的所有视频
    public static void completelyAVAll(List<String> urls, String downloadDir) throws Throwable {
        DefaultPipeline defaultPipeline = new DefaultPipeline();

        defaultPipeline.addLast(new UrlToPagesBean());
        defaultPipeline.addLast(new PagesBeanToVideoBean(downloadDir));
        DownloadSpeedListener listener = new DownloadSpeedListener();
        defaultPipeline.addLast(new ListVideoBeanToDisk(controller, listener));
        urls.forEach(url -> {
            try {
                defaultPipeline.processor(url);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            long start = -1;

            @Override
            public void run() {
                long size = listener.getSize();
                if (start == -1) {
                    start = size;
                    return;
                } else {
                    logger.info(((size - start) / 1024));
                    start = size;
                }
            }
        }, 2, 1, TimeUnit.SECONDS);
    }

    public static void completelyAVAllSyn(String url, String downloadDir) throws Throwable {

        DefaultPipeline defaultPipeline = new DefaultPipeline();
        defaultPipeline.addLast(new UrlToPagesBean());
        defaultPipeline.addLast(new PagesBeanToVideoBean(downloadDir));
        //defaultPipeline.addLast(new ListVideoBeanToDisk(listener));
        defaultPipeline.processor(url);
    }

    // 下载 所有动漫
    public static void completelyAnimalAll(String url, String downloadDir) throws Throwable {
        DownloadSpeedListener listener = new DownloadSpeedListener();

        DefaultPipeline defaultPipeline = new DefaultPipeline();
        defaultPipeline.addLast(new UrlToAnimalPagesBean());
        defaultPipeline.addLast(new AnimalPagesBeanToVideoBean(downloadDir));
        //defaultPipeline.addLast(new ListVideoBeanToDisk(controller, listener));
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            long start = -1;

            @Override
            public void run() {
                long size = listener.getSize();
                if (start == -1) {
                    start = size;
                    return;
                } else {
                    logger.info(((size - start) / 1024));
                    start = size;
                }
            }
        }, 2, 1, TimeUnit.SECONDS);
        defaultPipeline.processor(url);
    }

    //单集下载
    public static void completelyAnimalSingle(String url, String downloadDir) throws Throwable {
        DefaultPipeline defaultPipeline = new DefaultPipeline();
        defaultPipeline.addLast(new UrlToAnimalPagesBean(true));
        defaultPipeline.addLast(new AnimalPagesBeanToVideoBean(downloadDir));
        //  defaultPipeline.addLast(new ListVideoBeanToDisk(listener));

        System.out.println(defaultPipeline.processor(url));
    }*/

}
