package pl.wydzials.model;

import java.util.Map;
import java.util.Set;

public class Model {
    public Map<String, Point> points;
    public Set<Line> lines;

    public Model(Map<String, Point> points, Set<Line> lines) {
        this.points = points;
        this.lines = lines;
    }
}
