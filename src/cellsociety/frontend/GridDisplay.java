package cellsociety.frontend;

import cellsociety.backend.Cell;
import cellsociety.backend.gridstructures.GridStructure;
import javafx.scene.layout.Region;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Color;

public abstract class GridDisplay {
    // public static final double CELL_GAP = 0;
    public static final double SQRT_THREE = Math.sqrt(3);
    public static final double SQRT_TWO = Math.sqrt(2);

    private Region gridDisplay;
    private Cell[][] cellObjects;
    private Shape[][] cellShapes;
    private double totalWidth;
    private double totalHeight;
    private double cellWidth;
    private double cellHeight;
    private int rowNum;
    private int colNum;

    public GridDisplay(int rowNum, int colNum, double totalWidth, double totalHeight) {
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.totalWidth = totalWidth;
        this.totalHeight = totalHeight;
        initDisplay(totalWidth, totalHeight);
    }

    private void initDisplay(double width, double height) {
        gridDisplay = new Region();
        gridDisplay.setPrefSize(width, height); //*** Does this work?
        // renderCellMatrix(); //*** Do we need this?
    }

    public void setupDisplayFromStructure(GridStructure struct) {
        for(int row=0; row<rowNum; row++){
            for(int col=0; col<colNum; col++){

                double radius = struct.getRadius();
                Cell currCell = struct.getCellAtIndex(row, col);
                Shape currShape = createShape(radius);

                double[] colorRGB = currCell.getColor();
                Color color = Color.color(colorRGB[0], colorRGB[1], colorRGB[2]);
                currShape.setFill(color);

                cellObjects[row][col] = currCell;
                cellShapes[row][col] = currShape;
            }
        }
    }

    public abstract Shape createShape(double radius);

    private void updateCellShapes() {
        for(int row=0; row<rowNum; row++) {
            for(int col=0; col<colNum; col++){
                Cell currCell = cellObjects[row][col];
                Shape currShape = cellShapes[row][col];
                double[] colorRGB = currCell.getColor();
                Color color = Color.color(colorRGB[0], colorRGB[1], colorRGB[2]);
                currShape.setFill(color);
            }
        }
    }

    //***
    double[] calcPolygonLengths(double radius, int n) {
        double[] ret = new double[2];
        ret[0] = 2*radius*Math.sin(Math.PI/n);  // SIDE_LENGTH
        ret[1] = radius*Math.cos(Math.PI/n);    // APOTHEM
        return ret;
    }

    double[] calcTriangleWH(double radius) {
        double[] lengths = calcPolygonLengths(radius, 3);
        double[] ret = new double[2];
        ret[0] = lengths[0];                    // WIDTH = SIDE_LENGTH
        ret[1] = lengths[0]*(SQRT_THREE/2);     // HEIGHT
        return ret;
    }

    double[] calcHexagonWH(double radius) {
        double[] lengths = calcPolygonLengths(radius, 6);
        double[] ret = new double[2];
        ret[0] = radius*2;                      // WIDTH = 2*CIRCUMRADIUS (radius)
        ret[1] = 2*lengths[1];                  // HEIGHT = 2*INRADIUS (apothem)
        return ret;
    }

    double[] convertPolarCoords(double radius, double degrees) {
        double x = radius * Math.cos(degrees);
        double y = radius * Math.sin(degrees);
        return new double[]{x, y};
    }

    // Needed because a triangle cannot be sideways, as it would be rendered
    // by calcPolygonPoints.
    Double[] calcTrianglePoints(double[] center, double radius) {
        double[] diff1 = convertPolarCoords(radius, Math.PI/2);
        double[] diff2 = convertPolarCoords(radius, 7*Math.PI/6);
        double[] diff3 = convertPolarCoords(radius, 11*Math.PI/6);
        return new Double[]{diff1[0]+center[0], diff1[1]+center[1],
                diff2[0]+center[0], diff2[1]+center[1],
                diff3[0]+center[0], diff3[1]+center[1]};
    }

    Double[] calcPolygonPoints(double[] center, double radius, int n) {
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
    //***
    public Polygon renderHexagon(double[] center, double radius) {
        Polygon ret = new Polygon();
        Double[] points = calcPolygonPoints(center, radius, 6);
        ret.getPoints().addAll(points);
        return ret;
    }

    public Region getDisplay(){
        return gridDisplay;
    }
}
