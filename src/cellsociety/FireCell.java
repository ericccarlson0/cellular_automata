package cellsociety;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.ArrayList;
import java.util.HashSet;

public class FireCell extends Cell {
    public static final Paint TREE_COLOR = Color.color(0.2, 0.75, 0.2);
    public static final Paint EMPTY_COLOR = Color.color(0.8, 0.8, 0.6);
    public static final Paint FIRE_COLOR = Color.color(0.8, 0.2, 0.0);

    private double catchProb;

    enum FireCellState {
        EMPTY, TREE, FIRE
    }

    public FireCell(double width, double height, String currState, String shape, double catchProb) {
        super(width, height, FireCellState.valueOf(currState), shape);
        this.catchProb = catchProb;
        changeDisplay();
    }

    public void calcNewState(ArrayList<HashSet<Cell>> emptySpaces) {
        if (currState == FireCellState.EMPTY || currState == FireCellState.FIRE) {
            nextState = FireCellState.EMPTY;
            emptySpaces.get(1).add(this);
        } else {
            int index = 1;
            boolean couldCatch = false;
            while (index < neighbors.length){
                if (neighbors[index] != null &&
                        neighbors[index].getCurrState() == FireCellState.FIRE){
                    couldCatch = true;
                    break;
                }
                index += 2;
            }
            if (couldCatch && (Math.random() < catchProb)) {
                nextState = FireCellState.FIRE;
            }
        }
    }

    public void changeDisplay() {
        if (currState == FireCellState.FIRE) {
            visual.setFill(FIRE_COLOR);
        } else if (currState == FireCellState.EMPTY) {
            visual.setFill(EMPTY_COLOR);
        } else {
            visual.setFill(TREE_COLOR);
        }
    }
}
