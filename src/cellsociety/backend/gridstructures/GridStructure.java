package cellsociety.backend.gridstructures;

import cellsociety.backend.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GridStructure {

    // public static final double CELL_GAP = 0;
    // public static final int DISPLAY_WIDTH = 500;
    // public static final int DISPLAY_HEIGHT = 500;

    protected ArrayList<Cell> cellList;

    private Cell[][] gridStructure;
    private int rowNum;
    private int colNum;
    private double cellRadius;
    private List<Double> statePercents;
    private List<String> states;
    private int neighborhoodType;

    public GridStructure(int nowNum, int colNum, List<Double> percents, List<String> states,
                         double radius,  String shape, int neighborhoodType) {
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.cellRadius = radius;
        this.states = states;
        this.statePercents = percents;
        this.neighborhoodType = neighborhoodType;
    }

    protected abstract void calcNewStates();
    protected abstract void updateColorRGB(Cell cell);
    protected abstract Cell createCell(double radius, int row, int col);

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

    protected void init(String shape) { // Where is this called?
        initPercents();
        initGridStructure(shape);
    }

    public void step() {
        Collections.shuffle(cellList);
        calcNewStates();
        updateCellStates();
    }

    private void createCells(String shape) {
        for(int row=0; row < rowNum; row++) {
            for(int col=0; col < colNum; col++) {
                Cell curr = createCell(cellRadius, row, col);
                gridStructure[row][col] = curr;
                cellList.add(curr);
                updateColorRGB(curr);
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
                List<Cell> neighbors = new ArrayList<>();
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
        boolean colValid = (col >= 0) && (col < rowNum);
        if (rowValid && colValid){
            return gridStructure[row][col];
        }
        return null;
    }

    private void updateCellStates(){
        for (Cell cell : cellList){
            cell.updateState();
            updateColorRGB(cell);
        }
    }

    public Cell getCellAtIndex(int row, int col){
        return gridStructure[row][col];
    }

    public int getRowNum() { return rowNum; }
    public int getColNum() { return colNum; }
    public double getRadius() { return cellRadius; }
}
