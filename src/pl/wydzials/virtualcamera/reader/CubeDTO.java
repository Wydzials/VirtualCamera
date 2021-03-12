package pl.wydzials.virtualcamera.reader;

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
}
