package cellsociety.backend.gridstructures;

import cellsociety.Simulation;
import cellsociety.backend.Cell;

import java.util.ArrayList;
import java.util.List;

public class PercolationGrid extends GridStructure {

    public static final String GRID_TYPE_STRING = "PERCOLATION_";
    private double initialFillProbability;

    public PercolationGrid (int rowNum, int colNum, ArrayList<Double> percents, ArrayList<String> states, boolean isTorus,
                            int neighborhoodType, double initialFillProbability){
        super(rowNum, colNum, percents, states, neighborhoodType,isTorus);
        this.initialFillProbability = initialFillProbability;
        this.init();
    }

    protected Cell createCell(int row, int col){
        Simulation.AllStates state;
        if (row == 0 && Math.random() < initialFillProbability) {
            state = Simulation.AllStates.PERCOLATION_FULL;
        } else {
            state = Simulation.AllStates.valueOf(GRID_TYPE_STRING+generateState());
        }
        return new Cell(state);
    }

    protected void calcNewStates(){
        for (Cell c: cellList){
            updatePercolationCell(c);
        }
    }

    private void updatePercolationCell(Cell currCell){
        List<Cell> allNeighbors = currCell.getNeighbors();
        if (currCell.getCurrState() == Simulation.AllStates.PERCOLATION_BLOCK){
            currCell.setNextState(Simulation.AllStates.PERCOLATION_BLOCK);
        } else if (currCell.getCurrState() == Simulation.AllStates.PERCOLATION_FULL){
            currCell.setNextState(Simulation.AllStates.PERCOLATION_FULL);
        } else {
            int numNeighborsFull = 0;
            for (Cell currNeighbor: allNeighbors) {
                if (currNeighbor != null && currNeighbor.getCurrState() == Simulation.AllStates.PERCOLATION_FULL){
                    numNeighborsFull++;
                }
            }
            if (numNeighborsFull > 0){
                currCell.setNextState(Simulation.AllStates.PERCOLATION_FULL);
            } else {
                currCell.setNextState(Simulation.AllStates.PERCOLATION_EMPTY);
            }
        }
    }
}
