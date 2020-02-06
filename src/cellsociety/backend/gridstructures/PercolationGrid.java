package cellsociety.backend.gridstructures;

import cellsociety.backend.Cell;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class PercolationGrid extends GridStructure {
    public static final Paint BLOCK_COLOR = Color.color(0.4, 0.2, 0.2);
    public static final Paint FULL_COLOR = Color.color(0.5, 0.75, 1.0);
    public static final Paint EMPTY_COLOR = Color.color(1.0, 1.0, 1.0);

    private double initialFillProbability;

    enum PercolationCellStates {
        BLOCK, EMPTY, FULL
    }

    public PercolationGrid(int size, ArrayList<Double> percents, ArrayList<String> states, String shape, int numNeighbors, double initialFillProbability){
        super(size,percents,states,shape,numNeighbors);
        this.initialFillProbability = initialFillProbability;
        this.init(shape);
    }

    protected void calcNewStates(){
        for(Cell c: allCells){
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

    protected Cell makeCellOfType(double width, double height, String shape, int row, int col){
        PercolationCellStates selectedState;
        if(row == 0 && Math.random() < initialFillProbability){
            selectedState = PercolationCellStates.FULL;
        }
        else{
            selectedState = PercolationCellStates.valueOf(generateState());
        }
        return new Cell(width,height,selectedState,shape);
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
