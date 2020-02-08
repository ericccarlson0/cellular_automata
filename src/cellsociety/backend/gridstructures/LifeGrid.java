package cellsociety.backend.gridstructures;

import cellsociety.backend.Cell;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class LifeGrid extends GridStructure{
    public static final Paint ALIVE_COLOR = Color.BLACK;
    public static final Paint EMPTY_COLOR = Color.WHITE;

    enum LifeCellStates {
        LIFE_ALIVE, LIFE_EMPTY
    }

    public LifeGrid(int size, ArrayList<Double> percents, ArrayList<String> states, int numNeighbors){
        super(size,percents,states,numNeighbors);
    }

    protected void calcNewStates(){
        for(Cell c: allCells){
            lifeSimStateRules(c);
        }
    }

    private void lifeSimStateRules(Cell currCell) {
        List<Cell> allNeighbors = currCell.getNeighbors();
        int numNeighborsAlive = 0;
        for(Cell currNeighbor: allNeighbors){
            if(currNeighbor.getCurrState() == LifeCellStates.LIFE_ALIVE){
                numNeighborsAlive++;
            }
            if(currCell.getCurrState() == LifeCellStates.LIFE_ALIVE && (numNeighborsAlive == 2 || numNeighborsAlive == 3)) {
                currCell.setNextState(LifeCellStates.LIFE_ALIVE);
            }
            else if(currCell.getCurrState() == LifeCellStates.LIFE_EMPTY && numNeighborsAlive == 3) {
                currCell.setNextState(LifeCellStates.LIFE_ALIVE);
            } else {
                currCell.setNextState(LifeCellStates.LIFE_EMPTY);
            }
        }
    }

    protected Cell makeCellOfType(double width, double height, String shape, int row, int col){
        LifeCellStates selectedState = LifeCellStates.valueOf(generateState());
        return new Cell(width,height,selectedState,shape);
    }

    protected void updateColor(Cell c){
        if (c.getCurrState() == LifeCellStates.LIFE_ALIVE) {
            c.setColor(ALIVE_COLOR);
        } else {
            c.setColor(EMPTY_COLOR);
        }
    }

}
