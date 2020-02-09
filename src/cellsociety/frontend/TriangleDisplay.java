package cellsociety.frontend;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class TriangleDisplay extends GridDisplay {
    public TriangleDisplay(int rowNum, int colNum, double radius) {
        super(rowNum, colNum, radius);
    }

    public Shape createShape(int row, int col) {
//        PolygonConstructor pc = new PolygonConstructor();
//        double[] triangleWH = pc.calcTriangleWH(radius);
//        double width = triangleWH[0];
//        double height = triangleWH[1];
//        Shape ret = renderTriangle(new double[]{width/2, height/2}, radius);
//        return ret;
        return null;
    }

    @Override
    public void addCellToDisplay(int row, int col, Object state) {

    }

    public void renderDisplay(Pane display, Shape[][] cellShapes, int rn, int cn,
                              double cx, double cy, double radius) {
        double side = 2*radius*Math.sin(Math.PI/3);
        double cellWidth = side/2;
        double cellHeight = side*Math.sqrt(3)/2;
        double startingX = cx - rn*cellWidth/2;
        double startingY = cy - cn*cellHeight/2;
        for (int row=0; row<rn; row++) {
            for (int col=0; col<cn; col++) {
                Shape currShape = cellShapes[row][col];
                currShape.setTranslateX(startingX + cellWidth*row);
                currShape.setTranslateY(startingY + cellHeight*col);
                if ((rn+cn)%2 == 0) {
                    currShape.setRotate(90);
                }
                display.getChildren().add(currShape);
            }
        }
    }

    public Polygon renderTriangle(double[] center, double radius) {
        PolygonConstructor pc = new PolygonConstructor();
        Polygon ret = new Polygon();
        Double[] points = pc.calcTrianglePoints(center, radius);
        ret.getPoints().addAll(points);
        return ret;
    }
}
