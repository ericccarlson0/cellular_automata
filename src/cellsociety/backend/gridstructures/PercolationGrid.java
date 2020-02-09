package cellsociety.backend.gridstructures;

import cellsociety.backend.Cell;

import java.util.ArrayList;
import java.util.List;

public class PercolationGrid extends GridStructure {
    public static final double[] BLOCK_COLOR = new double[]{0.4, 0.2, 0.2};
    public static final double[] FULL_COLOR = new double[]{0.5, 0.75, 1.0};
    public static final double[] EMPTY_COLOR = new double[]{1.0, 1.0, 1.0};

    private double initialFillProbability;

    enum PercolationCellState {
        BLOCK, EMPTY, FULL
    }

    public PercolationGrid(int rowNum, int colNum, ArrayList<Double> percents, ArrayList<String> states,
                           int radius, String shape, int neighborhoodType, double initialFillProbability){
        super(rowNum, colNum, percents, states, radius, shape, neighborhoodType);
        this.initialFillProbability = initialFillProbability;
        this.init(shape);
    }

    @Override
    protected Cell createCell(double radius, int row, int col){
        PercolationCellState state;
        if (row == 0 && Math.random() < initialFillProbability) {
            state = PercolationCellState.FULL;
        } else {
            state = PercolationCellState.valueOf(generateState());
        }
        return new Cell(radius, state);
    }

    protected void updateColorRGB(Cell c){
        if (c.getCurrState() == PercolationCellState.EMPTY) {
            c.setColor(EMPTY_COLOR);
        } else if (c.getCurrState() == PercolationCellState.FULL) {
            c.setColor(FULL_COLOR);
        } else {
            c.setColor(BLOCK_COLOR);
        }
    }

    @Override
    protected void calcNewStates(){
        for (Cell c: cellList){
            percolationSimStateRules(c);
        }
    }

    private void percolationSimStateRules(Cell currCell){
        List<Cell> allNeighbors = currCell.getNeighbors();

        if(currCell.getCurrState() == PercolationCellState.BLOCK){
            currCell.setNextState(PercolationCellState.BLOCK);
        } else if(currCell.getCurrState() == PercolationCellState.FULL){
            currCell.setNextState(PercolationCellState.FULL);
        }
        else{
            int numNeighborsFull = 0;
            for(Cell currNeighbor: allNeighbors) {
                if(currNeighbor != null && currNeighbor.getCurrState() == PercolationCellState.FULL){
                    numNeighborsFull++;
                }
            }
            if(numNeighborsFull > 0){
                currCell.setNextState(PercolationCellState.FULL);
            }
            else{
                currCell.setNextState(PercolationCellState.EMPTY);
            }
        }
    }
}
