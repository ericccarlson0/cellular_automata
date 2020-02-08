package cellsociety;

import java.util.ArrayList;
import java.util.Collections;

public abstract class GridStructure {

    // public static final double CELL_GAP = 0;
    // public static final int DISPLAY_WIDTH = 500;
    // public static final int DISPLAY_HEIGHT = 500;

    protected ArrayList<Cell> cellList;

    private Cell[][] gridStructure;
    private int rowNum;
    private int colNum;
    private double cellRadius;
    private ArrayList<Double> statePercents;
    private ArrayList<String> states;
    private int neighborhoodType;

    public GridStructure(int nowNum, int colNum, ArrayList<Double> percents, ArrayList<String> states,
                         double radius,  String shape, int neighborhoodType) {
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.cellRadius = radius;
        this.states = states;
        this.statePercents = percents;
        this.neighborhoodType = neighborhoodType;
    }

    protected abstract void calcNewStates();

    protected Cell makeCell(String shape) {
        return new Cell(cellRadius, null, shape);
    }

    private void initPercents() {
        for (int index = 1; index < statePercents.size(); index++){
            statePercents.set(index, statePercents.get(index) + statePercents.get(index - 1));
        }
    }

    private void initGridStructure(String shape) {
        gridStructure = new Cell[rowNum][colNum];
        cellList = new ArrayList<>();
        createCells(shape);
        initCellNeighbors(neighborhoodType);
    }

    protected void init(String shape) { // Does this do any work?
        initPercents();
        initGridStructure(shape);
    }

    public void step() {
        Collections.shuffle(cellList);
        calcNewStates();
        updateCellStates();
    }

    private void createCells(String shape){ //***
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < rowNum; col++) {
                Cell curr = makeCell(shape);
                gridStructure[row][col] = curr;
                cellList.add(curr);
                // updateColor(curr); ***
            }
        }
    }

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

    private void initCellNeighbors(int numNeighbors) {
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < rowNum; col++) {
                ArrayList<Cell> neighbors = new ArrayList<>();
                switch(numNeighbors){
                    //TODO: make different configurations of neighbors
                    case 2:
                        break;
                    case 4:
                        break;
                    case 8:
                        neighbors = getNeighborsEight(row, col);
                        break;
                }
                removeNulls(neighbors);
                for (Cell neighbor: neighbors) {
                    gridStructure[row][col].addNeighbor(neighbor);
                }
            }
        }
    }

    private void removeNulls(ArrayList<Cell> neighbors) {
        // Found at:
        // https://stackoverflow.com/questions/4819635/how-to-remove-all-null-elements-from-a-arraylist-or-string-array
        neighbors.removeAll(Collections.singleton(null));
    }

    private ArrayList<Cell> getNeighborsEight(int row, int col) {
        ArrayList<Cell> neighbors = new ArrayList<>();
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

    private Cell isValidCoords(int row, int col) {
        boolean rowValid = (row >= 0) && (row < rowNum);
        boolean colValid = (col >= 0) && (col < rowNum);
        if (rowValid && colValid){
            return gridStructure[row][col];
        }
        return null;
    }

    private void updateCellStates(){
        for(Cell c : cellList){
            c.updateState();
            // updateColor(c);
        }
    }

    public Cell getCellAtIndex(int row, int col){
        return gridStructure[row][col];
    }

    public int getRowNum() { return rowNum; }

    public int getColNum() { return colNum; }
}
