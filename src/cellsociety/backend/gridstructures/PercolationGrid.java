package cellsociety.backend.gridstructures;

import cellsociety.backend.Cell;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class PercolationGrid extends GridStructure {
    public static final double[] BLOCK_COLOR = new double[]{0.4, 0.2, 0.2};
    public static final double[] FULL_COLOR = new double[]{0.5, 0.75, 1.0};
    public static final double[] EMPTY_COLOR = new double[]{1.0, 1.0, 1.0};

    private double initialFillProbability;

    enum PercolationCellStates {
        BLOCK, EMPTY, FULL
    }

    public PercolationGrid(int rowNum, int colNum, ArrayList<Double> percents, ArrayList<String> states,
                           int radius, String shape, int numNeighbors, double initialFillProbability){
        super(rowNum, colNum, percents, states, radius, shape,numNeighbors);
        this.initialFillProbability = initialFillProbability;
        this.init(shape);
    }

    protected void calcNewStates(){
        for(Cell c: cellList){
            percolationSimStateRules(c);
        }
    }

    private void percolationSimStateRules(Cell currCell){
        List<Cell> allNeighbors = currCell.getNeighbors();

        if(currCell.getCurrState() == PercolationCellStates.BLOCK){
            currCell.setNextState(PercolationCellStates.BLOCK);
        } else if(currCell.getCurrState() == PercolationCellStates.FULL){
            currCell.setNextState(PercolationCellStates.FULL);
        }
        else{
            int numNeighborsFull = 0;
            for(Cell currNeighbor: allNeighbors) {
                if(currNeighbor != null && currNeighbor.getCurrState() == PercolationCellStates.FULL){
                    numNeighborsFull++;
                }
            }
            if(numNeighborsFull > 0){
                currCell.setNextState(PercolationCellStates.FULL);
            }
            else{
                currCell.setNextState(PercolationCellStates.EMPTY);
            }
        }
    }

    protected Cell makeCellOfType(double radius, String shape, int row, int col){
        PercolationCellStates state;
        if(row == 0 && Math.random() < initialFillProbability){
            state = PercolationCellStates.FULL;
        } else {
            state = PercolationCellStates.valueOf(generateState());
        }
        return new Cell(radius, state, shape);
    }

    protected void updateColor(Cell c){
        if(c.getCurrState() == PercolationCellStates.EMPTY){
            c.setColor(EMPTY_COLOR);
        }
        else if(c.getCurrState() == PercolationCellStates.FULL){
            c.setColor(FULL_COLOR);
        }
        else{
            c.setColor(BLOCK_COLOR);
        }
    }
}
