package liusheng.main.userInter;

import javafx.scene.Node;
import javafx.scene.control.TableCell;

public interface CellAction<S,T> {
    void action(PreFlushTableCell<S,T> cell);
}
