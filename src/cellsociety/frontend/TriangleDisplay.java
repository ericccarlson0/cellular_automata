package cellsociety.frontend;

import javafx.scene.shape.Shape;

public class TriangleDisplay extends GridDisplay {
    public TriangleDisplay(int rowNum, int colNum, double totalWidth, double totalHeight) {
        super(rowNum, colNum, totalWidth, totalHeight);
    }

    public Shape createShape(double radius) {
        double[] triangleWH = calcTriangleWH(radius);
        double width = triangleWH[0];
        double height = triangleWH[1];
        Shape ret = renderTriangle(new double[]{width/2, height/2}, radius);
        return ret;
    }
}
