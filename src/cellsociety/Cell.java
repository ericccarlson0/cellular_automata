package cellsociety;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import java.util.ArrayList;

public class Cell{
    protected Shape visual;
    protected Object currState;
    protected Object nextState;
    protected ArrayList<Cell> neighbors;

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

    public void updateState() {
        currState = nextState;
        nextState = null;
    }

    public void setColor(Paint p){
        visual.setFill(p);
    }

    public Shape getVisual() {
        return visual;
    }

    public void setNeighbors(ArrayList<Cell> neighbors) {
        this.neighbors = neighbors;
    }

    public Object getCurrState(){
        return currState;
    }

    public void setNextState(Object state){
        nextState = state;
    }

    protected Object getNextState(){
        return nextState;
    }

    public ArrayList<Cell> getNeighbors() {
        return neighbors;
    }
}
