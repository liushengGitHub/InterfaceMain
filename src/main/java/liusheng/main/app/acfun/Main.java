package liusheng.main.app.acfun;

import liusheng.main.app.acfun.processor.M3U8BeanToSecondBean;
import liusheng.main.app.acfun.processor.SecondBeanToDisk;
import liusheng.main.app.acfun.processor.UrlToM3U8Bean;
import liusheng.main.pipeline.DefaultPipeline;

public class Main {
    public static void main(String[] args) throws Throwable {
        DefaultPipeline pipeline = new DefaultPipeline();

        pipeline.addLast(new UrlToM3U8Bean("d:\\hello"));
        pipeline.addLast(new M3U8BeanToSecondBean());
        pipeline.addLast(new SecondBeanToDisk());
        Object processor = pipeline.processor("https://www.acfun.cn/bangumi/ab5024701_34011_323919");

    }
}
