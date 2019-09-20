package liusheng.main.app.manhuadui.parse;

import liusheng.main.app.bilibili.parser.Parser;
import liusheng.main.util.ConnectionUtils;
import liusheng.main.app.entity.SearchItem;
import liusheng.main.app.entity.SearchPage;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ManHuaDuiSearchInfoParser implements Parser<Object> {

    @Override
    public boolean check(String url) {
        return true;
    }

    @Override
    public Object parse(String url) throws IOException {

        Document document = ConnectionUtils.getConnection(url).get();

        List<SearchItem> list = document.select("ul.list_con_li li.list-comic")
                .stream()
                .map(element -> {

                    Element img = element.select("img").first();
                    String imgSrc = "";
                    if (img != null) {
                        imgSrc = img.attr("abs:src");
                    }
                    Element a = element.select("a").first();
                    String title = "";
                    String href = "";
                    String authName = "";
                    if (a != null) {
                        title = a.attr("title");
                        href = a.attr("abs:href");
                    }
                    Element auth = element.select("p.auth").first();
                    if (auth != null) {
                        authName = auth.text();
                    }
                    return new SearchItem(href, title, imgSrc, authName);
                }).collect(Collectors.toList());


        Element element = document.select("ul.pagination li.last").first();

        int pages = 1;
        if (Objects.nonNull(element)) {

            Element a = element.select("a").first();
            if (Objects.nonNull(a)) {
                String href = a.attr("href");
                pages = getPages(href);
            }
        }
        return new SearchPage(list,pages);
    }

    private int getPages(String href) {

        int i = href.indexOf("?");
        if (i == -1) return  1;
        String[] querys = href.substring(i + 1).split("&");
        return   Arrays.asList(querys).stream()
                .filter(q -> q.contains("page"))
                .map(q -> {
                    String[] split = q.split("=");
                    if (split.length == 2) {
                        return Integer.parseInt(split[1]);
                    }
                    return 1;
                })
                .findFirst().orElse(1);
    }

    public static void main(String[] args) throws IOException {
        ManHuaDuiSearchInfoParser manHuaDuiSearchInfoParser = new ManHuaDuiSearchInfoParser();

        manHuaDuiSearchInfoParser.parse("https://www.manhuadui.com/search/?keywords=女友");
    }
}
