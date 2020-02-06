package cellsociety.frontend;

import cellsociety.backend.Cell;
import javafx.scene.layout.GridPane;

public class GridDisplay {
    public static final double CELL_GAP = .1;

    private GridPane display;
    private CellShape cellShape;
    private int size;

    enum CellShape{
        SQUARE
    }

    public GridDisplay(String cellShape,int size){
        this.cellShape = CellShape.valueOf(cellShape);
        this.size = size;
        initializeDisplay();
    }

    private void initializeDisplay() {
        switch(cellShape){
            case SQUARE:
                initSquareCellDisplay();
                break;
        }
    }

    public void addCellToDisplay(int row, int col, Cell c){
        display.add(c.getVisual(), col, row,1,1);
    }

    private void initSquareCellDisplay() {
        display = new GridPane();
        display.setGridLinesVisible(false);
        display.setHgap(CELL_GAP);
        display.setVgap(CELL_GAP);
    }

    public GridPane getDisplay(){
        return display;
    }
}
