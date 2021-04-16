package pl.wydzials.virtualcamera.model;

import java.awt.*;

public class Face {
    private final Point[] points;

    private Color color;

    public Face(Point... points) {
        this(new Color(204, 102, 0), points);
    }

    public Face(Color color, Point... points) {
        if (points.length != 4) {
            throw new IllegalArgumentException("Number of points must be equal to 4, was: " + points.length);
        }
        this.points = points;
        this.color = color;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
