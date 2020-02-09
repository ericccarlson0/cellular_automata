package cellsociety.backend.gridstructures;

import cellsociety.Simulation;
import cellsociety.backend.Cell;

import java.util.List;

public class RockPaperScissorsGrid extends GridStructure{
    public static final String GRID_TYPE_STRING = "RPS_";
    private double winThreshold;

    public RockPaperScissorsGrid(int size, List<Double> percents, List<String> states, int numNeighbors, double winThreshold) {
        super(size, percents, states, numNeighbors);
        this.winThreshold = winThreshold;
        this.init();
    }

    @Override
    protected void calcNewStates() {
        for(Cell c: cellList) {
            rpsCellStateRules(c);
        }
    }

    @Override
    protected Cell makeCellOfType(int row, int col) {
        Simulation.AllStates selectedState = Simulation.AllStates.valueOf(GRID_TYPE_STRING+generateState());
        return new Cell(selectedState);
    }

    private void rpsCellStateRules(Cell currCell) {
        Simulation.AllStates winningState = getWinningState(currCell.getCurrState());
        List<Cell> allNeighbors = currCell.getNeighbors();

        int numWin = 0;
        int numTotal = 0;
        for(Cell currNeighbor: allNeighbors) {
            if(currNeighbor != null && currNeighbor.getCurrState() == winningState) {
                numWin++;
                numTotal++;
            }
            else if(currNeighbor != null) {
                numTotal++;
            }
        }

        if(((numWin * 1.0) / numTotal) >= winThreshold) {
            currCell.setNextState(winningState);
        }
        else {
            currCell.setNextState(currCell.getCurrState());
        }
    }

    private Simulation.AllStates getWinningState(Object currState) {
        if(currState == Simulation.AllStates.RPS_PAPER) {
            return Simulation.AllStates.RPS_SCISSORS;
        }
        else if(currState == Simulation.AllStates.RPS_ROCK) {
            return Simulation.AllStates.RPS_PAPER;
        }
        else {
            return Simulation.AllStates.RPS_ROCK;
        }
    }
}
