package liusheng.main.app.entity;

import java.util.List;

public class SearchPage {
    private List<SearchItem> items;
    private int pages;

    public SearchPage(List<SearchItem> items, int pages) {
        this.items = items;
        this.pages = pages;
    }

    public SearchPage() {

    }

    public List<SearchItem> getItems() {
        return items;
    }

    public void setItems(List<SearchItem> items) {
        this.items = items;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
