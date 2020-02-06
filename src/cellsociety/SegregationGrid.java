package cellsociety;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class SegregationGrid extends GridStructure{
    public static final Paint ONE_COLOR = Color.color(1.0, 0.5, 0.5);
    public static final Paint TWO_COLOR = Color.color(0.5, 0.5, 1.0);
    public static final Paint EMPTY_COLOR = Color.color(1.0, 1.0, 1.0);

    private double satisfactionThreshold;
    private HashSet<Cell> allEmpties;

    enum SegregationCellStates {
        EMPTY, ONE, TWO
    }

    public SegregationGrid(int size, ArrayList<Double> percents, ArrayList<String> states, String shape, int numNeighbors, double satisfactionThreshold){
        super(size,percents,states,shape,numNeighbors);
        this.satisfactionThreshold = satisfactionThreshold;
        this.init(shape);
    }

    @Override
    protected void calcNewStates() {
        getAllEmpties();
        for(Cell c: allCells){
            segregationSimStateRules(c);
        }
    }

    @Override
    protected void updateColor(Cell c) {
        if(c.getCurrState() == SegregationCellStates.EMPTY) {
            c.setColor(EMPTY_COLOR);
        }
        else if(c.getCurrState() == SegregationCellStates.ONE) {
            c.setColor(ONE_COLOR);
        }
        else{
            c.setColor(TWO_COLOR);
        }
    }

    @Override
    protected Cell makeCellOfType(double width, double height, String shape, int row, int col) {
        SegregationCellStates selectedState = SegregationCellStates.valueOf(generateState());
        return new Cell(width,height,selectedState,shape);
    }

    private void segregationSimStateRules(Cell currCell) {
        if(currCell.getCurrState() == SegregationCellStates.EMPTY) {
            currCell.setNextState(SegregationCellStates.EMPTY);
        } else if(!isSatisfied(currCell) && !allEmpties.isEmpty()) {
            Cell currEmpty = getRandomEmpty();
            currEmpty.setCurrState(currCell.getCurrState());
            currEmpty.setNextState(currCell.getCurrState());
            allEmpties.remove(currEmpty);
            currCell.setNextState(SegregationCellStates.EMPTY);
        } else if(currCell.getCurrState() != SegregationCellStates.EMPTY) {
            currCell.setNextState(currCell.getCurrState());
        }
    }

    private boolean isSatisfied(Cell currCell) {
        ArrayList<Cell> allNeighbors = currCell.getNeighbors();
        if(currCell.getCurrState() == SegregationCellStates.EMPTY) {
            return true;
        }
        int numSame = 0;
        int numTotal = 0;
        for(Cell currNeighbor: allNeighbors) {
            if(currNeighbor != null && currNeighbor.getCurrState() == currCell.getCurrState()) {
                numSame++;
                numTotal++;
            }
            else if(currNeighbor != null && !(currNeighbor.getCurrState() == SegregationCellStates.EMPTY)) {
                numTotal++;
            }
        }

        return ((numSame * 1.0) / numTotal) >= satisfactionThreshold;
    }

    private Cell getRandomEmpty() {
        Cell[] empties = new Cell[allEmpties.size()];
        allEmpties.toArray(empties);

        int randIndex = (int)(Math.random() * allEmpties.size());
        return empties[randIndex];
    }

    protected void getAllEmpties(){
        HashSet<Cell> allEmpties = new HashSet<Cell>();
        for(Cell c: allCells) {
            if (c.getCurrState() == SegregationCellStates.EMPTY) {
                allEmpties.add(c);
            }
        }
        this.allEmpties = allEmpties;
    }
}
