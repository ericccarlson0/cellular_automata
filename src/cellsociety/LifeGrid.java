package cellsociety;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class LifeGrid extends GridStructure{
    public static final Paint ALIVE_COLOR = Color.BLACK;
    public static final Paint EMPTY_COLOR = Color.WHITE;

    enum LifeCellStates {
        ALIVE, EMPTY
    }

    public LifeGrid(int size, ArrayList<Double> percents, ArrayList<String> states, String shape, int numNeighbors){
        super(size,percents,states,shape,numNeighbors);
    }

    protected void calcNewStates(){
        for(Cell c: allCells){
            lifeSimStateRules(c);
        }
    }

    private void lifeSimStateRules(Cell currCell) {
        ArrayList<Cell> allNeighbors = currCell.getNeighbors();
        int numNeighborsAlive = 0;
        for(Cell currNeighbor: allNeighbors){
            if(currNeighbor.getCurrState() == LifeCellStates.ALIVE){
                numNeighborsAlive++;
            }
            if(currCell.getCurrState() == LifeCellStates.ALIVE && (numNeighborsAlive == 2 || numNeighborsAlive == 3)) {
                currCell.setNextState(LifeCellStates.ALIVE);
            }
            else if(currCell.getCurrState() == LifeCell.LifeCellState.EMPTY && numNeighborsAlive == 3) {
                currCell.setNextState(LifeCellStates.ALIVE);
            } else {
                currCell.setNextState(LifeCellStates.EMPTY);
            }
        }
    }

    protected Cell makeCellOfType(double width, double height, String shape){
        LifeCellStates selectedState = LifeCellStates.valueOf(generateState());
        return new Cell(width,height,selectedState,shape);
    }

    protected void updateColor(Cell c){
        if (c.getCurrState() == LifeCellStates.ALIVE) {
            c.setColor(ALIVE_COLOR);
        } else {
            c.setColor(EMPTY_COLOR);
        }
    }

}
