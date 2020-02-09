package cellsociety.frontend;

import cellsociety.Simulation;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

public abstract class GridDisplay {

    enum CellShape {
         SQUARE, DIAMOND, TRIANGLE, HEXAGON, CIRCLE
    }

    private Pane gridDisplay;
    private Shape[][] shapeHolder;
    private double totalWidth;
    private double totalHeight;
    private int numRows;
    private int numCols;
    private double cellRadius = 10; //***

    public GridDisplay(int numRows, int numCols, double radius) {
        this.cellRadius = radius;
        totalWidth = Simulation.DISPLAY_WIDTH;
        totalHeight = Simulation.DISPLAY_HEIGHT;
        this.numRows = numRows;
        this.numCols = numCols;
        gridDisplay = new Pane();
        gridDisplay.setPrefSize(totalWidth, totalHeight);
        shapeHolder = new Shape[numRows][numCols];
    }

    protected abstract Shape createShape(int row, int col);
    public abstract void addCellToDisplay(int row, int col, Object state);

//    public abstract void renderDisplay(Pane display, Shape[][] cellShapes, int rn, int cn,
//                                       double cx, double cy, double radius);
//    public void initializeDisplay() {
//        double centerX = totalWidth/2;
//        double centerY = totalHeight/2;
//        renderDisplay(gridDisplay, shapeHolder, rowNum, colNum, centerX, centerY, cellRadius); //***
//    }

    public void updateDisplayAtCell(int row, int col, Object stateAtCell) {
        Shape shape = shapeHolder[row][col];
        Simulation.AllStates state = (Simulation.AllStates) stateAtCell;
        shape.setFill(state.getColor());
    }

    public Pane getDisplay() { return gridDisplay; }

    protected double getCellRadius(){
        return cellRadius;
    }

    protected Shape[][] getShapeHolder(){
        return shapeHolder;
    }
}
