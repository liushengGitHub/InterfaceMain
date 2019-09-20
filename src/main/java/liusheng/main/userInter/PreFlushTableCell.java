package liusheng.main.userInter;

import javafx.scene.Node;
import javafx.scene.control.TableCell;

import java.util.HashMap;
import java.util.Map;

public class PreFlushTableCell<S, T> extends TableCell<S, T> {
    //观察模式
    private final CellAction<S, T> cellAction;
    private final Map<Integer,Node> nodeMap = new HashMap<>();

    public Map<Integer, Node> getNodeMap() {
        return nodeMap;
    }

    public PreFlushTableCell(CellAction<S, T> cellAction) {
        this.cellAction = cellAction;
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty) {
            cellAction.action(this);
        }
    }
}
