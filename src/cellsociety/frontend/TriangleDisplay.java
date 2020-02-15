package cellsociety.frontend;

import cellsociety.Simulation;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class TriangleDisplay extends GridDisplay {
    /**
     * A descendant of GridDisplay, TriangleDisplay represents a geometric matrix of cells. It is a matrix of triangles
     * organized as is typical, with each triangle rotated upside-down compared to the neighbors in its row. These are
     * sized and stretched in order to fit the specified row- and column-numbers.
     */
    public TriangleDisplay(int rowNum, int colNum) {
        super(rowNum, colNum);
    }

    protected Shape createShape(int row, int col) {
        double unitHeight = Simulation.DISPLAY_HEIGHT/getNumRows() + 1;
        double unitWidth = Simulation.DISPLAY_WIDTH/getNumCols() + 1;
        double prefRatio = Math.sqrt(3);
        double trueRatio = unitHeight/unitWidth;
        double ratioXY = prefRatio/trueRatio;

        double radius = unitWidth / Math.sin(Math.PI/2);
        double[] center = new double[]{unitWidth/2, unitHeight/2};

        PolygonConstructor pc = new PolygonConstructor();
        Double[] points = pc.calcTrianglePoints(center, radius);
        Polygon shape = new Polygon();
        shape.getPoints().addAll(points);

        shape.setTranslateX((unitWidth + .1)*col - unitWidth/2);
        shape.setTranslateY((unitHeight + .1)*row - unitHeight/2);
        if ((row+col)%2 == 1) { //***
            shape.setRotate(180);
        }
        shape.setScaleY(1/ratioXY);
        return shape;
    }

    @Deprecated
    public void renderDisplay(Pane display, Shape[][] cellShapes, int rn, int cn,
                              double cx, double cy, double radius) {
        double side = 2 * radius * Math.sin(Math.PI / 3);
        double cellWidth = side / 2;
        double cellHeight = side * Math.sqrt(3) / 2;
        double startingX = cx - rn * cellWidth / 2;
        double startingY = cy - cn * cellHeight / 2;
        for (int row = 0; row < rn; row++) {
            for (int col = 0; col < cn; col++) {
                Shape currShape = cellShapes[row][col];
                currShape.setTranslateX(startingX + cellWidth * row);
                currShape.setTranslateY(startingY + cellHeight * col);
                if ((rn + cn) % 2 == 0) {
                    currShape.setRotate(90);
                }
                display.getChildren().add(currShape);
            }
        }
    }
}
