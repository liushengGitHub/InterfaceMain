package liusheng.main.userInter.entity;

import liusheng.main.app.adapter.SearchPageParser;
import liusheng.main.app.bilibili.parser.Parser;

public class ComboBoxEntity {
    private String pattern;
    private String labelName;
    private Parser<?> parser ;
    private SearchPageParser searchPageParser;
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
