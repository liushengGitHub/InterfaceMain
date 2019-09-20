package liusheng.main.app.manhuadui.manhuadui.entity;

public class Chapter {
    private final String chapterUrl ;
    private final String name;

    public String getChapterUrl() {
        return chapterUrl;
    }

    public String getName() {
        return name;
    }

    public Chapter(String chapterUrl, String name) {
        this.chapterUrl = chapterUrl;
        this.name = name;
    }
}
