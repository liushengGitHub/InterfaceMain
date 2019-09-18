package liusheng.test;

import liusheng.main.app.bilibili.SearchJson;
import liusheng.main.app.bilibili.SearchInfoParser;
import org.junit.Test;

public class Main {
    @Test
    public void test1() throws Exception {
        SearchInfoParser parser = new SearchInfoParser();
        if (!parser.check("https://search.bilibili.com/all?keyword=cxk&page=2")) {
            throw new RuntimeException();
        }
        for (int i = 1; i < 20; i++) {

            Object json = parser.parse("https://search.bilibili.com/all?keyword=webflux&page=" + i);
            System.out.println(json);
        }


    }
}
