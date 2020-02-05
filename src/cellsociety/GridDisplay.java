package cellsociety;

import javafx.scene.layout.GridPane;

public class GridDisplay {
    public static final double CELL_GAP = .1;

    private GridPane display;
    private CellShape cellShape;
    private int size;

    enum CellShape{
        SQUARE
    }

    public GridDisplay(String cellShape,Cell[][] initCellConfig){
        this.cellShape = CellShape.valueOf(cellShape);
        size = initCellConfig.length;
        initializeDisplay(initCellConfig);
    }

    private void initializeDisplay(Cell[][] config) {
        switch(cellShape){
            case SQUARE:
                initSquareCellDisplay(config);
                break;
        }
    }

    private void initSquareCellDisplay(Cell[][] config) {
        display = new GridPane();
        display.setGridLinesVisible(false);
        display.setHgap(CELL_GAP);
        display.setVgap(CELL_GAP);
        for (int row = 0; row < size; row++){
            for (int col = 0; col < size; col++){
                display.add(config[row][col].getVisual(), col, row,1,1);
            }
        }
    }

    public GridPane getDisplay(){
        return display;
    }
}
