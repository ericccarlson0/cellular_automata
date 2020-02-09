package cellsociety.frontend;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class HexagonDisplay extends GridDisplay {
    public HexagonDisplay(int rowNum, int colNum, double radius) {
        super(rowNum, colNum, radius);
    }

    private Polygon renderHexagon(double[] center, double radius) { //*** How to determine the center?
        PolygonConstructor pc = new PolygonConstructor();
        Polygon ret = new Polygon();
        Double[] points = pc.calcPolygonPoints(center, radius, 6);
        ret.getPoints().addAll(points);
        return ret;
    }

    public void renderDisplay(Pane display, Shape[][] cellShapes, int rn, int cn,
                              double cx, double cy, double radius) {
        double apothem = radius*Math.cos(Math.PI/6);
        double side = 2*radius*Math.sin(Math.PI/6);
        double cellWidth = 2*radius+side;
        double cellHeight = radius;
        double startingX = cx - rn*cellWidth/2;
        double startingY = cy - cn*cellHeight/2;
        for (int row=0; row<rn; row++) {
            if (row%2 == 1) {
                startingX += cellWidth/2; // Offset
            }
            for (int col=0; col<cn; col++) {
                Shape currShape = cellShapes[row][col];
                currShape.setTranslateX(startingX + cellWidth*row);
                currShape.setTranslateY(startingY + cellHeight*col);
                display.getChildren().add(currShape);
            }
            if (row%2 == 1) {
                startingX -= cellWidth/2; // Remove offset
            }
        }
    }

    public Shape createShape(int row, int col) {
//        PolygonConstructor pc = new PolygonConstructor();
//        double[] hexagonWH = pc.calcHexagonWH(radius);
//        double width = hexagonWH[0];
//        double height = hexagonWH[1];
//        Shape ret = renderHexagon(new double[]{width/2, height/2}, radius);
//        ret.setFill(Color.BLACK);
//        return ret;
        return null;
    }

    @Override
    public void addCellToDisplay(int row, int col, Object state) {

    }
}
