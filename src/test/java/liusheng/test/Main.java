package liusheng.test;

import cn.hutool.core.util.ClassLoaderUtil;
import liusheng.main.app.bilibili.parser.BilibiliSearchInfoParser;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
    @Test
    public void test1() throws Exception {
        BilibiliSearchInfoParser parser = new BilibiliSearchInfoParser();
        if (!parser.check("https://search.bilibili.com/all?keyword=cxk&page=2")) {
            throw new RuntimeException();
        }
        for (int i = 1; i < 20; i++) {

            Object json = parser.parse("https://search.bilibili.com/all?keyword=webflux&page=" + i);
            System.out.println(json);
        }


    }

    @Test
    public void test() {
        int[] nums = {1, 2, 3, 4, 5, 10, 11, 12, 13};
        int m = 5, n = 4;

        reserve(nums, 0, nums.length);
        System.out.println(Arrays.toString(nums));
        reserve(nums,0,n);
        reserve(nums,n,nums.length);
        System.out.println(Arrays.toString(nums));
    }

    private void reserve(int[] nums, int start, int end) {
        for (int i = 0; i < (end - start) / 2; i++) {
            int a = nums[i + start];
            nums[i + start] = nums[end - i - 1];
            nums[end - i - 1] = a;
        }
    }

    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    @Test
    public void test2 () throws  Exception {

        ScriptEngine js = scriptEngineManager.getEngineByExtension("js");

        js.eval(new InputStreamReader(ClassLoaderUtil.getClassLoader().getResourceAsStream("acfun.js")));

        System.out.println(js.get("b"));
    }
}
