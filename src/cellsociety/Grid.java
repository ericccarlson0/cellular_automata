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
        updateCellStates();
        updateCellDisplays();
    }

    private void parseFile(String xmlFilename) {

    }

    private void initializeGridStructure() {

    }

    private void initializeGridVisual() {

    }

    private void calcNewStates(){

    }

    private void updateCellStates(){

    }

    private void updateCellDisplays(){

    }
}
