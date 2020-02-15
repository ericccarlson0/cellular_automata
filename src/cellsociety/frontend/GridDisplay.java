package cellsociety.frontend;

import cellsociety.Simulation;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

/**
 * Abstract class representing the Grid Display frontend object that holds and handles the display of the backend GridStructure
 */
public abstract class GridDisplay {

    /**
     * enumerator to specify different CellShape types
     */
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

    /**
     * Constructor for GridDisplay class that initializes necessary variables based on specified values.
     * @param numRows int representing number of rows in the grid
     * @param numCols int representing number of cols in the grid
     */
    public GridDisplay(int numRows, int numCols) {
        totalWidth = Simulation.DISPLAY_WIDTH;
        totalHeight = Simulation.DISPLAY_HEIGHT;
        this.numRows = numRows;
        this.numCols = numCols;
        gridDisplay = new Pane();
        gridDisplay.setPrefSize(totalWidth, totalHeight);
        shapeHolder = new Shape[numRows][numCols];
    }

    /**
     * Creates a Shape that corresponds to cell in the backend that will be used to display on the frontend.
     * Abstract method that will be implemented differently depending on the type of display subclass.
     * @param row int representing row where new shape being created should be
     * @param col int representing col where new shape being created should be
     * @return Shape that is the new shape representation of corresponding cell that was just created
     */
    protected abstract Shape createShape(int row, int col);

    /**
     * Adds a cell to the display at a specific row and column with a provided state
     * @param row int representing row
     * @param col int representing col
     * @param state Object of Simulation.AllStates type that represents the current state of the cell that is being added to the display
     */
    public void addCellToDisplay(int row, int col, Object state) {
        Shape shape = createShape(row, col);
        Paint p = ((Simulation.AllStates) state).getColor();
        shape.setFill(p);
        gridDisplay.getChildren().add(shape);
        shapeHolder[row][col] = shape;
    }

    /**
     * Updates a cell in the display so that its color and visualization corresponds to the state of cell in the backend.
     * @param row int representing row
     * @param col int representing col
     * @param stateAtCell Object of Simulation.AllStates type that represents the current state of the cell that is being updated in Display
     */
    public void updateDisplayAtCell(int row, int col, Object stateAtCell) {
        Shape shape = shapeHolder[row][col];
        Simulation.AllStates state = (Simulation.AllStates) stateAtCell;
        shape.setFill(state.getColor());
    }

    /**
     * Allows access in simulation class to the physical javafx visualization that will be passed onto UI class.
     * @return Pane that holds the display that has all shapes corresponding to diff cells
     */
    public Pane getDisplay() { return gridDisplay; }

    /**
     * Allows access to the shapeHolder array in subclasses
     * @return Shape[][] that holds all shapes that make up grid visualization by their specified indices.
     */
    protected Shape[][] getShapeHolder(){
        return shapeHolder;
    }

    /**
     * allows access to the radius that all cells in the visualization use to be created.
     * @return double representing radius that cells are created with
     */
    protected double getCellRadius(){
        return cellRadius;
    }

    /**
     * allows access to number of rows in the grid for subclasses
     * @return double that is the number of rows in the grid
     */
    protected double getNumRows(){ return numRows; }

    /**
     * allows access to number of cols in the grid for subclasses
     * @return double that is the number of columns in the grid
     */
    protected double getNumCols(){ return numCols; }

    /**
     * sets the radius that is used to create all cells in this grid to passed in value
     * @param rad double representing radius
     */
    protected void setCellRadius(double rad){
        cellRadius = rad;
    }
}
