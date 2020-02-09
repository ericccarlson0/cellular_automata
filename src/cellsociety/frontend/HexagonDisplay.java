package cellsociety.frontend;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class HexagonDisplay extends GridDisplay {
    public HexagonDisplay(int rowNum, int colNum, double totalWidth, double totalHeight) {
        super(rowNum, colNum, totalWidth, totalHeight);
    }

    public Shape createShape(double radius) {
        double[] hexagonWH = calcHexagonWH(radius);
        double width = hexagonWH[0];
        double height = hexagonWH[1];
        Shape ret = renderHexagon(new double[]{width/2, height/2}, radius);
        ret.setFill(Color.BLACK);
        return ret;
    }
}
