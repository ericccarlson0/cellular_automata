package cellsociety;

import java.util.ArrayList;

public class SegregationCell extends Cell {

    enum SegregationCellState{
        EMPTY,ONE,TWO
    }

    public SegregationCell(double width, double height, String currState){
        super(width,height,SegregationCellState.valueOf(currState));
        changeDisplay();
    }

    public void calcNewState(ArrayList<Cell> emptySpaces){

    }

    public void changeDisplay(){
    }
}
