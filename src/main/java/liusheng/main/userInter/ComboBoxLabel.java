package liusheng.main.userInter;

import javafx.scene.control.Label;
import liusheng.main.userInter.entity.ComboBoxEntity;

public class ComboBoxLabel extends Label {
    private final String urlPatten;

    public ComboBoxLabel(ComboBoxEntity comboBoxEntity) {
        super(comboBoxEntity.getLabelName());
        this.urlPatten = comboBoxEntity.getPattern();
    }


    public String getUrlPatten() {
        return urlPatten;
    }

}
