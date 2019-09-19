package liusheng.main.app.acfun.parser;

import liusheng.main.app.acfun.entity.AcfunSearchJson;
import liusheng.main.app.adapter.SearchPageParser;
import liusheng.main.app.entity.SearchItem;
import liusheng.main.app.entity.SearchPage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AcfunSearchPageParser implements SearchPageParser {

    public static final String IMAGE_VIEW_2_1_W_100_H_100 = "?imageView2/1/w/100/h/100";
    public static final String HTTPS_WWW_ACFUN_CN_V_AC_S = "https://www.acfun.cn/v/ac%s";

    @Override
    public SearchPage parse(Object o) {
        List<SearchItem> searchItems = Optional.ofNullable(o)
                .filter(o1 -> o1 instanceof AcfunSearchJson)
                .map(o1 -> (AcfunSearchJson) o1)
                .map(AcfunSearchJson::getVideoList)
                .map(list -> {
                    return list.stream().map(videoListBean -> {
                        String title = videoListBean.getTitle();
                        String userName = videoListBean.getUserName();
                        String coverUrl = videoListBean.getCoverUrl() + IMAGE_VIEW_2_1_W_100_H_100;
                        String url = String.format(HTTPS_WWW_ACFUN_CN_V_AC_S, videoListBean.getContentId());
                        return new SearchItem(url, title, coverUrl, userName);
                    }).collect(Collectors.toList());
                }).orElse(Collections.emptyList());
        Integer pages = Optional.ofNullable(o)
                .filter(o1 -> o1 instanceof AcfunSearchJson)
                .map(a -> ((AcfunSearchJson) a).getPageNum()).orElse(1);

        return new SearchPage(searchItems, pages);
    }
}
