package liusheng.main.app.bilibili.parser;

import com.google.gson.Gson;
import liusheng.main.app.bilibili.entity.animal.AnimalPagesBean;
import liusheng.main.app.bilibili.util.ConnectionUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.regex.Pattern;

public class AnimalPageInfoParser implements Parser<AnimalPagesBean> {
    private static final String PREFIX = "window.__INITIAL_STATE__=";
    @Override
    public boolean check(String url) {
        return Pattern.compile(":https://www.bilibili.com/bangumi/play/ep\\d+").matcher(url).matches();
    }

    @Override
    public AnimalPagesBean parse(String url) throws IOException {

        Document document = ConnectionUtils.getConnection(url).get();
        String script = document.select("script").stream().map(Element::html).filter(s -> s.contains(PREFIX)).findFirst().orElseThrow(RuntimeException::new);

        script = script.trim().substring(PREFIX.length());
        int i = script.lastIndexOf("]};(function(){");
        if (i == - 1) throw new RuntimeException();

        Gson gson = new Gson();
        script = script.substring(0, i + 2);
        return gson.fromJson(script,AnimalPagesBean.class);
    }
}
