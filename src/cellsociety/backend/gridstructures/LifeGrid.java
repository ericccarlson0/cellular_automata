package cellsociety.backend.gridstructures;

import cellsociety.Simulation;
import cellsociety.backend.Cell;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.awt.*;

import java.util.ArrayList;
import java.util.List;

public class LifeGrid extends GridStructure{
    public static final String GRID_TYPE_STRING = "LIFE_";

    public LifeGrid(int rowNum, int colNum, ArrayList<Double> percents, ArrayList<String> states, boolean isTorus, int neighborhoodType){
        super(rowNum, colNum, percents, states, neighborhoodType, isTorus);
        this.init();
    }

    protected Cell createCell(int row, int col){
        Simulation.AllStates selectedState = Simulation.AllStates.valueOf(GRID_TYPE_STRING + generateState());
        return new Cell(selectedState);
    }

    protected void calcNewStates(){
        for (Cell c: cellList){
            updateLifeCell(c);
        }
    }

    private void updateLifeCell(Cell currCell) {
        List<Cell> allNeighbors = currCell.getNeighbors();
        int numNeighborsAlive = 0;
        for(Cell currNeighbor: allNeighbors){
            if(currNeighbor.getCurrState() == Simulation.AllStates.LIFE_ALIVE){
                numNeighborsAlive++;
            }
            if(currCell.getCurrState() == Simulation.AllStates.LIFE_ALIVE && (numNeighborsAlive == 2 || numNeighborsAlive == 3)) {
                currCell.setNextState(Simulation.AllStates.LIFE_ALIVE);
            }
            else if(currCell.getCurrState() == Simulation.AllStates.LIFE_EMPTY && numNeighborsAlive == 3) {
                currCell.setNextState(Simulation.AllStates.LIFE_ALIVE);
            } else {
                currCell.setNextState(Simulation.AllStates.LIFE_EMPTY);
            }
        }
    }
}
