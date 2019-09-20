package liusheng.main.app.manhuadui.parse;

import liusheng.main.app.bilibili.parser.Parser;
import liusheng.main.util.ConnectionUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractParser<T> implements Parser<T> {
    protected static final AtomicInteger id = new AtomicInteger(1);
    @Override
    public T parse(String url) throws IOException {
        Objects.requireNonNull(url);
        if (!check(url)) throw  new IllegalArgumentException();

        return reParse(4, url);
    }

    public T reParse(int n, String url) throws IOException {

        try {
            return doParse(url);
        } catch (IOException e) {
            if (n == 0) {
                e.printStackTrace();
               throw  new RuntimeException(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return reParse(n - 1, url);

        }
    }
    protected T doParse(String url) throws IOException {
        Connection connection = ConnectionUtils.getConnection(url);
        Document document = connection.execute().parse();
        return trulyParse(document);
    }


    protected abstract T trulyParse(Document document);
}
