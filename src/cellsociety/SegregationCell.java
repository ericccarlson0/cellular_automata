package cellsociety;

import java.util.ArrayList;
import java.util.HashSet;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class SegregationCell extends Cell {
    public static final Paint ONE_COLOR = Color.color(1.0, 0.5, 0.5);
    public static final Paint TWO_COLOR = Color.color(0.5, 0.5, 1.0);
    public static final Paint EMPTY_COLOR = Color.color(1.0, 1.0, 1.0);

    private double threshold;

    enum SegregationCellState {
        EMPTY, ONE, TWO
    }

    public SegregationCell(double width, double height, String currState, String shape, double threshold) {
        super(width, height, SegregationCellState.valueOf(currState), shape);
        this.threshold = threshold;
        changeDisplay();
    }

    public void calcNewState(ArrayList<HashSet<Cell>> emptySpaces) {
        if (currState == SegregationCellState.EMPTY && nextState == null) {
            nextState = SegregationCellState.EMPTY;
            emptySpaces.get(1).add(this);
        } else if (!isSatisfied() && !(emptySpaces.get(0).isEmpty())) {
            Cell currCell = getRandomEmpty(emptySpaces.get(0));
            emptySpaces.get(0).remove(currCell);
            emptySpaces.get(1).remove(currCell);
            currCell.setNextState(currState);
            nextState = SegregationCellState.EMPTY;
            emptySpaces.get(1).add(this);
        } else if (currState != SegregationCellState.EMPTY) {
            nextState = currState;
        }
    }

    public void changeDisplay() {
        if(currState == SegregationCellState.EMPTY) {
            visual.setFill(EMPTY_COLOR);
        } else if(currState == SegregationCellState.ONE) {
            visual.setFill(ONE_COLOR);
        } else {
            visual.setFill(TWO_COLOR);
        }
    }

    private boolean isSatisfied() {
        if (currState == SegregationCellState.EMPTY){
            return true;
        }
        int numSame = 0;
        int numTotal = 0;
        for(Cell currNeighbor : neighbors){
            if(currNeighbor != null && currNeighbor.getCurrState() == currState){
                numSame += 1;
                numTotal += 1;
            } else if (currNeighbor != null && !(currNeighbor.getCurrState() == SegregationCellState.EMPTY)) {
                numTotal += 1;
            }
        }
        // System.out.println((numSame * 1.0) / numTotal);
        return ((numSame * 1.0) / numTotal) >= threshold;
    }

    private Cell getRandomEmpty(HashSet<Cell> currEmpties) {
        Cell[] empties = new Cell[currEmpties.size()];
        currEmpties.toArray(empties);

        int randIndex = (int)(Math.random() * currEmpties.size());
        return empties[randIndex];
    }
}
