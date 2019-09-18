package liusheng.main.app.bilibili;

import javafx.scene.control.Button;

public class DownloadButton extends Button {
    private String avUrl;

    public DownloadButton(String text, String avUrl) {
        super(text);
        this.avUrl = avUrl;
    }

    public String getAvUrl() {
        return avUrl;
    }
}
