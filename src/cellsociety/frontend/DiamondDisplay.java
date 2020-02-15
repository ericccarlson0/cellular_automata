package cellsociety.frontend;

import cellsociety.Simulation;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class DiamondDisplay extends GridDisplay {
    /**
     * A descendant of GridDisplay, DiamondDisplay represents a geometric matrix of cells. It is a matrix of diamonds
     * whose successive rows (or columns, for that matter) are offset in order to fit as many as possible. These are
     * sized and stretched in order to fit the specified row- and column-numbers.
     */
    public DiamondDisplay(int rowNum, int colNum) {
        super(rowNum, colNum);
    }

    protected Shape createShape(int row, int col) {
        double unitHeight = Simulation.DISPLAY_HEIGHT/getNumRows() + 1;
        double unitWidth = Simulation.DISPLAY_WIDTH/getNumCols() + 1;
        double prefRatio = 1/2;
        double trueRatio = unitHeight/unitWidth;
        double ratioXY = prefRatio/trueRatio;

        double radius = unitWidth/2;
        double[] center = new double[]{unitWidth/2, unitHeight/2};

        PolygonConstructor pc = new PolygonConstructor();
        Double[] points = pc.calcPolygonPoints(center, radius, 4);
        Polygon shape = new Polygon();
        shape.getPoints().addAll(points);

        double offset;
        if ((row)%2 == 1) {
            offset = unitWidth/2;
        } else {
            offset = 0;
        }

        shape.setTranslateX(unitWidth*col + offset);
        shape.setTranslateY(unitHeight*row);
        // shape.setScaleY(ratioXY);
        return shape;
    }

    @Deprecated
    public void renderDisplay(Pane display, Shape[][] cellShapes, int rn, int cn,
                              double cx, double cy, double radius) {
        double cellWidth = 2*radius*Math.sqrt(2);
        double cellHeight = radius*Math.sqrt(2);
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
