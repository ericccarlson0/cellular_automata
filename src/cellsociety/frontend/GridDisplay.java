package cellsociety.frontend;

import cellsociety.Simulation;
import cellsociety.backend.Cell;
import cellsociety.backend.gridstructures.LifeGrid;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Color;

public class GridDisplay {
    public static final double CELL_GAP = .1;
    public static final int DISPLAY_WIDTH = 500;
    public static final int DISPLAY_HEIGHT = 500;

    private GridPane gridDisplay;
    private Shape[][] shapeHolder;
    private CellShape cellShape;
//    private Cell[][] cellObjects;
//    private Shape[][] cellShapes;
//    private double totalWidth;
//    private double totalHeight;
//    private double cellWidth;
//    private double cellHeight;
    private double size;

    enum CellShape {
        SQUARE, DIAMOND, TRIANGLE, HEXAGON, CIRCLE
    }

    public GridDisplay(String shapeString, int size) {
        this.cellShape = CellShape.valueOf(shapeString); //***
        this.size = size;
        shapeHolder = new Shape[size][size];
        gridDisplay = new GridPane();
    }


    public void addCellToDisplay(int row, int col, Object state){
        switch(cellShape){
            case SQUARE:
                addRectToDisplay(row, col, state);
                break;
        }
    }

    private void addRectToDisplay(int row, int col, Object state) {
        double cellWidth = DISPLAY_WIDTH / size - 2*CELL_GAP;
        double cellHeight = DISPLAY_HEIGHT / size - 2*CELL_GAP;
        Shape s = new Rectangle(cellWidth, cellHeight);
        Paint p = ((Simulation.AllStates) state).getColor();
        s.setFill(p);
        gridDisplay.add(s, col, row,1,1);
        shapeHolder[row][col] = s;
    }

    private void initSquareCellDisplay() {

    }

    private void initDiamondCellDisplay() {

    }

    private void initTriCellDisplay() {

    }

    private void initHexCellDisplay() {

    }

    private void initCircleCellDisplay() {

    }

    public Region getDisplay(){
        return gridDisplay;
    }

    public void updateDisplayAtCell(int row, int col, Object stateAtCell) {
        Simulation.AllStates state =( Simulation.AllStates) stateAtCell;
        shapeHolder[row][col].setFill(state.getColor());
    }
}
