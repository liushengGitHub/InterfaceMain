package liusheng.main.app.acfun.parser;

import com.google.gson.Gson;
import liusheng.main.app.acfun.entity.AcfunSearchJson;
import liusheng.main.app.bilibili.parser.Parser;
import liusheng.main.util.ConnectionUtils;

import java.io.IOException;

public class AcfunSearchInfoParser implements Parser<Object> {

    private final Gson gson = new Gson();
    @Override
    public boolean check(String url) {
        return true;
    }

    @Override
    public Object parse(String url) throws IOException {
        String body = ConnectionUtils.getConnection(url).execute().body();

        AcfunSearchJson searchJson = gson.fromJson(body, AcfunSearchJson.class);

        return searchJson;
    }
}
