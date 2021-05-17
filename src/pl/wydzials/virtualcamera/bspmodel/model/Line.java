package pl.wydzials.virtualcamera.bspmodel.model;


import java.util.Objects;

public class Line {
    public Point point1;
    public Point point2;

    public Line(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return point1.equals(line.point1) && point2.equals(line.point2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point1, point2);
    }

    @Override
    public String toString() {
        return "Line(" + point1 + ", " + point2 + ")";
    }

}
