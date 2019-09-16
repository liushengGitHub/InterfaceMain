package liusheng.main.app.bilibili.processor.av;

import liusheng.main.app.bilibili.ThreadSafe;
import liusheng.main.app.bilibili.adapter.AdapterParam;
import liusheng.main.app.bilibili.adapter.DefaultParserAdapter;
import liusheng.main.app.bilibili.adapter.ParserAdapter;
import liusheng.main.app.bilibili.entity.AbstractVideoBean;
import liusheng.main.app.bilibili.entity.av.AllPageBean;
import liusheng.main.app.bilibili.entity.av.PagesBean;
import liusheng.main.app.bilibili.util.StringUtils;
import liusheng.main.process.AbstractLinkedListableProcessor;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;

@ThreadSafe
// 必须做到无状态 这个链是线程安全的
public class PagesBeanToVideoBean extends AbstractLinkedListableProcessor<PagesBean, PagesBeanToVideoBean> {


    private final Logger logger = Logger.getLogger(PagesBeanToVideoBean.class);
    private final ParserAdapter parserAdapter = new DefaultParserAdapter();
    private final String dir;

    public PagesBeanToVideoBean(String dir) {
        this.dir = Objects.requireNonNull(dir);
    }

    @Override
    protected void doProcess(PagesBean pages, List<Object> returnData) throws Throwable {
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
        // 返回所有解析的对象
        pageBeanList.parallelStream().forEach(page -> {

            try {

                // 获取视频的索引
                int index = page.getPage();

                // 文件名字 ，不包含后缀
                String name = index + "_" + page.getPart();

                // 每一p的 url
                String vUrl = pages.getUrl() + "?p=" + index;
                // 打印日志
                logger.debug("Start Parse " + vUrl);

                // 找到合适的解析器 ,解析对象，有两种类型 NewVideoBean 和 OldVideoBean
                AdapterParam param  = new AdapterParam();

                param.setUrl(vUrl);
                param.getMap().put("cid", pages.getVideoData().getCid());
                param.getMap().put("aid", pages.getAid());

                AbstractVideoBean abstractVideoBean = (AbstractVideoBean) parserAdapter.handle(param).parse(vUrl);
                abstractVideoBean.setName(StringUtils.fileNameHandle(name));
                abstractVideoBean.setDirFile(dirFile);
                abstractVideoBean.setUrl(vUrl);
                abstractVideoBean.setPageSize(pageBeanList.size());
                returnData.add(abstractVideoBean);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

    }
}
