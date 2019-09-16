package liusheng.main.app.bilibili.adapter;

import liusheng.main.app.bilibili.parser.Parser;

import java.io.IOException;

public interface ParserAdapter {

    Parser<?> handle(String url) throws IOException;
}
