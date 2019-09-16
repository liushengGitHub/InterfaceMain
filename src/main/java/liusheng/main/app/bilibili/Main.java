package liusheng.main.app.bilibili;

import liusheng.main.app.bilibili.space.SpaceDownloader;

public class Main {
    public static void main(String[] args) throws Throwable {
        SpaceDownloader downloader = new SpaceDownloader("f:\\hello");
        //https://space.bilibili.com/16095517/
        downloader.download("https://space.bilibili.com/16095517/video");
    }
}
