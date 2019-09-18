package liusheng.main.app.bilibili.executor;

import liusheng.main.app.bilibili.adapter.AdapterParam;
import liusheng.main.app.bilibili.adapter.DefaultParserAdapter;
import liusheng.main.app.bilibili.adapter.ParserAdapter;
import liusheng.main.app.bilibili.donwload.RetryDownloader;
import liusheng.main.app.bilibili.entity.AbstractVideoBean;
import liusheng.main.app.bilibili.entity.NewVideoBean;
import liusheng.main.app.bilibili.entity.OldVideoBean;
import liusheng.main.app.bilibili.entity.av.AllPageBean;
import liusheng.main.app.bilibili.entity.av.PagesBean;
import liusheng.main.app.bilibili.processor.HandleNewVideoBean;
import liusheng.main.app.bilibili.processor.HandleOldVideoBean;
import liusheng.main.app.bilibili.processor.ListVideoBeanToDisk;
import liusheng.main.app.bilibili.processor.av.PagesBeanToVideoBean;
import liusheng.main.app.bilibili.util.StringUtils;
import liusheng.main.process.AbstractLinkedProcessor;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

public class FailTask implements Runnable {
    private final Logger logger = Logger.getLogger(FailTask.class);
    public static final int COUNT = 3;
    private final PagesBean pages;
    private final AllPageBean page;
    private final File dirFile;
    private final AbstractLinkedProcessor linkedProcessor;
    private final int size;
    private final ParserAdapter parserAdapter = new DefaultParserAdapter();


    private int count = 0;

    public int getCount() {
        return count;
    }

    public void count() {
        count++;
    }

    public FailTask(PagesBean pages, AllPageBean page, File dirFile, AbstractLinkedProcessor linkedProcessor, int size) {
        this.pages = pages;
        this.page = page;
        this.dirFile = dirFile;
        this.linkedProcessor = linkedProcessor;
        this.size = size;
    }

    public PagesBean getPages() {
        return pages;
    }

    @Override
    public void run() {

        try {
            int index = page.getPage();

            // 文件名字 ，不包含后缀
            String name = index + "_" + page.getPart();

            // 每一p的 url
            String vUrl = pages.getUrl() + "?p=" + index;
            // 打印日志
            logger.debug("Start Parse " + vUrl);
            // 找到合适的解析器 ,解析对象，有两种类型 NewVideoBean 和 OldVideoBean
            AdapterParam param = new AdapterParam();

            param.setUrl(vUrl);
            param.getMap().put("cid", pages.getVideoData().getCid());
            param.getMap().put("aid", pages.getAid());

            AbstractVideoBean abstractVideoBean = abstractVideoBean = (AbstractVideoBean) parserAdapter.handle(param).parse(vUrl);
            abstractVideoBean.setName(StringUtils.fileNameHandle(name));
            abstractVideoBean.setDirFile(dirFile);
            abstractVideoBean.setUrl(vUrl);
            abstractVideoBean.setPageSize(size);

            linkedProcessor.process(abstractVideoBean);

        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
