package cellsociety;

import cellsociety.backend.Cell;
import cellsociety.backend.gridstructures.GridStructure;
import cellsociety.frontend.*;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class Simulation {
    public static final double DISPLAY_HEIGHT = 500;
    public static final double DISPLAY_WIDTH = 500;
    private GridStructure gridStruct;
    private GridDisplay gridDisplay;
    private int rowNum;
    private int colNum;
    private String cellShape;

    public enum AllStates {
        LIFE_ALIVE(Color.BLACK),
        LIFE_EMPTY(Color.WHITE),
        FIRE_TREE(Color.color(0.2, 0.75, 0.2)),
        FIRE_FIRE(Color.color(0.8, 0.2, 0.0)),
        FIRE_EMPTY(Color.color(0.8, 0.8, 0.6)),
        PERCOLATION_FULL(Color.color(0.5, 0.75, 1.0)),
        PERCOLATION_EMPTY(Color.color(1.0, 1.0, 1.0)),
        PERCOLATION_BLOCK(Color.color(0.4, 0.2, 0.2)),
        SEGREGATION_ONE(Color.color(1.0, 0.5, 0.5)),
        SEGREGATION_TWO(Color.color(0.5, 0.5, 1.0)),
        SEGREGATION_EMPTY(Color.color(1.0, 1.0, 1.0)),
        PRED_PREY_FISH(Color.LIGHTCORAL),
        PRED_PREY_SHARK(Color.BLACK),
        PRED_PREY_EMPTY(Color.TURQUOISE),
        RPS_ROCK(Color.color(0.0, 0.0, 0.0)),
        RPS_PAPER(Color.color(1.0, 1.0, 1.0)),
        RPS_SCISSORS(Color.color(0.5, 0.5, 0.5)),
        ANT_EMPTY(Color.WHITE),
        ANT_PHEROMONES(Color.LIGHTGREEN),
        ANT_FOOD(Color.BLUEVIOLET),
        ANT_NEST(Color.ORANGERED),
        ANT_FULL(Color.DARKGREEN);


        private Color stateColor;
        AllStates(Color p) { stateColor = p; }

        public Color getColor() {
            return stateColor;
        }
    }

    public Simulation(GridStructure gs, String shape) {
        gridStruct = gs;
        rowNum = gs.getRowNum();
        colNum = gs.getColNum();
        cellShape = shape;
        initDisplay();
    }

    private void initDisplay() {
        selectInitGrid();
        for (int row=0; row<rowNum; row++) {
            for (int col=0; col<colNum; col++){
                Cell currCell = gridStruct.getCellAtIndex(row, col);
                gridDisplay.addCellToDisplay(row, col, currCell.getCurrState());
            }
        }
    }

    private void selectInitGrid(){
        if (cellShape.equals("DIAMOND")) {
            gridDisplay = new DiamondDisplay(rowNum, colNum);
        } else if (cellShape.equals("TRIANGLE")) {
            gridDisplay = new TriangleDisplay(rowNum, colNum);
        } else if (cellShape.equals("HEXAGON")) {
            gridDisplay = new HexagonDisplay(rowNum, colNum);
        } else if (cellShape.equals("CIRCLE")) {
            gridDisplay = new CircleDisplay(rowNum, colNum);
        } else {
            gridDisplay = new SquareDisplay(rowNum, colNum);
        }
    }

    public Region getDisplay() {
        return gridDisplay.getDisplay();
    }

    public void step() {
        gridStruct.step();
        updateDisplay();
    }

    private void updateDisplay() {
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                gridDisplay.updateDisplayAtCell(row, col, gridStruct.getStateAtCell(row, col));
            }
        }
    }
}
