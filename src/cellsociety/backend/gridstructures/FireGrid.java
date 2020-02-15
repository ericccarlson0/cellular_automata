package cellsociety.backend.gridstructures;

import cellsociety.Simulation;
import cellsociety.backend.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a grid of the Fire Simulation type
 * Subclass of GridStructure abstract class
 */
public class FireGrid extends GridStructure {
    public static final String GRID_TYPE_STRING = "FIRE_";
    private double catchProb;

    /**
     * Constructor that creates a new GridStructure object based on passed in values that are obtained from UI or XML file.
     * Specifically creates a gridStructure that follows rules of fireSimulation in its functionality.
     * @param rowNum int number of rows this simulation grid should have
     * @param colNum int number of columns this simulation grid should have
     * @param percents List of doubles that represent the percents of each type of state
     * @param states List of Strings that represent the states, which correspond by index to the percents
     * @param neighborhoodType int that represents which neighborhood type should be created for this simulation
     * @param isTorus boolean which specifies if simulation is a torus simulation
     * @param catchProb double representing the probability that a tree with a neighbor on fire will catch on fire.
     */
    public FireGrid(int rowNum, int colNum, ArrayList<Double> percents, ArrayList<String> states,
                    boolean isTorus, int neighborhoodType, double catchProb){
        super(rowNum, colNum, percents, states, neighborhoodType, isTorus);
        this.catchProb = catchProb;
        this.init();
    }

    /**
     * Implementation of abstract method in GridStructure that specifies how to create a cell for this simulation type
     * @param row int row number for where the cell being created is in the grid
     * @param col int col number for where the cell being created is in the grid
     * @return Cell that is created based on provided information
     */
    protected Cell createCell(int row, int col) {
            Simulation.AllStates state = Simulation.AllStates.valueOf(GRID_TYPE_STRING + generateState());
            return new Cell(state);
    }

    /**
     * for every cell in the grid structure, it updates each cell based on the rules of this specific simulation type.
     * Implementation of required abstract method from gridstructure class.
     */
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
