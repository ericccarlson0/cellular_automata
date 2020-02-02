package cellsociety;


import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class LifeCell extends Cell {
    public static final Paint ALIVE_COLOR = Color.BLACK;
    public static final Paint DEAD_COLOR = Color.WHITE;

    public LifeCell(double width, double height, int currState){
        super(width,height,currState);
        changeDisplay();
    }

    public void calcNewState(){
        int numNeighborsAlive = 0;
        for(Cell currNeighbor : neighbors){
            if(currNeighbor != null){
                numNeighborsAlive += currNeighbor.getCurrState();
            }
        }
        if(currState == 1 && (numNeighborsAlive == 2 || numNeighborsAlive == 3)){
            nextState = 1;
        }
        else if(currState == 0 && numNeighborsAlive == 3){
            nextState = 1;
        }
        else{
            nextState = 0;
        }
    }

    public void changeDisplay(){
        if(currState == 1)
            vis.setFill(ALIVE_COLOR);
        else{
            vis.setFill(DEAD_COLOR);
        }
    }
}
