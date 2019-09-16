package liusheng.main.app.acfun.parser;

public interface Parser<T> {
    T parse(String content) throws Exception;
}
