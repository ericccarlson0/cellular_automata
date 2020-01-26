package cellsociety;

import javafx.scene.layout.GridPane;

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
