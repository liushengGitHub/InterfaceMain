package liusheng.main.app.bilibili;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.IoUtil;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

public class SearchItem {
    private String href;
    private String title;
    private String imgSrc;
    private Image image;
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) throws MalformedURLException {
        this.imgSrc = imgSrc;
    }

    public SearchItem() {
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SearchItem(String href, String title, String imgSrc,String author) {

        this.href = href;
        this.title = title;
        this.imgSrc = imgSrc /*+ "@100w_100h.webp"*/;
        this.author = author;
        try {
           /* BufferedImage bufferedImage = ImgUtil.toImage(IoUtil.readBytes(new URL(  this.imgSrc).openStream()));
            image  = SwingFXUtils.toFXImage(bufferedImage, new WritableImage(100, 100));*/
           image = new Image(imgSrc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
