package cellsociety;

import javafx.scene.shape.Rectangle;

public abstract class Cell{
    private Rectangle vis;
    //TODO: use enums for states?
    private String currState;
    private String nextState;
    private Cell[] neighbors;

    public Cell(double width, double height, String currState){
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
}
