package liusheng.main.app.bilibili.listener;

import liusheng.main.app.bilibili.donwload.RetryDownloader;

import java.util.concurrent.atomic.AtomicLong;

public class DownloadSpeedListener implements RetryDownloader.DownloaderListener {

        private AtomicLong size = new AtomicLong();

        @Override
        public void listen(int length) {
            size.getAndAdd(length);
           // System.out.println(getSize());
        }

       public long getSize() {
            return size.get();
        }
    }