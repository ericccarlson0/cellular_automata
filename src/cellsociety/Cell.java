package cellsociety;

import javafx.scene.shape.Rectangle;

public abstract class Cell{
    protected Rectangle vis;
    //TODO: use enums for states - need to figure out how to declare that they are needed and then override diff set of state types in each.
    protected Object currState;
    protected Object nextState;
    protected Cell[] neighbors;

    public Cell(double width, double height, Object currState){
        vis = new Rectangle(width,height);
        this.currState = currState;
        nextState = null;
    }

    public abstract void calcNewState();

    public void updateState() {
        currState = nextState;
        nextState = null;
    }

    public abstract void changeDisplay();

    public Rectangle getVis() {
        return vis;
    }

    public void setNeighbors(Cell[] neighbors) {
        this.neighbors = neighbors;
    }

    public Object getCurrState(){
        return currState;
    }
}
