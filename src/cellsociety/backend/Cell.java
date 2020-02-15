package cellsociety.backend;

import java.util.List;
import java.util.ArrayList;

/**
 * Class that represents the Cell object and holds all the different necessary information for each cell required for running simulation
 * Also allows access to neighbors and updating of cells states.
 */
public class Cell {
    protected Object currState;
    protected Object nextState;
    protected List<Cell> neighbors;

    /**
     * Constructor to create a new cell object.
     * @param currState Object that is Simulation.AllState types that represents the initial state of the Cell
     */
    public Cell (Object currState) {
        this.currState = currState;
        this.nextState = null;
        this.neighbors = new ArrayList<>();
    }

    /**
     * allows updating of the current state to the stored next state, and resets the next state.
     */
    public void updateState() {
        currState = nextState;
        nextState = null;
    }

    /**
     * allows the setting of this cells neighbors, which is called shortly after the cell is created in the GridStructure constructor.
     * @param neighbors List of type Cell that represents neighboring cells, varies depending on neighborhood type
     */
    public void setNeighbors(List<Cell> neighbors) {
        this.neighbors = neighbors;
    }

    /**
     * allows access to this cells current state
     * @return Object that is an enumerator of Simulation.AllStates representing current state
     */
    public Object getCurrState(){ return currState; }

    /**
     * stores the current state of the cell to the specified state.
     * @param state Object an enumerator of Simulation.AllStates is passed in and stored as an object here.
     */
    public void setCurrState(Object state){ currState = state; }

    /**
     * stores the next state of the cell to the specified state
     * @param state Object an enumerator of Simulation.AllStates is passed in and stored as an object here.
     */
    public void setNextState(Object state){ nextState = state; }

    /**
     * used in different gridStructure simulation specific classes to iterate over neighbors and perform some kind of calculation.
     * @return List of type Cell that corresponds to the neighboring cells of this current cell object
     */
    public List<Cell> getNeighbors() { return neighbors; }
}
