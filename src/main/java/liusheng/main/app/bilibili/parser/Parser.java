package liusheng.main.app.bilibili.parser;

import java.io.IOException;

public interface Parser<T> {
    boolean check(String url);
    T parse(String url) throws IOException;
}
