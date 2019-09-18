package liusheng.main.app.bilibili;

import liusheng.main.app.bilibili.space.SpaceDownloader;

public class Main {
    public static void main(String[] args) throws Throwable {
        SpaceDownloader downloader = new SpaceDownloader("f:\\hello1");
        //https://space.bilibili.com/16095517/
        downloader.download("https://space.bilibili.com/449283594/video");
        //https://www.bilibili.com/bangumi/play/ss25734
       /* Types.completelyAnimalAll("https://www.bilibili.com/bangumi/play/ss25734", "f:\\hello");*/
    }
}
