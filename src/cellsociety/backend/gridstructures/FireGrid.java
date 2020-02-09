package cellsociety.backend.gridstructures;

import cellsociety.Simulation;
import cellsociety.backend.Cell;

import java.util.ArrayList;
import java.util.List;

public class FireGrid extends GridStructure {
    public static final String GRID_TYPE_STRING = "FIRE_";
    private double catchProb;

    public FireGrid(int rowNum, int colNum, ArrayList<Double> percents, ArrayList<String> states,
                    int neighborhoodType, double catchProb){
        super(rowNum, colNum, percents, states, neighborhoodType);
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

    /* assumes 8 neighbors are given, only uses the 4 neighbors directly connecting */
    private void updateFireCell(Cell currCell) {
        List<Cell> allNeighbors = currCell.getNeighbors();

        if (currCell.getCurrState() == Simulation.AllStates.FIRE_EMPTY ||
                currCell.getCurrState() == Simulation.AllStates.FIRE_FIRE) {
            currCell.setNextState(Simulation.AllStates.FIRE_EMPTY);
        } else {
            int index = 1;
            boolean couldCatch = false;
            while (index < allNeighbors.size()){
                if (allNeighbors.get(index) != null &&
                        allNeighbors.get(index).getCurrState() == Simulation.AllStates.FIRE_FIRE) {
                    couldCatch = true;
                    break;
                }
                index += 2;
            }
            if(couldCatch && Math.random() < catchProb) {
                currCell.setNextState(Simulation.AllStates.FIRE_FIRE);
            } else {
                currCell.setNextState(Simulation.AllStates.FIRE_TREE);
            }
        }
    }
}
