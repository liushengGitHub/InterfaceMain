package liusheng.main.app.bilibili.entity;

import java.io.File;
import java.util.concurrent.Phaser;

public class AbstractVideoBean {
    // 文件夹
    File dirFile;
    // 每 P url
    String url;
    // 文件名
    String name;
    private int size;

    public int getSize() {
        return size;
    }

    public File getDirFile() {
        return dirFile;
    }

    public void setDirFile(File dirFile) {
        this.dirFile = dirFile;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPageSize(int size) {

        this.size = size;
    }
}
