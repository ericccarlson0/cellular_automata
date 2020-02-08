package cellsociety.frontend;

import cellsociety.backend.Cell;
import cellsociety.backend.gridstructures.GridStructure;
import javafx.scene.layout.Region;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.awt.*;

public class GridDisplay {
    public static final double CELL_GAP = .1;
    public static final double SQRT_THREE = Math.sqrt(3);
    public static final double SQRT_TWO = Math.sqrt(2);

    private Region gridDisplay;
    private CellShape cellShape;
    private Cell[][] cellObjects;
    private Shape[][] cellShapes;
    private double totalWidth;
    private double totalHeight;
    private double cellWidth;
    private double cellHeight;
    private int rowNum;
    private int colNum;

    enum CellShape {
        SQUARE, DIAMOND, TRIANGLE, HEXAGON, CIRCLE
    }

    public GridDisplay(String shapeString, int rowNum, int colNum, double totalWidth, double totalHeight) {
        this.cellShape = CellShape.valueOf(shapeString); //***
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.totalWidth = totalWidth;
        this.totalHeight = totalHeight;
        initDisplay(totalWidth, totalHeight);
    }

    private void initDisplay(double width, double height) {
        gridDisplay = new Region();
        gridDisplay.setPrefSize(width, height);
        switch(cellShape){
            case SQUARE:
                initSquareCellDisplay();
                break;
            case DIAMOND:
                initDiamondCellDisplay();
                break;
            case TRIANGLE:
                initTriCellDisplay();
                break;
            case HEXAGON:
                initHexCellDisplay();
                break;
            case CIRCLE:
                initCircleCellDisplay();
        }
    }

    public void setupFromGridStruct(GridStructure struct) {
        for(int row=0; row<rowNum; row++){
            for(int col=0; col<colNum; col++){
                this.cellObjects[row][col] = struct.getCellAtIndex(row, col);
            }
        }
        updateCellShapes();
    }

    private void updateCellShapes() {
        for(int row=0; row<rowNum; row++) {
            for(int col=0; col<colNum; col++){
                Cell currCell = cellObjects[row][col];
                Shape currShape = cellShapes[row][col];
                double[] colorRGB = currCell.getColor();
                Color shapeColor = Color.color(colorRGB[0], colorRGB[1], colorRGB[2]);
                currShape.setFill(shapeColor);
            }
        }
    }

    //***
    private double[] calcPolygonLengths(double radius, int n) {
        double[] ret = new double[2];
        ret[0] = 2*radius*Math.sin(Math.PI/n);  // SIDE_LENGTH
        ret[1] = radius*Math.cos(Math.PI/n);    // APOTHEM
        return ret;
    }

    private double[] calcTriangleWH(double radius) {
        double[] lengths = calcPolygonLengths(radius, 3);
        double[] ret = new double[2];
        ret[0] = lengths[0];                    // WIDTH = SIDE_LENGTH
        ret[1] = lengths[0]*(SQRT_THREE/2);     // HEIGHT
        return ret;
    }

    private double[] calcHexagonWH(double radius) {
        double[] lengths = calcPolygonLengths(radius, 6);
        double[] ret = new double[2];
        ret[0] = radius*2;                      // WIDTH = 2*CIRCUMRADIUS (radius)
        ret[1] = 2*lengths[1];                  // HEIGHT = 2*INRADIUS (apothem)
        return ret;
    }

    private double[] convertPolarCoords(double radius, double degrees) {
        double x = radius * Math.cos(degrees);
        double y = radius * Math.sin(degrees);
        return new double[]{x, y};
    }

    // Needed because a triangle cannot be sideways, as it would be rendered
    // by calcPolygonPoints.
    private Double[] calcTrianglePoints(double[] center, double radius) {
        double[] diff1 = convertPolarCoords(radius, Math.PI/2);
        double[] diff2 = convertPolarCoords(radius, 7*Math.PI/6);
        double[] diff3 = convertPolarCoords(radius, 11*Math.PI/6);
        return new Double[]{diff1[0]+center[0], diff1[1]+center[1],
                diff2[0]+center[0], diff2[1]+center[1],
                diff3[0]+center[0], diff3[1]+center[1]};
    }

    private Double[] calcPolygonPoints(double[] center, double radius, int n) {
        Double[] ret = new Double[2*n];
        double cx = center[0];
        double cy = center[1];
        double degreeIncrement = 2*Math.PI/n;
        for (int i=0; i<n; i++) {
            double[] diff = convertPolarCoords(radius, i*degreeIncrement);
            ret[2*i] = diff[0] + cx;
            ret[2*i+1] = diff[1] + cy;
        }
        return ret;
    }

    //***
    public Polygon renderTriangle(double[] center, double radius) {
        Polygon ret = new Polygon();
        Double[] points = calcTrianglePoints(center, radius);
        ret.getPoints().addAll(points);
        return ret;
    }

    public Polygon renderHexagon(double[] center, double radius) {
        Polygon ret = new Polygon();
        Double[] points = calcPolygonPoints(center, radius, 6);
        ret.getPoints().addAll(points);
        return ret;
    }

    private void updateColor(int row, int col) { //***

    }

    public void addCellToDisplay(int row, int col, Cell c){
        // gridDisplay.add(c.getVisual(), col, row,1,1);
    }

    private void initSquareCellDisplay() {

    }

    private void initDiamondCellDisplay() {

    }

    private void initTriCellDisplay() {

    }

    private void initHexCellDisplay() {

    }

    private void initCircleCellDisplay() {

    }

    public Region getDisplay(){
        return gridDisplay;
    }
}
