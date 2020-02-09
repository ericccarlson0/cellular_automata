package cellsociety.frontend;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

public class CircleDisplay extends GridDisplay {
    public CircleDisplay(int rowNum, int colNum, double totalWidth, double totalHeight) {
        super(rowNum, colNum, totalWidth, totalHeight);
    }

    @Override
    public Shape createShape(double radius) {
        Shape ret = new Ellipse(radius, radius);
        ret.setFill(Color.BLACK);
        return ret;
    }
}
