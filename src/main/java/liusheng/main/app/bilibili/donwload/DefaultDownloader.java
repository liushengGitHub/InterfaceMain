package liusheng.main.app.bilibili.donwload;

/**
 * 只能在一个线程调用
 */
public class DefaultDownloader implements RetryDownloader.DownloaderController {

    private volatile boolean cancel = false;
    private volatile boolean pause = false;


    public void cancel() {
        cancel = true;
    }

    public void pauseOrStart() {
        pause = !pause;
    }

    @Override
    public boolean isCancel() {
        return cancel;
    }

    @Override
    public boolean isPause() {
        return pause;
    }
}
