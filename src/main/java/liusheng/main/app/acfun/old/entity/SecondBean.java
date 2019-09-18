package liusheng.main.app.acfun.old.entity;

import java.nio.file.Path;

/**
 *
 * 第二个m3u8 文件
 */
public class SecondBean {
    // 前缀
    // 第二个m3u8文件的url
    private String url;
    // 分辨率
    private String fbl;
    private String fileName;
    /**
     * host-name : hb2-acfun-kcs070.aliyun
     */

    private Path dirPath;

    public SecondBean(String url, String fbl, String fileName, Path dirPath) {
        this.url = url;
        this.fbl = fbl;
        this.fileName = fileName;
        this.dirPath = dirPath;
    }

    public Path getDirPath() {
        return dirPath;
    }

    public void setDirPath(Path dirPath) {
        this.dirPath = dirPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFbl() {
        return fbl;
    }

    public void setFbl(String fbl) {
        this.fbl = fbl;
    }

}
