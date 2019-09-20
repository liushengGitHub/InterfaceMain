package liusheng.main.app.bilibili.processor.av;

import liusheng.main.annotation.ThreadSafe;
import liusheng.main.app.bilibili.entity.av.AllPageBean;
import liusheng.main.app.bilibili.entity.av.PagesBean;
import liusheng.main.app.bilibili.executor.BilibiliTask;
import liusheng.main.app.bilibili.executor.FailTask;
import liusheng.main.util.StringUtils;
import liusheng.main.process.AbstractLinkedListableProcessor;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;

@ThreadSafe
// 必须做到无状态 这个链是线程安全的
public class PagesBeanToVideoBean extends AbstractLinkedListableProcessor<PagesBean, PagesBeanToVideoBean> {


    private final Logger logger = Logger.getLogger(PagesBeanToVideoBean.class);

    private final String dir;


    public PagesBeanToVideoBean(String dir) {
        this.dir = Objects.requireNonNull(dir);
    }


    @Override
    protected void doProcess(PagesBean pages, List<Object> returnData) throws Throwable {


        try {
            //PagesBean pages = pagesBean;
            // 这个视频的所有信息
            List<AllPageBean> pageBeanList = Optional.ofNullable(pages)
                    .map(p -> p.getVideoData()).map(videoDataBean -> videoDataBean.getPages()).orElse(Collections.emptyList());

            // 为null 说明没有这个视频
            if (pageBeanList.isEmpty()) return;

            // 这个视频的名字
            String videoName = Optional.ofNullable(pages).map(p -> p.getVideoData()).map(videoDataBean -> StringUtils.isEmpty(videoDataBean.getTitle())
                    ? UUID.randomUUID().toString() : StringUtils.fileNameHandle(videoDataBean.getTitle())).get();


            // 创建这个视频的专有dir
            File dirFile = new File(dir, StringUtils.fileNameHandle(videoName));

            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            ExecutorService executorService = pipeline().getExecutorService();

            // 返回所有解析的对象
            pageBeanList.parallelStream().forEach(page -> {

                try {
                    // 获取视频的索引
                    executorService.execute(new FailTask(
                            new BilibiliTask(pages, page, dirFile, nextProcessor(), pageBeanList.size())));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

    }
}
