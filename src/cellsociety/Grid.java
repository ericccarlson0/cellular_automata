package cellsociety;

import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class Grid {
    public static final double CELL_GAP = .1;

    private String[][] initStatus;
    private Cell[][] gridStructure;
    private GridPane gridVisual;
    private String simType;
    private int size;
    private double displayWidth;
    private double displayHeight;

    public Grid(String xmlFilename,double displayWidth, double displayHeight){
        this.displayHeight = displayHeight;
        this.displayWidth = displayWidth;
        parseFile(xmlFilename);
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

    private void parseFile(String xmlFilename) {
        //TODO parse xml file set values necessary
    }

    private void initializeGridStructure() {
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
        double cellWidth = displayWidth / size - 2*(CELL_GAP);
        double cellHeight = displayHeight / size - 2*(CELL_GAP;
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                gridStructure[row][col] = makeCellOfType(cellWidth,cellHeight,initStatus[row][col]);
            }
        }
    }

    private Cell makeCellOfType(double width, double height, String status){
        Cell currCell = null;
        switch(simType){
            //TODO: use enums?
            case "Life":
                currCell = new LifeCell(width,height,status);
                break;
            case "Fire":
                currCell = new FireCell(width,height,status);
                break;
            case "Percolation":
                currCell = new PercolationCell(width,height,status);
                break;
            case "Segregation":
                currCell = new SegregationCell(width,height,status);
                break;
            case "PredPrey":
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
