package liusheng.main.app.bilibili.executor;

import liusheng.main.app.bilibili.adapter.AdapterParam;
import liusheng.main.app.bilibili.adapter.DefaultParserAdapter;
import liusheng.main.app.bilibili.adapter.ParserAdapter;
import liusheng.main.app.bilibili.entity.AbstractVideoBean;
import liusheng.main.app.bilibili.entity.av.AllPageBean;
import liusheng.main.app.bilibili.entity.av.PagesBean;
import liusheng.main.util.StringUtils;
import liusheng.main.process.AbstractLinkedProcessor;
import org.apache.log4j.Logger;

import java.io.File;

public class BilibiliTask implements Runnable{
    private final Logger logger = Logger.getLogger(BilibiliTask.class);
    private final PagesBean pages;
    private final AllPageBean page;
    private final File dirFile;
    private final AbstractLinkedProcessor linkedProcessor;
    private final int size;
    private final ParserAdapter parserAdapter = new DefaultParserAdapter();

    public BilibiliTask(PagesBean pages, AllPageBean page, File dirFile, AbstractLinkedProcessor linkedProcessor, int size) {
        this.pages = pages;
        this.page = page;
        this.dirFile = dirFile;
        this.linkedProcessor = linkedProcessor;
        this.size = size;
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
