package cellsociety;

import java.util.ArrayList;
import java.util.HashSet;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class PredPreyCell extends Cell {
    public static final Paint SHARK_COLOR = Color.LIGHTSLATEGRAY;
    public static final Paint FISH_COLOR = Color.LIGHTSEAGREEN;
    public static final Paint EMPTY_COLOR = Color.SKYBLUE;

    private double reproductionRate;
    private double lifeSpan = 0;

    enum PredPreyCellState{
        SHARK, FISH, EMPTY
    }

    public PredPreyCell(double width, double height, String currState, String shape, double fertility){
        super(width,height,PredPreyCellState.valueOf(currState), shape);
        this.reproductionRate = fertility;
        changeDisplay();
    }

    public void calcNewState(ArrayList<HashSet<Cell>> emptySpaces){
        if(currState == PredPreyCellState.EMPTY && nextState == null) {
            nextState = PredPreyCellState.EMPTY;
            emptySpaces.get(1).add(this);
        }
        else if(currState == PredPreyCellState.SHARK) {
            int index = 1;

        }
    }

    public void changeDisplay(){
        if(currState == PredPreyCellState.EMPTY) {
            visual.setFill(EMPTY_COLOR);
        }
        else if(currState == PredPreyCellState.SHARK) {
            visual.setFill(SHARK_COLOR);
        }
        else {
            visual.setFill(FISH_COLOR);
        }
    }
}
