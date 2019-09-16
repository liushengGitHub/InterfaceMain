package liusheng.main.app.bilibili;

import liusheng.main.app.bilibili.processor.*;
import liusheng.main.app.bilibili.processor.animal.AnimalPagesBeanToVideoBean;
import liusheng.main.app.bilibili.processor.animal.UrlToAnimalPagesBean;
import liusheng.main.app.bilibili.processor.av.PagesBeanToVideoBean;
import liusheng.main.app.bilibili.processor.av.UrlToPagesBean;
import liusheng.main.pipeline.DefaultPipeline;

public class Types {

    public static  void  completelyAVAll(String url , String downloadDir) throws Throwable {
        DefaultPipeline defaultPipeline = new DefaultPipeline();

        defaultPipeline.addLast(new UrlToPagesBean());
        defaultPipeline.addLast(new PagesBeanToVideoBean(downloadDir));
        defaultPipeline.addLast(new ListVideoBeanToDisk());
        defaultPipeline.processor(url);
    }

    public static  void  completelyAnimalAll(String url , String downloadDir) throws Throwable {
        DefaultPipeline defaultPipeline = new DefaultPipeline();
        defaultPipeline.addLast(new UrlToAnimalPagesBean());
        defaultPipeline.addLast(new AnimalPagesBeanToVideoBean(downloadDir));
        defaultPipeline.addLast(new ListVideoBeanToDisk());

        System.out.println(defaultPipeline.processor(url));
    }
    public static  void  completelyAnimalSingle(String url , String downloadDir) throws Throwable {
        DefaultPipeline defaultPipeline = new DefaultPipeline();
        defaultPipeline.addLast(new UrlToAnimalPagesBean(true));
        defaultPipeline.addLast(new AnimalPagesBeanToVideoBean(downloadDir));
        defaultPipeline.addLast(new ListVideoBeanToDisk());

        System.out.println(defaultPipeline.processor(url));
    }

}
