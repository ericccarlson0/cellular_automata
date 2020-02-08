package cellsociety.backend.gridstructures;

import cellsociety.Simulation;
import cellsociety.backend.Cell;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class PercolationGrid extends GridStructure {

    public static final String GRID_TYPE_STRING = "PERCOLATION_";
    private double initialFillProbability;

    public PercolationGrid(int size, ArrayList<Double> percents, ArrayList<String> states, int numNeighbors, double initialFillProbability){
        super(size,percents,states,numNeighbors);
        this.initialFillProbability = initialFillProbability;
    }

    protected void calcNewStates(){
        for(Cell c: allCells){
            percolationSimStateRules(c);
        }
    }

    private void percolationSimStateRules(Cell currCell){
        List<Cell> allNeighbors = currCell.getNeighbors();

        if(currCell.getCurrState() == Simulation.AllStates.PERCOLATION_BLOCKED){
            currCell.setNextState(Simulation.AllStates.PERCOLATION_BLOCKED);
        } else if(currCell.getCurrState() == Simulation.AllStates.PERCOLATION_FULL){
            currCell.setNextState(Simulation.AllStates.PERCOLATION_FULL);
        }
        else{
            int numNeighborsFull = 0;
            for(Cell currNeighbor: allNeighbors) {
                if(currNeighbor != null && currNeighbor.getCurrState() == Simulation.AllStates.PERCOLATION_FULL){
                    numNeighborsFull++;
                }
            }
            if(numNeighborsFull > 0){
                currCell.setNextState(Simulation.AllStates.PERCOLATION_FULL);
            }
            else{
                currCell.setNextState(Simulation.AllStates.PERCOLATION_EMPTY);
            }
        }
    }

    protected Cell makeCellOfType(String shape, int row, int col){
        Simulation.AllStates selectedState;
        if(row == 0 && Math.random() < initialFillProbability){
            selectedState = Simulation.AllStates.PERCOLATION_FULL;
        }
        else{
            selectedState = Simulation.AllStates.valueOf(GRID_TYPE_STRING+generateState());
        }
        return new Cell(selectedState,shape);
    }
}
