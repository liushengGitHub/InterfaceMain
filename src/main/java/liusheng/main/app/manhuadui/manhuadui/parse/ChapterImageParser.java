package liusheng.main.app.manhuadui.manhuadui.parse;

import liusheng.main.app.manhuadui.manhuadui.entity.Image;
import liusheng.main.app.manhuadui.manhuadui.script.MHScriptExecutor;
import liusheng.main.app.manhuadui.parse.AbstractParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ChapterImageParser extends AbstractParser<List<Image>> {

    private static final Pattern CHECK_REGEX = Pattern.compile( "https://www\\.manhuadui\\.com/manhua/\\S+/\\d+\\.html");
    private final MHScriptExecutor mhScriptExecutor = new MHScriptExecutor();
    @Override
    protected List<Image> trulyParse(Document document) {

        Element element = document.select("div.mainNav")
                // <script> 下的是html 类型 不是text
                .first();

        List<Image> list = new LinkedList<>();
        Optional.ofNullable(element)
                .map(e->e.nextElementSibling())
                .map(e-> e.html())
                .ifPresent(html-> {
                    try {
                        mhScriptExecutor.execute(html);
                        list.addAll( mhScriptExecutor.getChapterImages());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        return Collections.unmodifiableList(list);
    }

    @Override
    public boolean check(String url) {

        return CHECK_REGEX.matcher(url).matches();
    }
}
