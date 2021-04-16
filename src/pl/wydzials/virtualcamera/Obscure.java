package pl.wydzials.virtualcamera;

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

        if (Math.abs(firstValue) < 1 || Math.abs(secValue) < 1)
            return 0;
        else if (firstValue * secValue > 0)
            return 1;
        return -1;
    }
}
