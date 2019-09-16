package liusheng.main.app.acfun.parser;

import liusheng.main.app.acfun.entity.M3U8Bean;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptParser  implements Parser<M3U8Bean> {

    private final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    @Override
    public M3U8Bean parse(String content) throws Exception {
        ScriptEngine engine = scriptEngineManager.getEngineByExtension("js");
        engine.eval(content);
        M3U8Bean m3U8Bean = new M3U8Bean();
        return m3U8Bean;
    }
}
