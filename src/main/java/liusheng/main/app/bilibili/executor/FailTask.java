package liusheng.main.app.bilibili.executor;

import liusheng.main.app.bilibili.adapter.AdapterParam;
import liusheng.main.app.bilibili.adapter.DefaultParserAdapter;
import liusheng.main.app.bilibili.adapter.ParserAdapter;
import liusheng.main.app.bilibili.entity.AbstractVideoBean;
import liusheng.main.app.bilibili.entity.av.AllPageBean;
import liusheng.main.app.bilibili.entity.av.PagesBean;
import liusheng.main.app.bilibili.util.StringUtils;
import liusheng.main.process.AbstractLinkedProcessor;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * 代理模式
 */
public class FailTask implements Runnable {
    private final Logger logger = Logger.getLogger(FailTask.class);
    public static final int COUNT = 3;


    private final Runnable task;
    private int count = 0;

    public int getCount() {
        return count;
    }

    public void count() {
        count++;
    }

    public FailTask(Runnable task) {
        this.task = task;
    }


    @Override
    public void run() {
        task.run();
    }
}
