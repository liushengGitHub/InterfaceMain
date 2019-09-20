package liusheng.main.userInter.entity;

import liusheng.main.app.adapter.SearchPageParser;
import liusheng.main.app.bilibili.parser.Parser;

public class ComboBoxEntity {
    // 搜索的Url Pattern
    private String pattern;
    // 在页面显示的名字
    private String labelName;
    // 解析搜索页面的 把Json转化为 对象
    private Parser<?> parser ;
    private SearchPageParser searchPageParser; // 把对象转换为我们对象的

    public Parser<?> getParser() {
        return parser;
    }

    public void setParser(Parser<?> parser) {
        this.parser = parser;
    }

    public ComboBoxEntity() {

    }

    public SearchPageParser getSearchPageParser() {
        return searchPageParser;
    }

    public void setSearchPageParser(SearchPageParser searchPageParser) {
        this.searchPageParser = searchPageParser;
    }

    public ComboBoxEntity(String pattern, String labelName, Parser<?> parser, SearchPageParser searchPageParser) {
        this.pattern = pattern;
        this.labelName = labelName;
        this.parser = parser;
        this.searchPageParser = searchPageParser;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
}
