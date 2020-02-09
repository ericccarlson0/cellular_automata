package cellsociety.frontend;

import cellsociety.Simulation;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class SquareDisplay extends GridDisplay {
    public SquareDisplay(int rowNum, int colNum) {
        super(rowNum, colNum);
        setCellRadius((Simulation.DISPLAY_WIDTH/colNum)/2);
    }

    protected Shape createShape(int row, int col) {
        double height = Simulation.DISPLAY_HEIGHT/getNumRows();
        double width = Simulation.DISPLAY_WIDTH/getNumCols();

        Shape shape = new Rectangle(width, height);
        shape.setTranslateX((width + .1)*col);
        shape.setTranslateY((height + .1)*row);
        return shape;
    }

    @Deprecated
    public void renderDisplay(Pane display, Shape[][] cellShapes, int rn, int cn,
                              double cx, double cy, double radius) {
        double cellWidth = 2 * radius;
        double cellHeight = 2 * radius;
        double startingX = cx - rn * cellWidth / 2;
        double startingY = cy - cn * cellHeight / 2;
        for (int row = 0; row < rn; row++) {
            for (int col = 0; col < cn; col++) {
                Shape currShape = cellShapes[row][col];
                currShape.setTranslateX(startingX + cellWidth * row);
                currShape.setTranslateY(startingY + cellHeight * col);
                display.getChildren().add(currShape);
            }
        }
    }
}
