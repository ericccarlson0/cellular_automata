package cellsociety;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class FireCell extends Cell {
    public static final Paint TREE_COLOR = Color.FORESTGREEN;
    public static final Paint EMPTY_COLOR = Color.WHITE;
    public static final Paint FIRE_COLOR = Color.ORANGERED;

    private double probToCatch;

    enum FireCellState{
        EMPTY,TREE,FIRE;
    }

    public FireCell(double width, double height, String currState, double probToCatch){
        super(width,height,FireCellState.valueOf(currState));
        this.probToCatch = probToCatch;
        changeDisplay();
    }

    public void calcNewState(){
        if(currState == FireCellState.EMPTY || currState == FireCellState.FIRE){
            nextState = FireCellState.EMPTY;
        }
        else
        {
            int index = 1;
            boolean couldCatch = false;
            while(index < neighbors.length){
                if(neighbors[index] != null && neighbors[index].getCurrState() == FireCellState.FIRE){
                    couldCatch = true;
                    break;
                }
                index+=2;
            }
            if(couldCatch && (Math.random() < probToCatch)){
                nextState = FireCellState.FIRE;
            }
        }
    }

    public void changeDisplay(){
        if(currState == FireCellState.FIRE){
            vis.setFill(FIRE_COLOR);
        }
        else if(currState == FireCellState.EMPTY){
            vis.setFill(EMPTY_COLOR);
        }
        else{
            vis.setFill(TREE_COLOR);
        }
    }
}
