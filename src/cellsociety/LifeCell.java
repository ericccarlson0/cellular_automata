package cellsociety;


import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Set;

public class LifeCell extends Cell {
    public static final Paint ALIVE_COLOR = Color.BLACK;
    public static final Paint EMPTY_COLOR = Color.WHITE;

    enum LifeCellState{
        ALIVE,EMPTY;
    }

    public LifeCell(double width, double height, String currState){
        super(width,height,LifeCellState.valueOf(currState));
        changeDisplay();
    }

    public void calcNewState(Set<Cell> emptySpaces){
        int numNeighborsAlive = 0;
        for(Cell currNeighbor : neighbors){
            if(currNeighbor != null && currNeighbor.getCurrState() == LifeCellState.ALIVE){
                numNeighborsAlive += 1;
            }
        }
        if(currState == LifeCellState.ALIVE && (numNeighborsAlive == 2 || numNeighborsAlive == 3)){
            nextState = LifeCellState.ALIVE;
        }
        else if(currState == LifeCellState.EMPTY && numNeighborsAlive == 3){
            nextState = LifeCellState.ALIVE;
            emptySpaces.remove(this);
        }
        else{
            nextState = LifeCellState.EMPTY;
            emptySpaces.add(this);
        }
    }

    public void changeDisplay(){
        if(currState == LifeCellState.ALIVE)
            vis.setFill(ALIVE_COLOR);
        else{
            vis.setFill(EMPTY_COLOR);
        }
    }
}
