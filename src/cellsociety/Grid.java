package cellsociety;

import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Grid {


    public static final double CELL_GAP = .1;
    private static double DISPLAY_WIDTH = 600;
    private static double DISPLAY_HEIGHT = 600;

    private Cell[][] gridStructure;
    private GridPane gridVisual;
    private SimulationRunner.SimulationType simType;
    private int size;
    private ArrayList<Double> statePercents;
    private ArrayList<String> states;
    private double miscVal;
    private ArrayList<Cell> emptySpaces;

    public Grid(SimulationRunner.SimulationType typ, int size, ArrayList<Double> percents, ArrayList<String> associatedTypes, double misc){
        simType = typ;
        this.size = size;
        states = associatedTypes;
        statePercents = percents;
        miscVal = misc;
        emptySpaces = new ArrayList<Cell>();
        initPercents();
        initializeGridStructure();
        initializeGridVisual();
    }

    private void initPercents(){
        for(int index = 1; index < statePercents.size(); index++){
            statePercents.set(index,statePercents.get(index) + statePercents.get(index - 1));
        }
    }

    public void step(){
        calcNewStates();
        updateCell();
    }

    public GridPane getGridVisual(){
        return gridVisual;
    }

    private void initializeGridStructure() {
        gridStructure = new Cell[size][size];
        createCells();
        populateCellNeighbors();
    }

    private void populateCellNeighbors() {
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                Cell[] neighbors = getNeighbors(row,col);
                gridStructure[row][col].setNeighbors(neighbors);
            }
        }
    }

    private String determineInitState() {
        double val = Math.random() * 100;
        int index = 0;
        while(index < statePercents.size()){
            if (val < statePercents.get(index)) {
                return states.get(index);
            }
            index++;
        }
        return states.get(-1);
    }

    //gives array of neighbor cells starting in NE corner and moving clockwise
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

    private Cell isValidCoords(int row, int col){
        boolean rowValid = row >= 0 && row <= size - 1;
        boolean colValid = col >= 0 && col <= size - 1;
        if(rowValid && colValid){
            return gridStructure[row][col];
        }
        return null;
    }

    private void createCells() {
        double cellWidth = DISPLAY_WIDTH / size - 2*(CELL_GAP);
        double cellHeight = DISPLAY_HEIGHT / size - 2*(CELL_GAP);
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                gridStructure[row][col] = makeCellOfType(row, col, cellWidth, cellHeight);
            }
        }
    }

    private Cell makeCellOfType(int row, int col, double width, double height){
        Cell currCell = null;
        String initState = determineInitState();
        switch(simType){
            case LIFE:
                currCell = new LifeCell(width,height,initState);
                break;
            case FIRE:
                currCell = new FireCell(width,height,initState,miscVal);
                break;
            case PERCOLATION:
                if(row == 0 && Math.random() < miscVal){
                    currCell = new PercolationCell(width,height,"FULL");
                }
                else{
                    currCell = new PercolationCell(width,height,initState);
                }
                break;
            case SEGREGATION:
                //currCell = new SegregationCell(width,height,status);
                break;
            case PREDPREY:
                //currCell = new PredPreyCell(width,height,status);
                break;
            //TODO: add more cases for diff simulation types and enter parameters for creating new cells as needed.
        }
        if(initState.equals("EMPTY")){
            emptySpaces.add(currCell);
        }
        return currCell;
    }

    private void initializeGridVisual() {
        gridVisual = new GridPane();
        gridVisual.setGridLinesVisible(true);
        gridVisual.setHgap(CELL_GAP);
        gridVisual.setVgap(CELL_GAP);
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                Rectangle currCell = gridStructure[row][col].getVis();
                gridVisual.add(currCell,col,row,1,1);
            }
        }
    }

    private void calcNewStates(){
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++){
                gridStructure[row][col].calcNewState(emptySpaces);
            }
        }
    }

    private void updateCell(){
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++){
                gridStructure[row][col].updateState();
                gridStructure[row][col].changeDisplay();
            }
        }
    }

    private void determineEmptySpaces(){
        emptySpaces = new ArrayList<Cell>();
    }
}
