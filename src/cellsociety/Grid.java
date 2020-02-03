package cellsociety;

import javafx.scene.layout.GridPane;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.HashSet;

public class Grid {

    public static final double CELL_GAP = .1;
    public static final int DISPLAY_WIDTH = 500;
    public static final int DISPLAY_HEIGHT = 500;

    private Cell[][] gridStructure;
    private GridPane gridVisual;
    private SimulationRunner.SimulationType simType;
    private int size;
    private ArrayList<Double> statePercents;
    private ArrayList<String> states;
    private double miscValue;
    private ArrayList<HashSet<Cell>> emptySpaces;

    public Grid(SimulationRunner.SimulationType type, int size, ArrayList<Double> percents,
                ArrayList<String> states, double misc) {
        this.simType = type;
        this.size = size;
        this.states = states;
        this.statePercents = percents;
        this.miscValue = misc;

        emptySpaces = new ArrayList<>(); //***
        HashSet<Cell> currEmpty = new HashSet<>();
        HashSet<Cell> nextEmpty = new HashSet<>();

        emptySpaces.add(currEmpty);
        emptySpaces.add(nextEmpty);

        initPercents();
        initGridStructure();
        initGridVisual();
    }

    private void initPercents() {
        for (int index = 1; index < statePercents.size(); index++){
            statePercents.set(index, statePercents.get(index) + statePercents.get(index - 1));
        }
    }

    private void initGridStructure() {
        gridStructure = new Cell[size][size];
        createCells();
        initCellNeighbors();
    }

    private void initGridVisual() {
        gridVisual = new GridPane();
        gridVisual.setGridLinesVisible(false);
        gridVisual.setHgap(CELL_GAP);
        gridVisual.setVgap(CELL_GAP);
        for (int row = 0; row < size; row++){
            for (int col = 0; col < size; col++){
                Shape currCell = gridStructure[row][col].getVisual();
                gridVisual.add(currCell, col, row,1,1);
            }
        }
    }

    public void step() {
        calcNewStates();
        updateCell();
        updateEmpty();
        // System.out.println(emptySpaces.get(0).size());
    }

    private void createCells() {
        double cellWidth = DISPLAY_WIDTH / size - 2*CELL_GAP;
        double cellHeight = DISPLAY_HEIGHT / size - 2*CELL_GAP;
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                gridStructure[row][col] = makeCellOfType(row, col, cellWidth, cellHeight);
            }
        }
    }

    private Cell makeCellOfType(int row, int col, double width, double height) {
        Cell currCell = null;
        String initialState = generateState();
        switch (simType) {
            case LIFE:
                currCell = new LifeCell(width, height, initialState);
                break;
            case FIRE:
                currCell = new FireCell(width, height, initialState, miscValue);
                break;
            case PERCOLATION:
                if (row == 0 && Math.random() < miscValue) {
                    currCell = new PercolationCell(width, height,"FULL");
                } else {
                    currCell = new PercolationCell(width, height, initialState);
                } break;
            case SEGREGATION:
                currCell = new SegregationCell(width, height, initialState, miscValue);
                break;
            case PRED_PREY:
                //currCell = new PredPreyCell(width,height,status);
                break;
        }
        if (initialState.equals("EMPTY")) {
            emptySpaces.get(0).add(currCell);
        }
        return currCell;
    }

    private String generateState() {
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

    private void updateEmpty() {
        HashSet<Cell> nowEmpty = new HashSet<>();
        nowEmpty.addAll(emptySpaces.get(0));
        nowEmpty.addAll(emptySpaces.get(1));
        emptySpaces.set(0, nowEmpty);
        emptySpaces.get(1).clear();
    }

    public GridPane getGridVisual(){
        return gridVisual;
    }

    private void initCellNeighbors() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Cell[] neighbors = getNeighbors(row, col);
                gridStructure[row][col].setNeighbors(neighbors);
            }
        }
    }

    private Cell[] getNeighbors(int row, int col) {
        Cell[] neighbors = new Cell[8];
        neighbors[0] = isValidCoords(row - 1, col - 1);
        neighbors[1] = isValidCoords(row - 1,col);
        neighbors[2] = isValidCoords(row - 1,col + 1);
        neighbors[3] = isValidCoords(row,col + 1);
        neighbors[4] = isValidCoords(row + 1,col + 1);
        neighbors[5] = isValidCoords(row + 1,col);
        neighbors[6] = isValidCoords(row + 1,col - 1);
        neighbors[7] = isValidCoords(row,col - 1);
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

    private void calcNewStates(){
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                gridStructure[row][col].calcNewState(emptySpaces);
            }
        }
    }

    private void updateCell(){
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                gridStructure[row][col].updateState();
                gridStructure[row][col].changeDisplay();
            }
        }
    }
}
