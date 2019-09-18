package liusheng.main.app.bilibili.parser;

import com.google.gson.Gson;
import liusheng.main.app.bilibili.entity.av.PagesBean;
import liusheng.main.app.bilibili.util.ConnectionUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.regex.Pattern;

public class PageInfoParser implements Parser<PagesBean> {
    //                                                           http://www.bilibili.com/video/av67748053
    public static final String HTTPS_WWW_BILIBILI_COM_VIDEO_AV ="https://www.bilibili.com/video/av" ;
    private static final Pattern CHECK = Pattern.compile("https?://www.bilibili.com/video/av\\d+");
    private static final String PREFIX = "window.__INITIAL_STATE__=";

    static Logger logger = Logger.getLogger(PageInfoParser.class);

    @Override
    public boolean check(String url) {
        return CHECK.matcher(url).matches();
    }

    @Override
    public PagesBean parse(String url) throws IOException {
        Connection connection = ConnectionUtils.getConnection(url);

        if (connection == null) {
            return null;
        }

        Document document = connection
                .get();

        Gson gson = new Gson();
        String script = document.select("script").stream().map(Element::html).filter(h -> h.contains(PREFIX)).findFirst().get().substring(PREFIX.length());
        int i = script.lastIndexOf("]};(function(){");
        if (i == -1) throw new RuntimeException();
        PagesBean pageBean = gson.fromJson(script.substring(0, i + 2), PagesBean.class);

        return pageBean;
    }
}
