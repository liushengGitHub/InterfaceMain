package liusheng.main.app.manhuadui.listener;


import liusheng.main.app.manhuadui.manhuadui.entity.Image;

import java.util.List;

public interface DownloaderListener {
    void preParse(String url);
    void postParse(List<Image> images);
}
