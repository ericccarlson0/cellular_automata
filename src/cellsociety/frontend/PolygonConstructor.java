package cellsociety.frontend;

public class PolygonConstructor {
    public static final double SQRT_THREE = Math.sqrt(3);
    public static final double SQRT_TWO = Math.sqrt(2);
    public PolygonConstructor(){ }

    double[] calcPolygonLengths(double radius, int n) {
        double[] ret = new double[2];
        ret[0] = 2*radius*Math.sin(Math.PI/n);  // SIDE_LENGTH
        ret[1] = radius*Math.cos(Math.PI/n);    // APOTHEM (INRADIUS)
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

    // Needed because a triangle cannot be sideways -- as it would be rendered
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
            double[] diff = convertPolarCoords(radius, i * degreeIncrement);
            ret[2 * i] = diff[0] + cx;
            ret[2 * i + 1] = diff[1] + cy;
        }
        return ret;
    }
}
