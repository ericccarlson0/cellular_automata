package cellsociety.frontend;

import cellsociety.Simulation;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class SquareDisplay extends GridDisplay {
    public SquareDisplay(int rowNum, int colNum, double radius) {
        super(rowNum, colNum, radius);
    }

    public void renderDisplay(Pane display, Shape[][] cellShapes, int rn, int cn,
                              double cx, double cy, double radius) {
        double cellWidth = 2*radius;
        double cellHeight = 2*radius;
        double startingX = cx - rn*cellWidth/2;
        double startingY = cy - cn*cellHeight/2;
        for (int row=0; row<rn; row++) {
            for (int col=0; col<cn; col++) {
                Shape currShape = cellShapes[row][col];
                currShape.setTranslateX(startingX + cellWidth*row);
                currShape.setTranslateY(startingY + cellHeight*col);
                display.getChildren().add(currShape);
            }
        }
    }

    @Override
    protected Shape createShape(int row, int col) {
        double radius = getCellRadius();
        Shape shape = new Rectangle(radius*2, radius*2);
        shape.setTranslateX(2*radius*col); //
        shape.setTranslateY(2*radius*row); //
        return shape;
    }

    @Override
    public void addCellToDisplay(int row, int col, Object state) {
        Pane display = getDisplay();
        Shape[][] holder = getShapeHolder();
        double cx = Simulation.DISPLAY_WIDTH/2;
        double cy = Simulation.DISPLAY_WIDTH/2;
        Shape shape = createShape(row, col);
        Paint p = ((Simulation.AllStates) state).getColor();
        shape.setFill(p);
        display.getChildren().add(shape);
        holder[row][col] = shape;
    }
}
