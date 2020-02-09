package cellsociety.backend;

import java.util.List;
import java.util.ArrayList;

public class Cell {
    protected double radius;
    protected double[] colorRGB;
    protected Object currState;
    protected Object nextState;
    protected List<Cell> neighbors;

    public Cell (double radius, Object currState) {
        this.radius = radius;
        this.currState = currState;
        this.nextState = null;
        this.neighbors = new ArrayList<>();
    }

    public void updateState() {
        currState = nextState;
        nextState = null;
    }

    public void addNeighbor(Cell neighbor) {
        neighbors.add(neighbor);
    }

    public void setColor(double[] color) {
        colorRGB[0] = color[0];
        colorRGB[1] = color[1];
        colorRGB[2] = color[2];
    }

    public double[] getColor() {
        double[] ret = new double[]{colorRGB[0], colorRGB[1], colorRGB[2]};
        return ret;
    }

    // Is there any way to reduce the code smells here?

    public Object getCurrState(){ return currState; }

    public void setCurrState(Object state){ currState = state; }

    public void setNextState(Object state){ nextState = state; }

    protected Object getNextState(){ return nextState; }

    public List<Cell> getNeighbors() {
        return neighbors;
    }
}
