package liusheng.main.app.manhuadui.manhuadui.script;

import liusheng.main.app.manhuadui.manhuadui.entity.Image;
import liusheng.main.app.manhuadui.script.JSScriptExecutor;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MHScriptExecutor implements JSScriptExecutor {
    private final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    private final List<Image> imageLinkedList = new LinkedList<>();
    private static final  String PREFIX = "https://mhcdn.manhuazj.com/";
    // https://res.333dm.com
    //https://res02.333dm.com
    @Override
    public void execute(String jsScript) throws Exception {
        ScriptEngine engine = scriptEngineManager.getEngineByExtension("js");
        InputStream resourceAsStream = MHScriptExecutor.class.getClassLoader().getResourceAsStream("utils.js");
        engine.eval(jsScript);
        engine.eval(new InputStreamReader(resourceAsStream));

        Invocable invocable = (Invocable) engine;

        List<String> images = (List<String>) invocable.invokeFunction("getImages");

        String path = (String) invocable.invokeFunction("getChapterPath");

        if (images.size() > 0 && images.get(0).startsWith("https:")){
            imageLinkedList.addAll(   images.stream().map(image -> image.replace("\\","")).map(image->new Image(image)).collect(Collectors.toList()));
        }else {
            imageLinkedList.addAll( images.stream().map(image -> PREFIX  + path + image).map(image->new Image(image)).collect(Collectors.toList()));
        }
    }

    public List<Image> getChapterImages() {
        return imageLinkedList;
    }
}
