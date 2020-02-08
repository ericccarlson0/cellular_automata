package cellsociety.backend;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.List;

public class Cell{
    protected Object currState;
    protected Object nextState;
    protected List<Cell> neighbors;

    enum CellShape {
        SQUARE, DIAMOND, CIRCLE
    }

    public Cell (Object currState, String shape) {
        CellShape cm = CellShape.valueOf(shape);
        this.currState = currState;
        this.nextState = null;
    }

    public void updateState() {
        currState = nextState;
        nextState = null;
    }

    public void setNeighbors(List<Cell> neighbors) {
        this.neighbors = neighbors;
    }

    public Object getCurrState(){
        return currState;
    }

    public void setNextState(Object state){
        nextState = state;
    }

    public void setCurrState(Object state){
        currState = state;
    }

    protected Object getNextState(){
        return nextState;
    }

    public List<Cell> getNeighbors() {
        return neighbors;
    }
}
