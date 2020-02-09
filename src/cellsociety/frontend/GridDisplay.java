package cellsociety.frontend;

import cellsociety.Simulation;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
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
    private double cellRadius = 10;

    public GridDisplay(int numRows, int numCols) {
        totalWidth = Simulation.DISPLAY_WIDTH;
        totalHeight = Simulation.DISPLAY_HEIGHT;
        this.numRows = numRows;
        this.numCols = numCols;
        gridDisplay = new Pane();
        gridDisplay.setPrefSize(totalWidth, totalHeight);
        shapeHolder = new Shape[numRows][numCols];
    }

    protected abstract Shape createShape(int row, int col);

    public void addCellToDisplay(int row, int col, Object state) {
        Shape shape = createShape(row, col);
        Paint p = ((Simulation.AllStates) state).getColor();
        shape.setFill(p);
        gridDisplay.getChildren().add(shape);
        shapeHolder[row][col] = shape;
    }

    public void updateDisplayAtCell(int row, int col, Object stateAtCell) {
        Shape shape = shapeHolder[row][col];
        Simulation.AllStates state = (Simulation.AllStates) stateAtCell;
        shape.setFill(state.getColor());
    }

    public Pane getDisplay() { return gridDisplay; }
    protected Shape[][] getShapeHolder(){
        return shapeHolder;
    }
    protected double getCellRadius(){
        return cellRadius;
    }
    protected double getNumRows(){ return numRows; }
    protected double getNumCols(){ return numCols; }
    protected void setCellRadius(double rad){
        cellRadius = rad;
    }
}
