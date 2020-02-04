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

    enum CellShape {
        SQUARE, DIAMOND, CIRCLE
    }

    public Cell (double width, double height, Object currState, String shape) {
        CellShape cm = CellShape.valueOf(shape);
        visual = new Rectangle(width, height);
        if (cm == CellShape.DIAMOND) {
            visual.setRotate(45);
        } else if (cm == CellShape.CIRCLE) {
            visual = new Circle(width / 2);
        }
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

    protected Object getCurrState(){
        return currState;
    }

    protected void setNextState(Object state){
        nextState = state;
    }

    protected Object getNextState(){
        return nextState;
    }
}
