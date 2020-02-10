package cellsociety.backend.gridstructures;

import cellsociety.Simulation;
import cellsociety.backend.Cell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SegregationGrid extends GridStructure{
    public static final String GRID_TYPE_STRING = "SEGREGATION_";
    private double satisfactionThreshold;
    private HashSet<Cell> allEmpties;

    public SegregationGrid(int rowNum, int colNum, ArrayList<Double> percents, ArrayList<String> states, boolean isTorus,
                           int neighborhoodType, double satisfactionThreshold) {
        super(rowNum, colNum, percents, states, neighborhoodType, isTorus);
        this.satisfactionThreshold = satisfactionThreshold;
        this.init();
    }

    protected Cell createCell(int row, int col) {
        Simulation.AllStates state = Simulation.AllStates.valueOf(GRID_TYPE_STRING+generateState());
        return new Cell(state);
    }

    protected void calcNewStates() {
        getAllEmpties();
        for(Cell c: cellList){
            updateSegregationCell(c);
        }
    }

    private void updateSegregationCell(Cell currCell) {
        if (currCell.getCurrState() == Simulation.AllStates.SEGREGATION_EMPTY) {
            currCell.setNextState(Simulation.AllStates.SEGREGATION_EMPTY);
        } else if (!isSatisfied(currCell) && !allEmpties.isEmpty()) {
            Cell currEmpty = getRandomEmpty();
            currEmpty.setCurrState(currCell.getCurrState());
            currEmpty.setNextState(currCell.getCurrState());
            allEmpties.remove(currEmpty);
            currCell.setNextState(Simulation.AllStates.SEGREGATION_EMPTY);
        } else if (currCell.getCurrState() != Simulation.AllStates.SEGREGATION_EMPTY) {
            currCell.setNextState(currCell.getCurrState());
        }
    }

    private boolean isSatisfied(Cell currCell) {
        List<Cell> allNeighbors = currCell.getNeighbors();
        if (currCell.getCurrState() == Simulation.AllStates.SEGREGATION_EMPTY) {
            return true;
        }
        int numSame = 0;
        int numTotal = 0;
        for (Cell currNeighbor: allNeighbors) {
            if(currNeighbor != null && currNeighbor.getCurrState() == currCell.getCurrState()) {
                numSame++;
                numTotal++;
            }
            else if (currNeighbor != null && !(currNeighbor.getCurrState() == Simulation.AllStates.SEGREGATION_EMPTY)) {
                numTotal++;
            }
        }
        return ((numSame*1.0) / numTotal) >= satisfactionThreshold;
    }

    private Cell getRandomEmpty() {
        Cell[] empties = new Cell[allEmpties.size()];
        allEmpties.toArray(empties);

        int randIndex = (int)(Math.random() * allEmpties.size());
        return empties[randIndex];
    }

    protected void getAllEmpties(){
        HashSet<Cell> allEmpties = new HashSet<Cell>();
        for(Cell c: cellList) {
            if (c.getCurrState() == Simulation.AllStates.SEGREGATION_EMPTY) {
                allEmpties.add(c);
            }
        }
        this.allEmpties = allEmpties;
    }
}
