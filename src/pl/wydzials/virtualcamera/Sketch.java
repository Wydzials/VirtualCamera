package pl.wydzials.virtualcamera;

import pl.wydzials.virtualcamera.model.Face;
import pl.wydzials.virtualcamera.model.Line;
import pl.wydzials.virtualcamera.model.Model;
import pl.wydzials.virtualcamera.model.Point;
import pl.wydzials.virtualcamera.reader.ModelCreator;
import processing.core.PApplet;

import java.util.ArrayList;

public class Sketch extends PApplet {

    Model model;
    ArrayList<Line> projectedLines = new ArrayList<>();
    ArrayList<Face> projectedFaces = new ArrayList<>();

    double focalChange = 0;
    double focalLength = 400;

    double movingVelocity = 30;
    double xVelocity = 0;
    double yVelocity = 0;
    double zVelocity = 0;

    double rotationSpeed = Math.PI / 120;
    double xRotation = 0;
    double yRotation = 0;
    double zRotation = 0;

    public void settings() {
        size(1400, 800);
    }

    public void setup() {
        frameRate(60);
        surface.setTitle("VirtualCamera");

        ModelCreator creator = new ModelCreator();
        creator.readCubesFromFile("data/model.json");
        creator.generateRandomCubes(50);
        model = creator.getModel();
    }

    public void draw() {
        if (keyPressed || frameCount == 1) {
            background(200, 200, 200);

            updateSpeedFromFramerate();
            updateFocalLength();

            updatePoints();
            projectLines();

            renderFaces();
            renderLines();
            renderText();
        }
    }

    private void updateSpeedFromFramerate() {
        movingVelocity = 30 * 60 / frameRate;
        rotationSpeed = Math.PI / 120 * 60 / frameRate;
    }

    private void updateFocalLength() {
        focalLength += focalChange;
        focalLength = Math.max(focalLength, 50);
    }

    private void renderText() {
        fill(0, 0, 0);
        textSize(20);
        text("FPS: " + Math.round(frameRate), 10, 30);
        text("Focal: " + focalLength, 10, 60);
        text("Rendered lines: " + projectedLines.size(), 10, 90);
    }

    private void updatePoints() {
        for (Point point : model.points.values()) {
            point.move(xVelocity, yVelocity, zVelocity);
            point.rotate(xRotation, yRotation, zRotation);
        }
    }

    private void projectLines() {
        projectedLines.clear();
        for (Line line : model.lines) {
            if (isVisible(line.point1) && isVisible(line.point2)) {
                Point p1 = projectPoint(line.point1);
                Point p2 = projectPoint(line.point2);

                projectedLines.add(new Line(p1, p2));
            }
        }
    }

    private void renderFaces() {
        projectedFaces.clear();
        fill(204, 102, 0);
        noStroke();

        for (Face face : model.faces) {
            if (isVisible(face.getPoint(0)) && isVisible(face.getPoint(1))
                    && isVisible(face.getPoint(2)) && isVisible(face.getPoint(3))) {
                float[] quadPoints = new float[8];

                for (int n = 0; n < 4; n++) {
                    Point projected = projectPoint(face.getPoint(n));
                    quadPoints[2 * n] = (float) projected.x;
                    quadPoints[2 * n + 1] = (float) projected.y;
                }

                quad(quadPoints[0], quadPoints[1], quadPoints[2], quadPoints[3],
                        quadPoints[4], quadPoints[5], quadPoints[6], quadPoints[7]);
            }
        }
    }

    private Point projectPoint(Point point) {
        double x = width / 2. + point.x * focalLength / point.z;
        double y = height / 2. - point.y * focalLength / point.z;
        double z = point.z;

        return new Point(x, y, z);
    }

    private boolean isVisible(Point point) {
        return point.z > 0;
    }

    private void renderLines() {
        strokeCap(ROUND);
        stroke(0, 0, 0);
        for (Line line : projectedLines) {
            Point p1 = line.point1;
            Point p2 = line.point2;

            strokeWeight(2);
            line((float) p1.x, (float) p1.y, (float) p2.x, (float) p2.y);
        }
    }

    private void renderPoint(Point point) {
        if (isVisible(point)) {
            fill(255, 255, 255);
            circle((float) point.x, (float) point.y, (float) (60000 / point.z));
            textSize((float) (40000 / point.z));
        }
    }

    public void keyPressed() {
        switch (key) {
            case 'w' -> zVelocity = -movingVelocity;
            case 's' -> zVelocity = movingVelocity;

            case 'a' -> xVelocity = movingVelocity;
            case 'd' -> xVelocity = -movingVelocity;

            case 'q' -> yVelocity = movingVelocity;
            case 'e' -> yVelocity = -movingVelocity;

            case 'l' -> yRotation = -rotationSpeed;
            case 'j' -> yRotation = rotationSpeed;

            case 'i' -> xRotation = rotationSpeed;
            case 'k' -> xRotation = -rotationSpeed;

            case 'u' -> zRotation = -rotationSpeed;
            case 'o' -> zRotation = rotationSpeed;

            case '=' -> focalChange = 5;
            case '-' -> focalChange = -5;
        }
    }

    public void keyReleased() {
        switch (key) {
            case 'w', 's' -> zVelocity = 0;
            case 'a', 'd' -> xVelocity = 0;
            case 'q', 'e' -> yVelocity = 0;

            case 'l', 'j' -> yRotation = 0;
            case 'i', 'k' -> xRotation = 0;
            case 'u', 'o' -> zRotation = 0;

            case '=', '-' -> focalChange = 0;
        }
    }
}
