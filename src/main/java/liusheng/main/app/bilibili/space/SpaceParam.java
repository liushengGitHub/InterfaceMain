package liusheng.main.app.bilibili.space;
//?mid=161419374&pagesize=30&tid=0&page=1&keyword=&order=pubdate
public class SpaceParam {
    public static final String HTTPS_SPACE_BILIBILI_COM_AJAX_MEMBER_GET_SUBMIT_VIDEOS = "https://space.bilibili.com/ajax/member/getSubmitVideos";
    private String mid;
    private String tid = "0";
    private String pagesize = "30" ;
    private String page ;
    private String keyword = null;
    private String order = "pubdate";

    public SpaceParam(String mid, String tid, String pagesize, String page, String keyword, String order) {
        this.mid = mid;
        this.tid = tid;
        this.pagesize = pagesize;
        this.page = page;
        this.keyword = keyword;
        this.order = order;
    }

    public SpaceParam(String mid,String pageIndex) {
        this(mid,"0","30",pageIndex,null,"pubdate");
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(HTTPS_SPACE_BILIBILI_COM_AJAX_MEMBER_GET_SUBMIT_VIDEOS);
        stringBuilder.append("?")
                .append("mid=").append(mid).append("&")
                .append("tid=").append(tid).append("&")
                .append("pagesize=").append(pagesize).append("&")
                .append("page=").append(page).append("&")
                .append("order=").append(order);
        if (keyword != null) {
            stringBuilder.append("&keyword=").append(keyword);
        }
        return  stringBuilder.toString();
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getPagesize() {
        return pagesize;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
