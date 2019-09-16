package liusheng.main.app.bilibili.adapter;

import liusheng.main.app.bilibili.parser.NewParser;
import liusheng.main.app.bilibili.parser.OldParser;
import liusheng.main.app.bilibili.parser.Parser;
import liusheng.main.app.bilibili.util.ConnectionUtils;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DefaultParserAdapter implements ParserAdapter {
    private static final String TAG = "\"dash\"";
    private static final String PREFIX = "window.__playinfo__=";
    @Override
    public Parser<?> handle(String url) throws IOException {
        Document document = ConnectionUtils.getConnection(url)
                .execute().parse();
        String script = document.select("script").get(2).html().substring(PREFIX.length());
        Parser<?> parser = null;
        if (script.contains(TAG)) {
            NewParser newParser = new NewParser();
            newParser.setContent(script);
            parser = newParser;
        } else {
            OldParser oldParser = new OldParser();
            oldParser.setContent(script);
            parser = oldParser;
        }
        return parser;
    }
}
