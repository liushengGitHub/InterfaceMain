package liusheng.main.app.acfun.processor;

import com.google.gson.Gson;
import liusheng.main.app.acfun.entity.DataBean;
import liusheng.main.app.acfun.entity.M3u8Bean;
import liusheng.main.app.bilibili.util.ConnectionUtils;
import liusheng.main.process.AbstractLinkedListableProcessor;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

public class UrlToM3u8Bean extends AbstractLinkedListableProcessor<String, UrlToM3u8Bean> {
    private final Logger logger = Logger.getLogger(UrlToM3u8Bean.class);
    public static final String WINDOW_PAGE_INFO_WINDOW_BANGUMI_DATA = "window.pageInfo ";
    private final Gson gson = new Gson();

    @Override
    protected void doProcess(String s, List<Object> returnData) throws Throwable {
        Document document = ConnectionUtils.getConnection(s).get();

        String script = document.select("script").stream().map(Element::html)
                .filter(s1 -> s1.contains(WINDOW_PAGE_INFO_WINDOW_BANGUMI_DATA)).findFirst()
                .orElseThrow(RuntimeException::new);


        int end = script.lastIndexOf("window.addEventListener");
        int start = script.indexOf("{");
        if (end != -1 && start != -1 && end >start) {
            script = script.substring(start, end);
            script = script.trim();
            script = script.substring(0, script.length() - 1);
        }
        M3u8Bean m3u8Bean = gson.fromJson(script, M3u8Bean.class);
        String playJson = m3u8Bean.getCurrentVideoInfo().getKsPlayJson();
        m3u8Bean.setDataBean(gson.fromJson(playJson, DataBean.class));

        returnData.add(m3u8Bean);
    }
}
