package liusheng.main.app.manhuadui.manhuadui.donwload;

import liusheng.main.app.bilibili.parser.Parser;
import liusheng.main.app.manhuadui.manhuadui.entity.Image;
import liusheng.main.app.manhuadui.manhuadui.parse.ChapterImageParser;
import liusheng.main.util.FileUtils;
import liusheng.main.util.ConnectionUtils;
import liusheng.main.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class ChapterDownloader {
    private final String dir ;
    private Parser<List<Image>> parser = new ChapterImageParser();
    public ChapterDownloader(String dir) {

        this.dir = dir;
    }
    public void download(String url ){
        if (! parser.check(url) ) throw  new IllegalArgumentException();

        String name = StringUtils.urlToFileName(url);
        int index = name.lastIndexOf(".");
        if (index != -1) name = name.substring(0,index);

        File dirFile  = new File(dir ,name);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        try {
            List<Image> images = parser.parse(url);

            System.out.println(url);

            IntStream.rangeClosed(1,images.size()).parallel().forEach( i->{
                String refer = url + "?p=" + i;
                try {
                    Image image = images.get(i - 1);
                    BufferedInputStream inputStream = ConnectionUtils.getConnection(image.getUrl()).header("Referer", refer).timeout(120000).maxBodySize(100 * 1024 * 1024)
                            .execute().bodyStream();

                    System.out.println(url + " == " + image.getUrl());

                    FileUtils.copy(inputStream,new FileOutputStream(new File(dirFile,image.getFileName())));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
