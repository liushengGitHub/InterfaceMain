package liusheng.main.app.bilibili.entity.animal;

public class AnimalPageBean {
    @Override
    public String toString() {
        return "AnimalPageBean{" +
                "index=" + index +
                ", longTitle='" + longTitle + '\'' +
                ", id=" + id +
                ", url='" + url + '\'' +
                ", refererUrl='" + refererUrl + '\'' +
                '}';
    }

    private int index ;
    private String longTitle ;
    private int id ;
    private String url;
    private String refererUrl;

    public AnimalPageBean() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLongTitle() {
        return longTitle;
    }

    public void setLongTitle(String longTitle) {
        this.longTitle = longTitle;
    }

    public  int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRefererUrl() {
        return refererUrl;
    }

    public void setRefererUrl(String refererUrl) {
        this.refererUrl = refererUrl;
    }

    public AnimalPageBean(int index, String longTitle, int id, String url, String refererUrl) {
        this.index = index;
        this.longTitle = longTitle;
        this.id = id;
        this.url = url;
        this.refererUrl = refererUrl;
    }
}
