package pl.wydzials.model;


import java.util.Objects;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Point {
    public double x, y, z;

    public Point(double x, double y, double z) {
        set(x, y, z);
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private double[][] toMatrix() {
        return new double[][]{{x}, {y}, {z}};
    }

    public void rotate(char axis, double angle) {
        double[][] rotated = multiplyMatrices(getRotationMatrix(axis, angle), toMatrix());
        set(rotated[0][0], rotated[1][0], rotated[2][0]);
    }

    public void move(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void rotate(double xAngle, double yAngle, double zAngle) {
        rotate('x', xAngle);
        rotate('y', yAngle);
        rotate('z', zAngle);
    }

    private double[][] getRotationMatrix(char axis, double angle) {
        return switch (Character.toLowerCase(axis)) {
            case 'x' -> new double[][]{
                    {1, 0, 0},
                    {0, cos(angle), -sin(angle)},
                    {0, sin(angle), cos(angle)}
            };
            case 'y' -> new double[][]{
                    {cos(angle), 0, sin(angle)},
                    {0, 1, 0},
                    {-sin(angle), 0, cos(angle)}
            };
            case 'z' -> new double[][]{
                    {cos(angle), -sin(angle), 0},
                    {sin(angle), cos(angle), 0},
                    {0, 0, 1}
            };
            default -> throw new IllegalArgumentException("Invalid axis: " + axis);
        };
    }

    private double[][] multiplyMatrices(double[][] a, double[][] b) {
        double[][] c = new double[a.length][b[0].length];

        for (int row = 0; row < c.length; row++) {
            for (int col = 0; col < c[0].length; col++) {
                for (int i = 0; i < a[0].length; i++) {
                    c[row][col] += a[row][i] * b[i][col];
                }
            }
        }
        return c;
    }

    @Override
    public String toString() {
        return "Point3D(" + key() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0 && Double.compare(point.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public String key() {
        return "%s-%s-%s".formatted(x, y, z);
    }


}
