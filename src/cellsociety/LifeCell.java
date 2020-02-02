package cellsociety;


import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class LifeCell extends Cell {
    public static final Paint ALIVE_COLOR = Color.BLACK;
    public static final Paint DEAD_COLOR = Color.WHITE;

    enum LifeCellState{
        ALIVE,DEAD,
    }

    public LifeCell(double width, double height, String currState){
        super(width,height,LifeCellState.valueOf(currState));
        changeDisplay();
    }

    public void calcNewState(){
        int numNeighborsAlive = 0;
        for(Cell currNeighbor : neighbors){
            if(currNeighbor != null && currNeighbor.getCurrState() == LifeCellState.ALIVE){
                numNeighborsAlive += 1;
            }
        }
        if(currState == LifeCellState.ALIVE && (numNeighborsAlive == 2 || numNeighborsAlive == 3)){
            nextState = LifeCellState.ALIVE;
        }
        else if(currState == LifeCellState.DEAD && numNeighborsAlive == 3){
            nextState = LifeCellState.ALIVE;
        }
        else{
            nextState = LifeCellState.DEAD;
        }
    }

    public void changeDisplay(){
        if(currState == LifeCellState.ALIVE)
            vis.setFill(ALIVE_COLOR);
        else{
            vis.setFill(DEAD_COLOR);
        }
    }
}
