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

    private void parseFile(String xmlFilename) {

    }

    private void initializeGridStructure() {
        createCells();
        populateCellNeighbors();
    }

    private void populateCellNeighbors() {

    }

    private void createCells() {

    }

    private void initializeGridVisual() {
        gridVisual = new GridPane();
        gridVisual.setGridLinesVisible(true);
        gridVisual.setHgap(.5);
        gridVisual.setVgap(.5);
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
