package cellsociety.frontend;

import cellsociety.Simulation;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class CircleDisplay extends GridDisplay {
    /**
     * A descendant of GridDisplay, CircleDisplay represents a geometric matrix of cells. It is a matrix of circles
     * whose successive rows (or columns, for that matter) are offset (as would be the case in a matrix of diamonds).
     * These are sized and stretched in order to fit the specified row- and column-numbers.
     */
    public CircleDisplay(int rowNum, int colNum) {
        super(rowNum, colNum);
    }

    /**
     *
     * @param row int representing row where new shape being created should be
     * @param col int representing col where new shape being created should be
     * @return
     */
    protected Shape createShape(int row, int col) {
        double unitHeight = Simulation.DISPLAY_HEIGHT/getNumRows() + 1;
        double unitWidth = Simulation.DISPLAY_WIDTH/getNumCols() + 1;
        double radiusX = unitWidth/2;
        double radiusY = unitHeight/2;

        double offset;
        if ((row)%2 == 1) {
            offset = unitWidth/2;
        } else {
            offset = 0;
        }

        Shape shape = new Ellipse(radiusX, radiusY);
        shape.setTranslateX(unitWidth*col + offset);
        shape.setTranslateY(unitHeight*row);
        return shape;
    }

    @Deprecated
    public void renderDisplay(Pane display, Shape[][] cellShapes, int rn, int cn,
                              double cx, double cy, double radius) {
        double cellWidth = 2*radius;
        double cellHeight = 2*radius;
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
