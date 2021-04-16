package pl.wydzials.virtualcamera.model;

public class Face {
    private final Point[] points;

    public Face(Point... points) {
        if (points.length != 4) {
            throw new IllegalArgumentException("Number of points must be equal to 4, was: " + points.length);
        }
        this.points = points;
    }

    public Point[] getPoints() {
        return points;
    }

    public Point getPoint(int n) {
        if (n >= 0 && n <= 3) {
            return points[n];
        }
        throw new IllegalArgumentException("Point number must be between 0 and 3, was: " + n);
    }

    public void setPoint(int n, Point point) {
        if (n >= 0 && n <= 3) {
            points[n] = point;
        } else {
            throw new IllegalArgumentException("Point number must be between 0 and 3, was: " + n);
        }
    }
}
