package cellsociety;

import javafx.scene.layout.GridPane;

public class GridDisplay {
    public static final double CELL_GAP = .1;
    private GridPane display;
    private CellShape cellShape;
    private int size;

    enum CellShape {
        SQUARE, DIAMOND, TRIANGLE, HEXAGON, CIRCLE
    }

    //***
    private double[] calcPolygonLengths(double radius, int n) {
        double[] ret = new double[2];
        ret[0] = 2*radius*Math.sin(Math.PI/n);  // SIDE LENGTH
        ret[1] = radius*Math.cos(Math.PI/n);    // APOTHEM
        return ret;
    }

    private double[] calcTriangleWH(double radius) {
        double[] lengths = calcPolygonLengths(radius, 3);
        double[] ret = new double[2];
        ret[0] = lengths[0];                    // WIDTH
        ret[1] = lengths[0]*(Math.sqrt(3)/2);   // HEIGHT
        return ret;
    }

    private double[] calcHexagonWH(double radius) {
        double[] lengths = calcPolygonLengths(radius, 6);
        double[] ret = new double[2];
        ret[0] = radius*2;                      // WIDTH
        ret[1] = lengths[1]*2;                  // HEIGHT
        return ret;
    }

    private Double[] calcTrianglePoints() {
        return null;
    }

    private Double[] calcHexagonPoints() {
        return null;
    }


    public GridDisplay(String cellShape, int size){
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
