package cellsociety.frontend;

import cellsociety.Simulation;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class HexagonDisplay extends GridDisplay {
    /**
     * A descendant of GridDisplay, HexagonDisplay represents a geometric matrix of cells. It is a matrix of hexagons
     * organized as is typical, with six neighbors to each cell. These are sized and stretched in order to fit the
     * specified row- and column-numbers. Note: this results in around 2X vertical stretching when the row- and column-
     * numbers are the same due to the fact that successive rows interlock so closely.
     */
    public HexagonDisplay(int rowNum, int colNum) {
        super(rowNum, colNum);
    }

    protected Shape createShape(int row, int col) {
        double unitHeight =  Simulation.DISPLAY_HEIGHT/getNumRows() + 1;
        double unitWidth =   Simulation.DISPLAY_WIDTH/getNumCols() + 1;
        double prefRatio = Math.cos(Math.PI/6) / (2 + 2*Math.sin(Math.PI/6));
        double trueRatio = unitHeight/unitWidth;
        double ratioXY = prefRatio/trueRatio;

        double radius = unitWidth / (2 + 2*Math.sin(Math.PI/6));
        double[] center = new double[]{unitWidth/2, unitHeight/2};

        PolygonConstructor pc = new PolygonConstructor();
        Double[] points = pc.calcPolygonPoints(center, radius, 6);
        Polygon shape = new Polygon();
        shape.getPoints().addAll(points);

        double offset;
        if ((row)%2 == 1) {
            offset = unitWidth/2;
        } else {
            offset = 0;
        }

        shape.setTranslateX((unitWidth + .1)*col - unitWidth/2 + offset);
        shape.setTranslateY((unitHeight + .1)*row - unitHeight/2);
        shape.setScaleY(1/ratioXY);
        return shape;
    }

    @Deprecated
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
}
