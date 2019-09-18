package liusheng.main.app.bilibili.processor.animal;

import liusheng.main.app.bilibili.entity.animal.AnimalPagesBean;
import liusheng.main.app.bilibili.parser.AnimalPageInfoParser;
import liusheng.main.app.bilibili.parser.Parser;
import liusheng.main.process.AbstractLinkedListableProcessor;

import java.util.List;

public class UrlToAnimalPagesBean extends AbstractLinkedListableProcessor<String, UrlToAnimalPagesBean> {
    private Parser<AnimalPagesBean> pageInfoParser = new AnimalPageInfoParser();
    private final boolean single;

    public UrlToAnimalPagesBean(boolean single) {
        this.single = single;
    }

    public UrlToAnimalPagesBean() {
        this(false);
    }

    @Override
    protected void doProcess(String url, List<Object> returnData) throws Throwable {

        if (!pageInfoParser.check(url)) throw new IllegalArgumentException();
        AnimalPagesBean animalPagesBean = pageInfoParser.parse(url);

        animalPagesBean.setUrl(url);
        animalPagesBean.setSingle(single);

        returnData.add(animalPagesBean);
    }
}
