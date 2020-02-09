package cellsociety.backend.gridstructures;

import cellsociety.backend.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GridStructure {

    protected ArrayList<Cell> allCells;

    private Cell[][] gridStructure;
    private int size;
    private List<Double> statePercents;
    private List<String> states;
    private int numNeighbors;

    public GridStructure(int size, List<Double> percents, List<String> states, int numNeighbors) {
        this.size = size;
        this.states = states;
        this.statePercents = percents;
        this.numNeighbors = numNeighbors;
    }

    protected abstract void calcNewStates();

    protected abstract Cell makeCellOfType(int row, int col);

    private void initPercents() {
        for (int index = 1; index < statePercents.size(); index++){
            statePercents.set(index, statePercents.get(index) + statePercents.get(index - 1));
        }
    }

    private void initGridStructure() {
        gridStructure = new Cell[size][size];
        allCells = new ArrayList<>();
        createCells();
        initCellNeighbors(numNeighbors);
    }

    protected void init() {
        initPercents();
        initGridStructure();
    }

    public void step() {
        Collections.shuffle(allCells);
        calcNewStates();
        updateCellStates();
    }

    private void createCells() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Cell curr = makeCellOfType(row,col);
                gridStructure[row][col] = curr;
                allCells.add(curr);
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

    //TODO: set up so that supports creating neighborhoods of different amounts for individual cells
    private void initCellNeighbors(int numNeighbors) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                ArrayList<Cell> neighbors = new ArrayList<>();
                switch(numNeighbors){
                    case 8:
                        neighbors = getNeighborsEight(row, col);
                        break;
                }
                removeNulls(neighbors);
                gridStructure[row][col].setNeighbors(neighbors);
            }
        }
    }

    private void removeNulls(ArrayList<Cell> neighbors) {
        //found at https://stackoverflow.com/questions/4819635/how-to-remove-all-null-elements-from-a-arraylist-or-string-array
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
        boolean rowValid = (row >= 0) && (row < size);
        boolean colValid = (col >= 0) && (col < size);
        if (rowValid && colValid){
            return gridStructure[row][col];
        }
        return null;
    }

    private void updateCellStates(){
        for(Cell c : allCells){
            c.updateState();
        }
    }

    public Cell getCellAtIndex(int row, int col){
        return gridStructure[row][col];
    }

    public int getSize() {
        return size;
    }

    public Object getStateAtCell(int row, int col){
        return gridStructure[row][col].getCurrState();
    }
}
