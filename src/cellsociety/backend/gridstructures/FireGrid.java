package cellsociety.backend.gridstructures;

import cellsociety.Simulation;
import cellsociety.backend.Cell;

import java.util.ArrayList;
import java.util.List;

public class FireGrid extends GridStructure {
    public static final String GRID_TYPE_STRING = "FIRE_";
    private double catchProb;

    public FireGrid(int rowNum, int colNum, ArrayList<Double> percents, ArrayList<String> states,
                    boolean isTorus, int neighborhoodType, double catchProb){
        super(rowNum, colNum, percents, states, neighborhoodType, isTorus);
        this.catchProb = catchProb;
        this.init();
    }

    protected Cell createCell(int row, int col) {
            Simulation.AllStates state = Simulation.AllStates.valueOf(GRID_TYPE_STRING + generateState());
            return new Cell(state);
    }

    protected void calcNewStates() {
        for (Cell c : cellList) {
            updateFireCell(c);
        }
    }

    private void updateFireCell(Cell currCell) {
        List<Cell> allNeighbors = currCell.getNeighbors();

        if (currCell.getCurrState() == Simulation.AllStates.FIRE_EMPTY || currCell.getCurrState() == Simulation.AllStates.FIRE_FIRE) {
            currCell.setNextState(Simulation.AllStates.FIRE_EMPTY);
        } else {
            boolean couldCatch = false;
            for(Cell c: allNeighbors){
                if(c.getCurrState() == Simulation.AllStates.FIRE_FIRE)
                    couldCatch = true;
            }
            if(couldCatch && Math.random() < catchProb) {
                currCell.setNextState(Simulation.AllStates.FIRE_FIRE);
            } else {
                currCell.setNextState(Simulation.AllStates.FIRE_TREE);
            }
        }
    }
}
