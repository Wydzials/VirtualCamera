package pl.wydzials.virtualcamera;

import pl.wydzials.virtualcamera.model.Face;
import pl.wydzials.virtualcamera.model.Point;

import java.util.ArrayList;
import java.util.List;

public class Obscure {

    public static void main(String[] args) {
        List<Point> pointArr = new ArrayList<>();
        pointArr.add(new Point(-6, 3, -2));
        pointArr.add(new Point(-2, 7, 4));
        pointArr.add(new Point(1, -6, 1));

        double[] planeIndexes = calcThePlane(pointArr);
        Point pointD = new Point(1, -5, 1);
        Point pointE = new Point(2, -5, 1);
//        Point pointE = new Point(-6.29,1.07,-3.2);

        int sideDecision = areTwoPointsSameSide(pointD, pointE, planeIndexes);
        if (sideDecision == -1)
            System.out.println("Są po przeciwnych stronach");
        else if (sideDecision == 1)
            System.out.println("Są po tej samej stronie");
        else
            System.out.println("Jeden z punktów leży na płaszczyźnie");

    }

    public static double[] calcThePlane(List<Point> pointArr) {
        double[] planeIndexes = new double[4];
        for (int i = 0; i < 4; i++) {
            double[][] dmatrix = threePointsToMatrix(pointArr, i);
            planeIndexes[i] = calcDeterminant(dmatrix);
            if (i % 2 == 1)
                planeIndexes[i] *= -1;
        }

        return planeIndexes;
    }

    private static double[][] threePointsToMatrix(List<Point> pointArr, int numOfDeterminant) {
        double[][] dmatrix = new double[3][3];
        for (int i = 0; i < 3; i++) {
            Point currentPoint = pointArr.get(i);
            double[] currentRow = {currentPoint.x, currentPoint.y, currentPoint.z, 1};
            int currentCol = 0;
            for (int j = 0; j < 4; j++) {
                if (j == numOfDeterminant)
                    continue;
                dmatrix[i][currentCol] = currentRow[j];
                currentCol++;
            }
        }
        return dmatrix;
    }

    private static double calcDeterminant(double[][] dmatrix) {
        double x = (dmatrix[0][0] * (dmatrix[1][1] * dmatrix[2][2] - dmatrix[1][2] * dmatrix[2][1]));
        double y = (dmatrix[0][1] * (dmatrix[1][0] * dmatrix[2][2] - dmatrix[1][2] * dmatrix[2][0]));
        double z = (dmatrix[0][2] * (dmatrix[1][0] * dmatrix[2][1] - dmatrix[1][1] * dmatrix[2][0]));
        return x - y + z;
    }

    public static int areTwoPointsSameSide(Point firstPoint, Point secPoint, double[] planeIndexes) {
        double firstValue = firstPoint.x * planeIndexes[0] + firstPoint.y * planeIndexes[1] + firstPoint.z * planeIndexes[2] + planeIndexes[3];
        double secValue = secPoint.x * planeIndexes[0] + secPoint.y * planeIndexes[1] + secPoint.z * planeIndexes[2] + planeIndexes[3];

        if (Math.abs(firstValue) < 10 || Math.abs(secValue) < 10)
            return 0;
        if (firstValue * secValue > 0)
            return 1;
        return -1;
    }

    public static Face[] divideFace(Point[] oneSidePoints, Point[] secSidePoints, double[] planeIndexes) {
        Point[] orderedPoints = determinePointPairs(oneSidePoints, secSidePoints);
        Point firstPoint = findPoint(orderedPoints[0], orderedPoints[1], planeIndexes);
        Point secPoint = findPoint(orderedPoints[2], orderedPoints[3], planeIndexes);

        Face firstFace = new Face(oneSidePoints[0], oneSidePoints[1], firstPoint, secPoint);
        Face secFace = new Face(secSidePoints[0], secSidePoints[1], firstPoint, secPoint);
        return new Face[]{firstFace, secFace};
    }

    private static Point findPoint(Point firstPoint, Point secPoint, double[] planeIndexes){
        double[] vector = calculateVector(firstPoint, secPoint);
        double variableT = -(planeIndexes[0]* firstPoint.x + planeIndexes[1]* firstPoint.y + planeIndexes[2]* firstPoint.z + planeIndexes[3])/(vector[0]+vector[1]+vector[2]);
        Point newPoint = new Point(firstPoint.x+variableT*vector[0], firstPoint.y + variableT*vector[1], firstPoint.z +variableT*vector[2]);

        return newPoint;
    }
    private static Point[] determinePointPairs(Point[] oneSidePoints, Point[] secSidePoints){
        double[] firstVector = calculateVector(oneSidePoints[0], secSidePoints[0]);
        double[] secVector = calculateVector(oneSidePoints[0], secSidePoints[1]);
        double firstVectorLength = Math.sqrt(firstVector[0]*firstVector[0] + firstVector[1]*firstVector[1] + firstVector[2]*firstVector[2]);
        double secVectorLength = Math.sqrt(secVector[0]*secVector[0] + secVector[1]*secVector[1] + secVector[2]*secVector[2]);

        if( firstVectorLength < secVectorLength){
            return new Point[]{oneSidePoints[0], secSidePoints[0], oneSidePoints[1], secSidePoints[1]};
        }
        return new Point[]{oneSidePoints[0], secSidePoints[1], oneSidePoints[1], secSidePoints[0]};
    }

    private static double[] calculateVector(Point firstPoint, Point secPoint){
        return new double[]{firstPoint.x - secPoint.x, firstPoint.y - secPoint.y, firstPoint.z - secPoint.z};
    }
}
