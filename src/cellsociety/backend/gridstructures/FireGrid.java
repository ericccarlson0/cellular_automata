package cellsociety.backend.gridstructures;

import cellsociety.backend.Cell;

import java.util.ArrayList;
import java.util.List;

public class FireGrid extends GridStructure {
    public static final double[] TREE_COLOR = new double[]{0.2, 0.75, 0.2};
    public static final double[] EMPTY_COLOR = new double[]{0.8, 0.8, 0.6};
    public static final double[] FIRE_COLOR = new double[]{0.8, 0.2, 0.0};
    private double catchProb;

    enum FireCellState {
        EMPTY, TREE, FIRE
    }

    public FireGrid(int rowNum, int colNum, ArrayList<Double> percents, ArrayList<String> states,
                    double radius, String shape, int neighborhoodType, double catchProb) {
        super(rowNum, colNum, percents, states, radius, shape, neighborhoodType);
        this.catchProb = catchProb;
        this.init(shape);
    }

    protected Cell createCell(double radius, int row, int col) {
        FireCellState state = FireCellState.valueOf(generateState());
        return new Cell(radius, state);
    }

    @Override
    protected void updateColorRGB(Cell c) {
        if (c.getCurrState() == FireCellState.EMPTY) {
            c.setColor(EMPTY_COLOR);
        } else if (c.getCurrState() == FireCellState.TREE) {
            c.setColor(TREE_COLOR);
        } else {
            c.setColor(FIRE_COLOR);
        }
    }

    @Override
    protected void calcNewStates() {
        for (Cell c: cellList){
            fireSimStateRules(c);
        }
    }

    /* assumes 8 neighbors are given, only uses the 4 neighbors directly connecting */
    private void fireSimStateRules(Cell currCell) {
        List<Cell> allNeighbors = currCell.getNeighbors();

        if(currCell.getCurrState() == FireCellState.EMPTY || currCell.getCurrState() == FireCellState.FIRE) {
            currCell.setNextState(FireCellState.EMPTY);
        }
        else {
            int index = 1;
            boolean couldCatch = false;
            while(index < allNeighbors.size()){
                if(allNeighbors.get(index) != null && allNeighbors.get(index).getCurrState() == FireCellState.FIRE) {
                    couldCatch = true;
                    break;
                }
                index += 2;
            }
            if(couldCatch && Math.random() < catchProb) {
                currCell.setNextState(FireCellState.FIRE);
            }
            else{
                currCell.setNextState(FireCellState.TREE);
            }
        }
    }
}
