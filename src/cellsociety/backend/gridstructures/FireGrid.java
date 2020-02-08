package cellsociety.backend.gridstructures;

import cellsociety.backend.Cell;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class FireGrid extends GridStructure {
    public static final Paint TREE_COLOR = Color.color(0.2, 0.75, 0.2);
    public static final Paint EMPTY_COLOR = Color.color(0.8, 0.8, 0.6);
    public static final Paint FIRE_COLOR = Color.color(0.8, 0.2, 0.0);

    private double catchProb;

    enum FireCellStates {
        EMPTY, TREE, FIRE
    }

    public FireGrid(int size, ArrayList<Double> percents, ArrayList<String> states, int numNeighbors, double catchProb){
        super(size,percents,states,numNeighbors);
        this.catchProb = catchProb;
    }

    @Override
    protected void calcNewStates() {
        for(Cell c: allCells){
            fireSimStateRules(c);
        }
    }

    @Override
    protected void updateColor(Cell c) {
        if(c.getCurrState() == FireCellStates.EMPTY) {
            c.setColor(EMPTY_COLOR);
        }
        else if(c.getCurrState() == FireCellStates.TREE) {
            c.setColor(TREE_COLOR);
        }
        else{
            c.setColor(FIRE_COLOR);
        }
    }

    @Override
    protected Cell makeCellOfType(double width, double height, String shape, int row, int col) {
        FireCellStates selectedState = FireCellStates.valueOf(generateState());
        return new Cell(width,height,selectedState,shape);
    }

    /* assumes 8 neighbors are given, only uses the 4 neighbors directly connecting */
    private void fireSimStateRules(Cell currCell) {
        List<Cell> allNeighbors = currCell.getNeighbors();

        if(currCell.getCurrState() == FireCellStates.EMPTY || currCell.getCurrState() == FireCellStates.FIRE) {
            currCell.setNextState(FireCellStates.EMPTY);
        }
        else {
            int index = 1;
            boolean couldCatch = false;
            while(index < allNeighbors.size()){
                if(allNeighbors.get(index) != null && allNeighbors.get(index).getCurrState() == FireCellStates.FIRE) {
                    couldCatch = true;
                    break;
                }
                index += 2;
            }
            if(couldCatch && Math.random() < catchProb) {
                currCell.setNextState(FireCellStates.FIRE);
            }
            else{
                currCell.setNextState(FireCellStates.TREE);
            }
        }
    }
}
