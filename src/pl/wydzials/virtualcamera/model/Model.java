package pl.wydzials.virtualcamera.model;

import java.util.Map;
import java.util.Set;

public class Model {
    public final Map<String, Point> points;
    public final Set<Line> lines;
    public final Set<Face> faces;

    public Model(Map<String, Point> points, Set<Line> lines, Set<Face> faces) {
        this.points = points;
        this.lines = lines;
        this.faces = faces;
    }
}
