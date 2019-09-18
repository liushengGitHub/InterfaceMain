package liusheng.main.app.acfun;


import liusheng.main.app.acfun.processor.M3u8BeanToDisk;
import liusheng.main.app.acfun.processor.UrlToM3u8Bean;
import liusheng.main.pipeline.DefaultPipeline;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Throwable {
        DefaultPipeline pipeline = new DefaultPipeline();

        pipeline.addLast(new UrlToM3u8Bean());
        pipeline.addLast(new M3u8BeanToDisk(Paths.get("d:\\hello")));
        Object processor = pipeline.processor("https://www.acfun.cn/bangumi/ab5021860_36188_271399");

    }
}
