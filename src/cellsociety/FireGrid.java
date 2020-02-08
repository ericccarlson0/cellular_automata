package cellsociety;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class FireGrid extends GridStructure {
    public static final double[] TREE_COLOR = new double[]{0.2, 0.75, 0.2};
    public static final double[] EMPTY_COLOR = new double[]{0.8, 0.8, 0.6};
    public static final double[] FIRE_COLOR = new double[]{0.8, 0.2, 0.0};
    private double catchProb;

    enum FireCellStates {
        EMPTY, TREE, FIRE
    }

    public FireGrid(int rowNum, int colNum, ArrayList<Double> percents, ArrayList<String> states,
                    double radius, String shape, int neighborhoodType, double catchProb) {
        super(rowNum, colNum, percents, states, radius, shape, neighborhoodType);
        this.catchProb = catchProb;
        this.init(shape);
    }

    @Override
    protected void calcNewStates() {
        for(Cell c: cellList){
            fireSimStateRules(c);
        }
    }

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

    protected Cell makeCell(double radius, String shape, int row, int col) {
        FireCellStates selectedState = FireCellStates.valueOf(generateState());
        return new Cell(radius, selectedState,shape);
    }

    /* assumes 8 neighbors are given, only uses the 4 neighbors directly connecting */
    private void fireSimStateRules(Cell currCell) {
        ArrayList<Cell> allNeighbors = currCell.getNeighbors();

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
