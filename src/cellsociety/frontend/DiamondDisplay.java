package cellsociety.frontend;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class DiamondDisplay extends GridDisplay {
    public DiamondDisplay(int rowNum, int colNum, double totalWidth, double totalHeight) {
        super(rowNum, colNum, totalWidth, totalHeight);
    }

    @Override
    public Shape createShape(double radius) {
        Shape ret = new Rectangle(radius*2, radius*2);
        ret.setRotate(45);
        ret.setFill(Color.BLACK);
        return ret;
    }
}
