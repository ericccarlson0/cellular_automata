package cellsociety.backend.gridstructures;

import cellsociety.backend.Cell;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.awt.*;

import java.util.ArrayList;
import java.util.List;

public class LifeGrid extends GridStructure {
    public static final double[] ALIVE_COLOR = new double[]{0, 0, 0};
    public static final double[] EMPTY_COLOR = new double[]{1, 1, 1};

    enum LifeCellState {
        ALIVE, EMPTY
    }

    public LifeGrid(int rowNum, int colNum, ArrayList<Double> percents, ArrayList<String> states,
                    double radius, String shape, int neighborhoodType) {
        super(rowNum, colNum, percents, states, radius, shape, neighborhoodType);
        this.init(shape);
    }

    protected void calcNewStates(){
        for(Cell c: cellList){
            updateLifeCell(c);
        }
    }

    private void updateLifeCell(Cell currCell) {
        List<Cell> allNeighbors = currCell.getNeighbors();
        int numNeighborsAlive = 0;
        for(Cell currNeighbor: allNeighbors){
            if(currNeighbor.getCurrState() == LifeCellState.ALIVE){
                numNeighborsAlive++;
            }
            if(currCell.getCurrState() == LifeCellState.ALIVE && (numNeighborsAlive == 2 || numNeighborsAlive == 3)) {
                currCell.setNextState(LifeCellState.ALIVE);
            }
            else if(currCell.getCurrState() == LifeCellState.EMPTY && numNeighborsAlive == 3) {
                currCell.setNextState(LifeCellState.ALIVE);
            } else {
                currCell.setNextState(LifeCellState.EMPTY);
            }
        }
    }

    protected Cell makeCell(double radius, String shape, int row, int col){
        LifeCellState state = LifeCellState.valueOf(generateState());
        return new Cell(radius, state, shape);
    }

    protected void updateColor(Cell c){
        if (c.getCurrState() == LifeCellState.ALIVE) {
            c.setColor(ALIVE_COLOR);
        } else {
            c.setColor(EMPTY_COLOR);
        }
    }
}
