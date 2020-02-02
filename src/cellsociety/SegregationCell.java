package cellsociety;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class SegregationCell extends Cell {
    public static final Paint ONE_COLOR = Color.BLUEVIOLET;
    public static final Paint TWO_COLOR = Color.MEDIUMVIOLETRED;
    public static final Paint EMPTY_COLOR = Color.WHITE;

    private double satisfactionThreshold;

    enum SegregationCellState{
        EMPTY,
        ONE,
        TWO
    }

    public SegregationCell(double width, double height, String currState, double satisfactionThreshold){
        super(width,height,SegregationCellState.valueOf(currState));
        this.satisfactionThreshold = satisfactionThreshold;
        changeDisplay();
    }

    public void calcNewState(ArrayList<HashSet<Cell>> emptySpaces){
        
    }

    public void changeDisplay(){
        if(currState == SegregationCellState.EMPTY) {
            vis.setFill(EMPTY_COLOR);
        }
        else if(currState == SegregationCellState.ONE) {
            vis.setFill(ONE_COLOR);
        }
        else {
            vis.setFill(TWO_COLOR);
        }
    }
}
