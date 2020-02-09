package cellsociety.frontend;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class DiamondDisplay extends GridDisplay {
    public DiamondDisplay(int rowNum, int colNum, double radius) {
        super(rowNum, colNum, radius);
    }

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

    public Shape createShape(int row, int col) {
//        Shape ret = new Rectangle(radius*2, radius*2);
//        ret.setRotate(45);
//        ret.setFill(Color.BLACK);
        return null;
    }

    @Override
    public void addCellToDisplay(int row, int col, Object state) {

    }
}
