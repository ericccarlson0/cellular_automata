package cellsociety.frontend;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class SquareDisplay extends GridDisplay {
    public SquareDisplay(int rowNum, int colNum, double totalWidth, double totalHeight) {
        super(rowNum, colNum, totalWidth, totalHeight);
    }

    public Shape createShape(double radius) {
        Shape ret = new Rectangle(radius*2, radius*2);
        ret.setFill(Color.BLACK);
        return ret;
    }
}
