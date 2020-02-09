package cellsociety;

import cellsociety.backend.Cell;
import cellsociety.backend.gridstructures.GridStructure;
import cellsociety.frontend.GridDisplay;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class Simulation {
    private GridStructure gridStruct;
    private GridDisplay gridDisplay;
    private int size;

    public enum AllStates {
        LIFE_ALIVE(Color.BLACK),
        LIFE_EMPTY(Color.WHITE),
        FIRE_TREE(Color.color(0.2, 0.75, 0.2)),
        FIRE_FIRE(Color.color(0.8, 0.2, 0.0)),
        FIRE_EMPTY(Color.color(0.8, 0.8, 0.6)),
        PERCOLATION_FULL(Color.color(0.5, 0.75, 1.0)),
        PERCOLATION_EMPTY(Color.color(1.0, 1.0, 1.0)),
        PERCOLATION_BLOCK(Color.color(0.4, 0.2, 0.2)),
        SEGREGATION_ONE(Color.color(1.0, 0.5, 0.5)),
        SEGREGATION_TWO(Color.color(0.5, 0.5, 1.0)),
        SEGREGATION_EMPTY(Color.color(1.0, 1.0, 1.0));

        private Color stateColor;
        AllStates(Color p){
            stateColor = p;
        }

        public Color getColor(){
            return stateColor;
        }
    }


    public Simulation(GridStructure gs, String myShape) {
        gridStruct = gs;
        size = gs.getSize();
        initDisplay(myShape, size);
    }

    private void initDisplay(String myShape, int size) {
        gridDisplay = new GridDisplay(myShape,size);
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                Cell currCell = gridStruct.getCellAtIndex(row,col);
                gridDisplay.addCellToDisplay(row,col,currCell.getCurrState());
            }
        }
    }

    public Region getDisplay() {
        return gridDisplay.getDisplay();
    }

    public void step() {
        gridStruct.step();
        updateDisplay();
    }

    private void updateDisplay() {
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                gridDisplay.updateDisplayAtCell(row, col, gridStruct.getStateAtCell(row,col));
            }
        }
    }
}
