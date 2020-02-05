package cellsociety;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashSet;

public class PercolationCell extends Cell {
    public static final Paint BLOCK_COLOR = Color.color(0.4, 0.2, 0.2);
    public static final Paint FULL_COLOR = Color.color(0.5, 0.75, 1.0);
    public static final Paint EMPTY_COLOR = Color.color(1.0, 1.0, 1.0);

    enum PercolationCellState {
        BLOCK, EMPTY, FULL
    }

    public PercolationCell(double width, double height, String currState, String shape) {
        super(width, height, PercolationCellState.valueOf(currState), shape);
        changeDisplay();
    }

    public void calcNewState (ArrayList<HashSet<Cell>> emptySpaces) {
        if(currState == PercolationCellState.BLOCK){
            nextState = PercolationCellState.BLOCK;
        } else if(currState == PercolationCellState.FULL) {
            nextState = PercolationCellState.FULL;
        } else {
            int numNeighborsFull = 0;
            for(Cell currNeighbor : neighbors){
                if(currNeighbor != null && currNeighbor.getCurrState() == PercolationCellState.FULL){
                    numNeighborsFull += 1;
                }
            }
            if(numNeighborsFull > 0) {
                nextState = PercolationCellState.FULL;
                emptySpaces.get(0).remove(this);
            } else {
                nextState = PercolationCellState.EMPTY;
            }
        }
    }

    public void changeDisplay() {
        if (currState == PercolationCellState.BLOCK) {
            visual.setFill(BLOCK_COLOR);
        } else if (currState == PercolationCellState.EMPTY) {
            visual.setFill(EMPTY_COLOR);
        } else {
            visual.setFill(FULL_COLOR);
        }
    }
}
