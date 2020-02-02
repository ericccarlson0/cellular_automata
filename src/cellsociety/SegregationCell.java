package cellsociety;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SegregationCell extends Cell {

    enum SegregationCellState{
        EMPTY,ONE,TWO
    }

    public SegregationCell(double width, double height, String currState){
        super(width,height,SegregationCellState.valueOf(currState));
        changeDisplay();
    }

    public void calcNewState(ArrayList<HashSet<Cell>> emptySpaces){

    }

    public void changeDisplay(){
    }
}
