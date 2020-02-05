package cellsociety;

import java.util.ArrayList;
import java.util.Collections;

public abstract class GridStructure {

    public static final double CELL_GAP = .1;
    public static final int DISPLAY_WIDTH = 500;
    public static final int DISPLAY_HEIGHT = 500;

    protected ArrayList<Cell> allCells;

    private Cell[][] gridStructure;
    private int size;
    private ArrayList<Double> statePercents;
    private ArrayList<String> states;
    private int numNeighbors;

    public GridStructure(int size, ArrayList<Double> percents, ArrayList<String> states, String shape, int numNeighbors) {
        this.size = size;
        this.states = states;
        this.statePercents = percents;
        this.numNeighbors = numNeighbors;
        initPercents();
        initGridStructure(shape);
    }

    protected abstract void calcNewStates();

    //TODO: SHOULD PROB GET MOVED to GridDisplay
    protected abstract void updateColor(Cell c);

    protected abstract Cell makeCellOfType(double width, double height, String shape);

    private void initPercents() {
        for (int index = 1; index < statePercents.size(); index++){
            statePercents.set(index, statePercents.get(index) + statePercents.get(index - 1));
        }
    }

    private void initGridStructure(String shape) {
        gridStructure = new Cell[size][size];
        allCells = new ArrayList<>();
        createCells(shape);
        initCellNeighbors(numNeighbors);
    }

    public void step() {
        Collections.shuffle(allCells);
        calcNewStates();
        updateCellStates();
    }

    private void createCells(String shape) {
        double cellWidth = DISPLAY_WIDTH / size - 2*CELL_GAP;
        double cellHeight = DISPLAY_HEIGHT / size - 2*CELL_GAP;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Cell curr = makeCellOfType(cellWidth,cellHeight,shape);
                gridStructure[row][col] = curr;
                allCells.add(curr);
                updateColor(curr);
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
            updateColor(c);
        }
    }

    protected void setInitColors(){
        for(Cell c : allCells){
            updateColor(c);
        }
    }

    public Cell[][] getConfig(){
        return gridStructure;
    }

}
