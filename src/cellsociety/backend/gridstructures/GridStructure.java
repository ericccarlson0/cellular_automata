package cellsociety.backend.gridstructures;

import cellsociety.backend.Cell;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract class that represents and holds all necessary information about the GridStructure and backend half of the program.
 * This class is extended when each new simulation type is added.
 */
public abstract class GridStructure {
    protected List<Cell> cellList;
    private Cell[][] gridStructure;
    private int rowNum;
    private int colNum;
    private List<Double> statePercents;
    private List<String> states;
    private int neighborhoodType;
    private boolean isTorus;

    /**
     * Constructor that creates a new GridStructure object based on passed in values that are obtained from UI or XML file
     * @param rowNum int number of rows this simulation grid should have
     * @param colNum int number of columns this simulation grid should have
     * @param percents List of doubles that represent the percents of each type of state
     * @param states List of Strings that represent the states, which correspond by index to the percents
     * @param neighborhoodType int that represents which neighborhood type should be created for this simulation
     * @param isTorus boolean which specifies if simulation is a torus simulation
     */
    public GridStructure(int rowNum, int colNum, List<Double> percents, List<String> states, int neighborhoodType, boolean isTorus) {
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.states = states;
        this.statePercents = percents;
        this.neighborhoodType = neighborhoodType;
        this.isTorus = isTorus;
    }

    /**
     * abstract method to calculate new cell states for all cells that must be implemented by all grid structure subclasses.
     * each simulation type will implement differently based on simulation type.
     */
    protected abstract void calcNewStates();

    /**
     * abstract method used to create new cells that must be implemented by all grid structure subclasses
     * @param row int row number for where the cell being created is in the grid
     * @param col int col number for where the cell being created is in the grid
     * @return Cell object that is created according to simulation type
     */
    protected abstract Cell createCell(int row, int col);

    private void initPercents() {
        for (int index = 1; index < statePercents.size(); index++){
            statePercents.set(index, statePercents.get(index) + statePercents.get(index - 1));
        }
    }

    private void initGridStructure() {
        gridStructure = new Cell[rowNum][colNum];
        cellList = new ArrayList<>();
        createCells();
        initCellNeighbors(neighborhoodType);
    }

    /**
     * called by constructors of subclasses to initializePercents and the gridStructure after super() has been called and other possibly needed values have been set by subclass constructors.
     */
    protected void init() {
        initPercents();
        initGridStructure();
    }

    /**
     * called by simulation to step the backend.
     * First, shuffles all cells to a random order.
     * Then calculates the new state of each and lastly, updates for each cell.
     */
    public void step() {
        Collections.shuffle(cellList);
        calcNewStates();
        updateCellStates();
    }

