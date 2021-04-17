package pl.wydzials.virtualcamera.bsp;

import pl.wydzials.virtualcamera.model.Face;
import pl.wydzials.virtualcamera.model.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Geometry {

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

    public static double[] calcThePlane(List<Point> points) {
        double x1 = points.get(1).x - points.get(0).x;
        double y1 = points.get(1).y - points.get(0).y;
        double z1 = points.get(1).z - points.get(0).z;

        double x2 = points.get(2).x - points.get(0).x;
        double y2 = points.get(2).y - points.get(0).y;
        double z2 = points.get(2).z - points.get(0).z;

        double a = y1 * z2 - y2 * z1;
        double b = x2 * z1 - x1 * z2;
        double c = x1 * y2 - y1 * x2;
        double d = -a * points.get(0).x - b * points.get(0).y - c * points.get(0).z;

        return new double[]{a, b, c, d};
    }

    public static int areTwoPointsSameSide(Point firstPoint, Point secPoint, double[] planeIndexes) {
        double firstValue = firstPoint.x * planeIndexes[0] + firstPoint.y * planeIndexes[1] + firstPoint.z * planeIndexes[2] + planeIndexes[3];
        double secValue = secPoint.x * planeIndexes[0] + secPoint.y * planeIndexes[1] + secPoint.z * planeIndexes[2] + planeIndexes[3];

        if (java.lang.Math.abs(firstValue) < 10 || java.lang.Math.abs(secValue) < 10) {
            return 0;
        }
        if (firstValue * secValue > 0) {
            return 1;
        }
        return -1;
    }

    public static int areFaceAndPointSameSide(Face face, Point point, double[] planeIndexes) {
        int[] sameSides = new int[4];
        for (int i = 0; i < 4; i++) {
            sameSides[i] = Geometry.areTwoPointsSameSide(face.getPoint(i), point, planeIndexes);
        }

        long sameCount = Arrays.stream(sameSides).filter(i -> i == 1).count();
        long oppositeCount = Arrays.stream(sameSides).filter(i -> i == -1).count();
        long zeroCount = Arrays.stream(sameSides).filter(i -> i == 0).count();

        if (zeroCount == 4) {
            return 0; // w plaszczyznie
        } else if (sameCount == 4 || (zeroCount > 0 && sameCount > oppositeCount)) {
            return 1;
        } else if (oppositeCount == 4 || (zeroCount > 0 && oppositeCount > sameCount)) {
            return -1;
        } else {
            return -10; // do podzialu
        }
    }

    public static Face[] divideFace(Point[] oneSidePoints, Point[] secSidePoints, double[] planeIndexes) {
        Point[] orderedPoints = determinePointPairs(oneSidePoints, secSidePoints);
        Point firstPoint = findPoint(orderedPoints[0], orderedPoints[1], planeIndexes);
        Point secPoint = findPoint(orderedPoints[2], orderedPoints[3], planeIndexes);

        System.out.println(firstPoint + " " + secPoint);

        Face firstFace = new Face(oneSidePoints[0], oneSidePoints[1], firstPoint, secPoint);
        Face secFace = new Face(secSidePoints[0], secSidePoints[1], firstPoint, secPoint);
        return new Face[]{firstFace, secFace};
    }

    private static Point findPoint(Point firstPoint, Point secPoint, double[] planeIndexes) {
        double[] vector = calculateVector(firstPoint, secPoint);
        double variableT = -(planeIndexes[0] * firstPoint.x + planeIndexes[1] * firstPoint.y + planeIndexes[2] * firstPoint.z + planeIndexes[3]) / (vector[0] + vector[1] + vector[2]);

        return new Point(firstPoint.x + variableT * vector[0], firstPoint.y + variableT * vector[1], firstPoint.z + variableT * vector[2]);
    }

    private static Point[] determinePointPairs(Point[] oneSidePoints, Point[] secSidePoints) {
        double[] firstVector = calculateVector(oneSidePoints[0], secSidePoints[0]);
        double[] secVector = calculateVector(oneSidePoints[0], secSidePoints[1]);
        double firstVectorLength = java.lang.Math.sqrt(firstVector[0] * firstVector[0] + firstVector[1] * firstVector[1] + firstVector[2] * firstVector[2]);
        double secVectorLength = java.lang.Math.sqrt(secVector[0] * secVector[0] + secVector[1] * secVector[1] + secVector[2] * secVector[2]);

        if (firstVectorLength < secVectorLength) {
            return new Point[]{oneSidePoints[0], secSidePoints[0], oneSidePoints[1], secSidePoints[1]};
        }
        return new Point[]{oneSidePoints[0], secSidePoints[1], oneSidePoints[1], secSidePoints[0]};
    }

    private static double[] calculateVector(Point firstPoint, Point secPoint) {
        return new double[]{firstPoint.x - secPoint.x, firstPoint.y - secPoint.y, firstPoint.z - secPoint.z};
    }
}
