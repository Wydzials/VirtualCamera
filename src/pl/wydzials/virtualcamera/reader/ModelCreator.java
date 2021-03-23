package pl.wydzials.virtualcamera.reader;

import com.google.gson.Gson;
import pl.wydzials.virtualcamera.model.Line;
import pl.wydzials.virtualcamera.model.Model;
import pl.wydzials.virtualcamera.model.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ModelCreator {
    private final Gson gson = new Gson();

    private final Map<String, Point> points = new HashMap<>();
    private final Set<Line> lines = new HashSet<>();

    public void readLinesFromFile(String path) {
        String file = readFile(path);
        LineDTO[] lineDTOs = gson.fromJson(file, LineDTO[].class);

        addLines(new HashSet<>(Arrays.asList(lineDTOs)));
    }

    public void readCubesFromFile(String path) {
        String file = readFile(path);
        CubeDTO[] cubeDTOs = gson.fromJson(file, CubeDTO[].class);

        for (CubeDTO c : cubeDTOs) {
            addLines(c.toLines());
        }
    }

    public Model getModel() {
        System.out.println("Loaded objects:");
        System.out.println(points.size() + " points");
        System.out.println(lines.size() + " lines");

        return new Model(points, lines);
    }

    public void generateRandomCubes(int n) {
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            double x = r.nextDouble() * 40000 - 20000;
            double y = r.nextDouble() * 40000 - 20000;
            double z = r.nextDouble() * 40000 - 20000;
            double len = r.nextDouble() * 2000 + 500;

            CubeDTO cube = new CubeDTO(x, y, z, len, len, len);
            addLines(cube.toLines());
        }
    }

    private void addLines(Set<LineDTO> lineDTOs) {
        for (LineDTO line : lineDTOs) {
            Point point1 = new Point(line.x1, line.y1, line.z1);
            Point point2 = new Point(line.x2, line.y2, line.z2);

            if (!points.containsValue(point1)) {
                points.put(point1.key(), point1);
            }
            if (!points.containsValue(point2)) {
                points.put(point2.key(), point2);
            }
            lines.add(new Line(points.get(point1.key()), points.get(point2.key())));
        }
    }

    private String readFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            System.out.println("Cannot read file '" + path + "'");
        }
        System.exit(-1);
        return null;
    }
}