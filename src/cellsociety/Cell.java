package cellsociety;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class Cell {
    protected Shape visual;
    protected Object currState;
    protected Object nextState;
    protected Cell[] neighbors;

    public Cell (double width, double height, Object currState) {
        // visual = new Rectangle(width, height);
        visual = new Circle(width/2);
        this.currState = currState;
        this.nextState = null;
    }

    public abstract void calcNewState (ArrayList<HashSet<Cell>> emptySpots);

    public void updateState() {
        currState = nextState;
        nextState = null;
    }

    public abstract void changeDisplay();

    public Shape getVisual() {
        return visual;
    }

    public void setNeighbors(Cell[] neighbors) {
        this.neighbors = neighbors;
    }

    public Object getCurrState(){
        return currState;
    }

    public void setNextState(Object state){
        nextState = state;
    }
}
