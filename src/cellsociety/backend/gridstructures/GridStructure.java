package cellsociety.backend.gridstructures;

import cellsociety.backend.Cell;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GridStructure {
    protected List<Cell> cellList;
    private Cell[][] gridStructure;
    private int rowNum;
    private int colNum;
    private List<Double> statePercents;
    private List<String> states;
    private int neighborhoodType;

    public GridStructure(int rowNum, int colNum, List<Double> percents, List<String> states, int neighborhoodType) {
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.states = states;
        this.statePercents = percents;
        this.neighborhoodType = neighborhoodType;
    }

    protected abstract void calcNewStates();
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

    protected void init() {
        initPercents();
        initGridStructure();
    }

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
                gridStructure[row][col].setNeighbors(neighbors); //***
            }
        }
    }

    private void removeNulls(List<Cell> neighbors) {
        // Found at:
        // https://stackoverflow.com/questions/4819635/how-to-remove-all-null-elements-from-a-arraylist-or-string-array
        neighbors.removeAll(Collections.singleton(null));
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

    private Cell isValidCoords(int row, int col) {
        boolean rowValid = (row >= 0) && (row < rowNum);
        boolean colValid = (col >= 0) && (col < colNum);
        if (rowValid && colValid){
            return gridStructure[row][col];
        }
        return null;
    }

    private void updateCellStates() {
        for (Cell cell : cellList) {
            cell.updateState();
        }
    }

    public Cell getCellAtIndex(int row, int col) {
        return gridStructure[row][col];
    }

    public Object getStateAtCell(int row, int col) {
        return gridStructure[row][col].getCurrState();
    }

    public int getRowNum() { return rowNum; }
    public int getColNum() { return colNum; }
}