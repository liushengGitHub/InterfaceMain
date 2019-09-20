package liusheng.main.app.manhuadui.manhuadui.donwload;


import liusheng.main.app.manhuadui.manhuadui.entity.Cartoon;
import liusheng.main.app.manhuadui.manhuadui.parse.CartoonParser;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.*;

public class CartoonDonwloader {
    private String dir;
    private CartoonParser cartoonParser;
    private ExecutorService executorService = new DownloadExecutorService(Runtime.getRuntime().availableProcessors() * 2);

    static class DownloadExecutorService extends ThreadPoolExecutor {
        public DownloadExecutorService(int size) {
            super(size, size, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            this.allowCoreThreadTimeOut(true);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {

        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {

        }
    }

    public CartoonDonwloader(String dir) {
        this.dir = dir;
        this.cartoonParser = new CartoonParser();
    }

    public void download(String url) {

        if (!cartoonParser.check(url)) {
            throw new IllegalArgumentException();
        }

        try {
            Cartoon cartoon = cartoonParser.parse(url);

            File d1 = new File(dir, cartoon.getName());

            if (!d1.exists()) {
                d1.mkdirs();
            }
            cartoon.getVersions().forEach(version -> {
                File d2 = new File(d1, version.getVersion());

                if (!d2.exists()) {
                    d2.mkdirs();
                }
                version.getChapters().forEach(chapter -> {
                    File d3 = new File(d2, version.getVersion());

                    if (!d3.exists()) {
                        d3.mkdirs();
                    }

                    executorService.execute(()->{
                        try {
                            new ChapterDownloader(d3.getAbsolutePath()).download(chapter.getChapterUrl());
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                });
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {

        }

    }
}
