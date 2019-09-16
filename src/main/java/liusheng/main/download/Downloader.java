package liusheng.main.download;

import java.io.IOException;

public interface Downloader {

    void download(String url) throws IOException;

}
