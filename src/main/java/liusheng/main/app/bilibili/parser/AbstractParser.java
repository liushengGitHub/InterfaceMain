package liusheng.main.app.bilibili.parser;

import com.google.gson.Gson;
import liusheng.main.app.bilibili.util.ConnectionUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.regex.Pattern;

public class AbstractParser<T> implements  Parser<T> {
    private static final Pattern CHECK = Pattern.compile("https://www.bilibili.com/video/av\\d+/\\?p=\\d");
    private static final String PREFIX = "window.__playinfo__=";
    private String content ;

    private final Logger logger ;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean check(String url) {
        return CHECK.matcher(url).matches();
    }

    private final  Class<T> clazz ;
    {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();

        clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];

        logger =  Logger.getLogger(clazz);
    }
    @Override
    public T parse(String url) throws IOException {
        if ( content == null) {

            Document document = ConnectionUtils.getConnection(url).get();
            content  = document.select("script").get(2).html().substring(PREFIX.length());

        }
        String script = content;

        logger.info(script);

        return new Gson().fromJson(script,clazz);
    }
}
