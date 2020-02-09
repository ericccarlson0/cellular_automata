package cellsociety.backend.gridstructures;

import cellsociety.Simulation;
import cellsociety.backend.Cell;

import java.util.ArrayList;
import java.util.List;

public class FireGrid extends GridStructure {
    public static final String GRID_TYPE_STRING = "FIRE_";
    private double catchProb;


    public FireGrid(int size, ArrayList<Double> percents, ArrayList<String> states, int numNeighbors, double catchProb){
        super(size,percents,states,numNeighbors);
        this.catchProb = catchProb;
        this.init();
    }

    @Override
    protected void calcNewStates() {
        for(Cell c: allCells){
            fireSimStateRules(c);
        }
    }

    @Override
    protected Cell makeCellOfType(int row, int col) {
        Simulation.AllStates selectedState = Simulation.AllStates.valueOf(GRID_TYPE_STRING+generateState());
        return new Cell(selectedState);
    }

    /* assumes 8 neighbors are given, only uses the 4 neighbors directly connecting */
    private void fireSimStateRules(Cell currCell) {
        List<Cell> allNeighbors = currCell.getNeighbors();

        if(currCell.getCurrState() == Simulation.AllStates.FIRE_EMPTY || currCell.getCurrState() == Simulation.AllStates.FIRE_FIRE) {
            currCell.setNextState(Simulation.AllStates.FIRE_EMPTY);
        }
        else {
            int index = 1;
            boolean couldCatch = false;
            while(index < allNeighbors.size()){
                if(allNeighbors.get(index) != null && allNeighbors.get(index).getCurrState() == Simulation.AllStates.FIRE_FIRE) {
                    couldCatch = true;
                    break;
                }
                index += 2;
            }
            if(couldCatch && Math.random() < catchProb) {
                currCell.setNextState(Simulation.AllStates.FIRE_FIRE);
            }
            else{
                currCell.setNextState(Simulation.AllStates.FIRE_TREE);
            }
        }
    }
}
