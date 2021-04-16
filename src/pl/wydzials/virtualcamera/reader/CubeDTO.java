package pl.wydzials.virtualcamera.reader;

import pl.wydzials.virtualcamera.model.Face;
import pl.wydzials.virtualcamera.model.Point;

import java.util.HashSet;
import java.util.Set;

public class CubeDTO {
    public double x, y, z, xLen, yLen, zLen;

    public CubeDTO(double x, double y, double z, double xLen, double yLen, double zLen) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xLen = xLen;
        this.yLen = yLen;
        this.zLen = zLen;
    }

    public Set<LineDTO> toLines() {
        Set<LineDTO> lines = new HashSet<>();

        lines.add(new LineDTO(x, y, z, x + xLen, y, z));
        lines.add(new LineDTO(x + xLen, y, z, x + xLen, y + yLen, z));
        lines.add(new LineDTO(x + xLen, y + yLen, z, x, y + yLen, z));
        lines.add(new LineDTO(x, y + yLen, z, x, y, z));

        lines.add(new LineDTO(x, y, z + zLen, x + xLen, y, z + zLen));
        lines.add(new LineDTO(x + xLen, y, z + zLen, x + xLen, y + yLen, z + zLen));
        lines.add(new LineDTO(x + xLen, y + yLen, z + zLen, x, y + yLen, z + zLen));
        lines.add(new LineDTO(x, y + yLen, z + zLen, x, y, z + zLen));

        lines.add(new LineDTO(x, y, z, x, y, z + zLen));
        lines.add(new LineDTO(x + xLen, y, z, x + xLen, y, z + zLen));
        lines.add(new LineDTO(x + xLen, y + yLen, z, x + xLen, y + yLen, z + zLen));
        lines.add(new LineDTO(x, y + yLen, z, x, y + yLen, z + zLen));

        return lines;
    }

    public Set<Face> toFaces() {
        Set<Face> faces = new HashSet<>();
        Point[] vertices = getVertices();

        // front and back
        faces.add(new Face(vertices[0], vertices[1], vertices[2], vertices[3]));
        faces.add(new Face(vertices[4], vertices[5], vertices[6], vertices[7]));

        // down and up
        faces.add(new Face(vertices[0], vertices[1], vertices[5], vertices[4]));
        faces.add(new Face(vertices[2], vertices[3], vertices[7], vertices[6]));

        // left and right
        faces.add(new Face(vertices[0], vertices[3], vertices[7], vertices[4]));
        faces.add(new Face(vertices[1], vertices[2], vertices[6], vertices[5]));

        return faces;
    }

    private Point[] getVertices() {
        Point[] points = new Point[8];

        // front counterclockwise
        points[0] = new Point(x, y, z);
        points[1] = new Point(x + xLen, y, z);
        points[2] = new Point(x + xLen, y + yLen, z);
        points[3] = new Point(x, y + yLen, z);

        // back counterclockwise
        points[4] = new Point(x, y, z + zLen);
        points[5] = new Point(x + xLen, y, z + zLen);
        points[6] = new Point(x + xLen, y + yLen, z + zLen);
        points[7] = new Point(x, y + yLen, z + zLen);

        return points;
    }
}
