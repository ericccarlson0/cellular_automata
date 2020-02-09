package cellsociety.backend;

import java.util.List;
import java.util.ArrayList;

public class Cell {
    protected Object currState;
    protected Object nextState;
    protected List<Cell> neighbors;

    public Cell (Object currState) {
        this.currState = currState;
        this.nextState = null;
        this.neighbors = new ArrayList<>();
    }

    public void updateState() {
        currState = nextState;
        nextState = null;
    }

    public void setNeighbors(List<Cell> neighbors) {
        this.neighbors = neighbors;
    }

    public Object getCurrState(){ return currState; }
    public void setCurrState(Object state){ currState = state; }
    public void setNextState(Object state){ nextState = state; }
    public List<Cell> getNeighbors() { return neighbors; }
}
