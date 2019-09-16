package liusheng.main.app.bilibili.processor.av;

import liusheng.main.app.bilibili.entity.av.PagesBean;
import liusheng.main.app.bilibili.parser.PageInfoParser;
import liusheng.main.process.AbstractLinkedListableProcessor;

import java.util.List;

public class UrlToPagesBean extends AbstractLinkedListableProcessor<String, UrlToPagesBean> {

    private final PageInfoParser pageInfoParser = new PageInfoParser();

    @Override
    protected void doProcess(String url, List<Object> returnData) throws Throwable {
        if ( !pageInfoParser.check(url)) throw  new IllegalArgumentException("");
        PagesBean pagesBean = pageInfoParser.parse(url);

        pagesBean.setUrl(url);
        returnData.add(pagesBean);
    }
}
