package cellsociety;

import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class Grid {


    public static final double CELL_GAP = .1;
    private static double DISPLAY_WIDTH = 600;
    private static double DISPLAY_HEIGHT = 600;

    private String[][] initStatus;
    private Cell[][] gridStructure;
    private GridPane gridVisual;
    private SimulationRunner.SimulationType simType;
    private int size;

    public Grid(SimulationRunner.SimulationType typ, int size){
        simType = typ;
        this.size = size;
        initializeGridStructure();
        initializeGridVisual();
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
                gridStructure[row][col] = makeCellOfType(cellWidth,cellHeight,initStatus[row][col]);
            }
        }
    }

    private Cell makeCellOfType(double width, double height, String status){
        Cell currCell = null;
        switch(simType){
            case LIFE:
                currCell = new LifeCell(width,height,status);
                break;
            case FIRE:
                currCell = new FireCell(width,height,status);
                break;
            case PERCOLATION:
                currCell = new PercolationCell(width,height,status);
                break;
            case SEGREGATION:
                currCell = new SegregationCell(width,height,status);
                break;
            case PREDPREY:
                currCell = new PredPreyCell(width,height,status);
                break;
            //TODO: add more cases for diff simulation types and enter parameters for creating new cells as needed.
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
                gridStructure[row][col].calcNewState();
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
}
