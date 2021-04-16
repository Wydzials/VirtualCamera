package pl.wydzials.virtualcamera.model;

import java.util.Map;
import java.util.Set;

public class Model {
    private final Map<String, Point> points;
    private final Set<Line> lines;
    private final Set<Face> faces;

    public Model(Map<String, Point> points, Set<Line> lines, Set<Face> faces) {
        this.points = points;
        this.lines = lines;
        this.faces = faces;
    }

    public Map<String, Point> getPoints() {
        return points;
    }

    public Set<Line> getLines() {
        return lines;
    }

    public Set<Face> getFaces() {
        return faces;
    }
}