    private void createCells() {
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                Cell curr = createCell(row, col);
                gridStructure[row][col] = curr;
                cellList.add(curr);
            }
        }
    }

    /**
     * Selects a random state to assign as initial state for each cell and is called by the createCell() method in subclass.
     * @return String that represents the randomly selected state type based on percents passed into constructor
     */
    protected String generateState() {
        double val = Math.random() * 100;
        int index = 0;
        while (index < statePercents.size()){
            if (val < statePercents.get(index)) {
                return states.get(index);
            }
            index++;
        }
        return states.get(-1);
    }

    private void initCellNeighbors(int neighborhoodType) {
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                List<Cell> neighbors = new ArrayList<>();
                switch(neighborhoodType){
                    case 3:
                        neighbors = getNeighborsThree(row,col);
                        break;
                    case 4:
                        neighbors = getNeighborsFour(row,col);
                        break;
                    case 6:
                        neighbors = getNeighborsSix(row,col);
                        break;
                    case 8:
                        neighbors = getNeighborsEight(row, col);
                        break;
                    case 9:
                        neighbors = getNeighborsNine(row,col);
                        break;
                    case 12:
                        neighbors = getNeighborsTwelve(row,col);
                        break;
                }
                removeNulls(neighbors);
                gridStructure[row][col].setNeighbors(neighbors); //***
            }
        }
    }

    private List<Cell> getNeighborsThree(int row, int col){
        List<Cell> neighbors = new ArrayList<>();
        neighbors.add(isValidCoords(row,col - 1));
        neighbors.add(isValidCoords(row,col+1));
        boolean pointedUp = ((row+col)%2 == 1);
        if(pointedUp)
            neighbors.add(isValidCoords(row + 1, col));
        else
            neighbors.add(isValidCoords(row - 1, col));
        return neighbors;
    }

    private List<Cell> getNeighborsFour(int row, int col){
        List<Cell> neighbors = new ArrayList<>();
        neighbors.add(isValidCoords(row - 1, col));
        neighbors.add(isValidCoords(row, col+1));
        neighbors.add(isValidCoords(row+1,col));
        neighbors.add(isValidCoords(row, col-1));
        return neighbors;
    }

    private List<Cell> getNeighborsSix(int row, int col){
        List<Cell> neighbors = new ArrayList<>();
        neighbors.add(isValidCoords(row - 2, col));
        neighbors.add(isValidCoords(row + 2,col));
        boolean off = (row%2 == 0);
        if(off){
            neighbors.add(isValidCoords(row - 1,col));
            neighbors.add(isValidCoords(row - 1,col - 1));
            neighbors.add(isValidCoords(row + 1,col - 1));
            neighbors.add(isValidCoords(row + 1,col));
        }
        else{
            neighbors.add(isValidCoords(row - 1,col));
            neighbors.add(isValidCoords(row - 1,col + 1));
            neighbors.add(isValidCoords(row + 1,col + 1));
            neighbors.add(isValidCoords(row + 1,col));
        }
        return neighbors;
    }

    private List<Cell> getNeighborsEight(int row, int col) {
        List<Cell> neighbors = new ArrayList<>();
        neighbors.add(isValidCoords(row - 1, col - 1));
        neighbors.add(isValidCoords(row - 1,col));
        neighbors.add(isValidCoords(row - 1,col + 1));
        neighbors.add(isValidCoords(row,col + 1));
        neighbors.add(isValidCoords(row + 1,col + 1));
        neighbors.add(isValidCoords(row + 1,col));
        neighbors.add(isValidCoords(row + 1,col - 1));
        neighbors.add(isValidCoords(row,col - 1));
        return neighbors;
    }

    private List<Cell> getNeighborsNine(int row, int col){
        List<Cell> neighbors = new ArrayList<>();
        neighbors.add(isValidCoords(row,col + 2));
        neighbors.add(isValidCoords(row,col - 2));
        boolean pointedUp = ((row+col)%2 == 1);
        if(pointedUp){
            neighbors.add(isValidCoords(row - 1, col));
            neighbors.add(isValidCoords(row - 1, col+1));
            neighbors.add(isValidCoords(row - 1, col-1));
            neighbors.add(isValidCoords(row + 1, col+1));
            neighbors.add(isValidCoords(row + 1, col+2));
            neighbors.add(isValidCoords(row + 1, col-1));
            neighbors.add(isValidCoords(row + 1, col-2));
        }
        else{
            neighbors.add(isValidCoords(row + 1, col));
            neighbors.add(isValidCoords(row + 1, col+1));
            neighbors.add(isValidCoords(row + 1, col-1));
            neighbors.add(isValidCoords(row - 1, col+1));
            neighbors.add(isValidCoords(row - 1, col+2));
            neighbors.add(isValidCoords(row - 1, col-1));
            neighbors.add(isValidCoords(row - 1, col-2));
        }
        return neighbors;
    }

    private List<Cell> getNeighborsTwelve(int row, int col){
        List<Cell> neighbors = new ArrayList<>();
        List<Cell> fromThree = getNeighborsThree(row,col);
        List<Cell> fromNine = getNeighborsNine(row,col);
        for(Cell c: fromThree){
            neighbors.add(c);
        }
        for(Cell c: fromNine){
            neighbors.add(c);
        }
        return neighbors;
    }

    private void removeNulls(List<Cell> neighbors) {
        // Found at:
        // https://stackoverflow.com/questions/4819635/how-to-remove-all-null-elements-from-a-arraylist-or-string-array
        neighbors.removeAll(Collections.singleton(null));
    }

    private Cell isValidCoords(int row, int col) {
        boolean rowValid = (row >= 0) && (row < rowNum);
        boolean colValid = (col >= 0) && (col < colNum);
        if (rowValid && colValid){
            return gridStructure[row][col];
        }
        else if(isTorus){
            return getTorusCoords(row,col);
        }
        return null;
    }

    private Cell getTorusCoords(int row, int col){
        if(row < 0)
            row = rowNum - 1;
        if(col < 0)
            col = colNum - 1;
        if(col >= colNum)
            col = 0;
        if(row >= rowNum)
            row = 0;
        return gridStructure[row][col];
    }

    private void updateCellStates() {
        for (Cell cell : cellList) {
            cell.updateState();
        }
    }

    /**
     * Allows access to a Cell object stored at a specific location in the Grid
     * @param row int specifying row
     * @param col int specifying col
     * @return Cell at specified row col values
     */
    public Cell getCellAtIndex(int row, int col) {
        return gridStructure[row][col];
    }

    /**
     * Allows access to the current state of a cell at a specific location in the grid structure.
     * @param row int specifying row
     * @param col int specifying col
     * @return Object representing a Simulation.AllStates that is currentState of the specified cell
     */
    public Object getStateAtCell(int row, int col) {
        return gridStructure[row][col].getCurrState();
    }

    /**
     * Allows access to the number of rows in the grid structure.
     * @return int representing number of rows in grid
     */
    public int getRowNum() { return rowNum; }

    /**
     * Allows access to the number of columns in the grid structure
     * @return int representing number of cols in grid
     */
    public int getColNum() { return colNum; }
}
