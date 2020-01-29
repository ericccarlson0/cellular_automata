package cellsociety;

import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class Grid {
    private Cell[][] gridStructure;
    private GridPane gridVisual;
    private String simType;
    private int size;

    public Grid(String xmlFilename){
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
        //TODO
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
        neighbors[0] = checkNWN(row,col);
        neighbors[1] = checkNN(row,col);
        neighbors[2] = checkNEN(row,col);
        neighbors[3] = checkEN(row,col);
        neighbors[4] = checkSEN(row,col);
        neighbors[5] = checkSN(row,col);
        neighbors[6] = checkSWN(row,col);
        neighbors[7] = checkWN(row,col);
        return neighbors;
    }

    private Cell checkNWN(int row, int col){
        if(row > 0 && col > 0){
            return gridStructure[row - 1][col - 1];
        }
        return null;
    }

    private Cell checkNN(int row, int col){
        if(row > 0){
            return gridStructure[row - 1][col];
        }
        return null;
    }

    private Cell checkNEN(int row, int col){
        if(row > 0 && col < size - 1){
            return gridStructure[row - 1][col + 1];
        }
        return null;
    }

    private Cell checkEN(int row, int col){
        if(col < size - 1){
            return gridStructure[row][col + 1];
        }
        return null;
    }

    private Cell checkSEN(int row, int col){
        if(row < size - 1 && col < size - 1){
            return gridStructure[row + 1][col + 1];
        }
        return null;
    }

    private Cell checkSN(int row, int col){
        if(row < size - 1){
            return gridStructure[row + 1][col];
        }
        return null;
    }

    private Cell checkSWN(int row, int col){
        if(row < size - 1 && col > 0){
            return gridStructure[row + 1][col - 1];
        }
        return null;
    }

    private Cell checkWN(int row, int col){
        if(col > 0){
            return gridStructure[row][col - 1];
        }
        return null;
    }

    private void createCells() {
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                Cell currCell = null;
                switch(simType){
                    case "Life":
                        currCell = new LifeCell();
                        break;
                    case "Fire":
                        currCell = new FireCell();
                        break;
                    case "Percolation":
                        currCell = new PercolationCell();
                        break;
                    //TODO: add more cases for diff simulation types and enter parameters for creating new cells as needed.
                }
                gridStructure[row][col] = currCell;
            }
        }
    }

    private void initializeGridVisual() {
        gridVisual = new GridPane();
        gridVisual.setGridLinesVisible(true);
        gridVisual.setHgap(.1);
        gridVisual.setVgap(.1);
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
