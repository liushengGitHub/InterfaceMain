package liusheng.main.app.manhuadui.parse;

import liusheng.main.app.acfun.entity.AcfunSearchJson;
import liusheng.main.app.adapter.SearchPageParser;
import liusheng.main.app.entity.SearchItem;
import liusheng.main.app.entity.SearchPage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ManHuaDuiSearchPageParser implements SearchPageParser {

    @Override
    public SearchPage parse(Object o) {

        if (o instanceof SearchPage) {
            return (SearchPage) o;
        }
        return new SearchPage(Collections.emptyList(), 1);
    }
}
