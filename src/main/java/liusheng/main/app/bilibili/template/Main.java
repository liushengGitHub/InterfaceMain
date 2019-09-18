package liusheng.main.app.bilibili.template;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.engine.thymeleaf.ThymeleafEngine;

import java.util.Collections;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {


        ThymeleafEngine engine = new ThymeleafEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));

        Template template = engine.getTemplate("helloWorld.fxml");

        String numbers = template.render(Collections.singletonMap("numbers",
                IntStream.range(0, 10).toArray()));

        System.out.println(numbers);
    }
}
