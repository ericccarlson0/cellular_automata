package cellsociety;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class PercolationCell extends Cell {
    public static final Paint BLOCK_COLOR = Color.BLACK;
    public static final Paint EMPTY_COLOR = Color.WHITE;
    public static final Paint FULL_COLOR = Color.SKYBLUE;

    enum PercolationCellState{
        BLOCK,EMPTY,FULL;
    }

    public PercolationCell(double width, double height, String currState){
        super(width,height,PercolationCellState.valueOf(currState));
        changeDisplay();
    }

    public void calcNewState(ArrayList<Cell> emptySpaces){
        if(currState == PercolationCellState.BLOCK){
            nextState = PercolationCellState.BLOCK;
        }
        else if(currState == PercolationCellState.FULL){
            nextState = PercolationCellState.FULL;
        }
        else
        {
            int numNeighborsFull = 0;
            for(Cell currNeighbor : neighbors){
                if(currNeighbor != null && currNeighbor.getCurrState() == PercolationCellState.FULL){
                    numNeighborsFull += 1;
                }
            }
            if(numNeighborsFull > 0){
                nextState = PercolationCellState.FULL;
            }
            else{
                nextState = PercolationCellState.EMPTY;
            }
        }
    }

    public void changeDisplay(){
        if(currState == PercolationCellState.BLOCK)
            vis.setFill(BLOCK_COLOR);
        else if(currState == PercolationCellState.EMPTY)
            vis.setFill(EMPTY_COLOR);
        else{
            vis.setFill(FULL_COLOR);
        }
    }
}
