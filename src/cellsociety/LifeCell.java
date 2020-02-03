package cellsociety;


import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashSet;

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

    public void calcNewState(ArrayList<HashSet<Cell>> emptySpaces){
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
            emptySpaces.get(0).remove(this);
        }
        else{
            nextState = LifeCellState.EMPTY;
            emptySpaces.get(1).add(this);
        }
    }

    public void changeDisplay(){
        if(currState == LifeCellState.ALIVE)
            visual.setFill(ALIVE_COLOR);
        else{
            visual.setFill(EMPTY_COLOR);
        }
    }
}
