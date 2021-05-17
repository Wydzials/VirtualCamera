package pl.wydzials.virtualcamera.bspmodel;

import pl.wydzials.virtualcamera.bspmodel.bsp.BinarySearchPartitioning;
import pl.wydzials.virtualcamera.bspmodel.bsp.Geometry;
import pl.wydzials.virtualcamera.bspmodel.bsp.TreeNode;
import pl.wydzials.virtualcamera.bspmodel.model.Face;
import pl.wydzials.virtualcamera.bspmodel.model.Line;
import pl.wydzials.virtualcamera.bspmodel.model.Model;
import pl.wydzials.virtualcamera.bspmodel.model.Point;
import pl.wydzials.virtualcamera.bspmodel.reader.ModelCreator;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Arrays;

public class Sketch extends PApplet {

    Model model;
    ArrayList<Line> projectedLines = new ArrayList<>();
    TreeNode<Face> facesTree;

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
        size(1700, 900);
    }

    public void setup() {
        frameRate(60);
        surface.setTitle("VirtualCamera");

        ModelCreator creator = new ModelCreator();
        creator.readCubesFromFile("data/model.json");
        creator.generateRandomCubes(100);
        model = creator.getModel();

        facesTree = BinarySearchPartitioning.buildTree(model.getFaces());

        updatePointsFromTree(facesTree);
    }

    private void updatePointsFromTree(TreeNode<Face> faces) {
        if (faces == null) {
            return;
        }
        Face face = faces.key;

        updatePointsFromTree(faces.left);
        for (int i = 0; i < face.getPoints().length; i++) {
            Point point = face.getPoint(i);

            if (!model.getPoints().containsKey(point.key())) {
                model.getPoints().put(point.key(), point);
            } else {
                face.setPoint(i, model.getPoints().get(point.key()));
            }
        }
        updatePointsFromTree(faces.right);
    }

    public void draw() {
        if (keyPressed || frameCount == 1) {
            background(200, 200, 200);

            updateSpeedFromFramerate();
            updateFocalLength();
            updatePoints();

            renderFaces(facesTree);
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

    private void updatePoints() {
        for (Point point : model.getPoints().values()) {
            point.move(xVelocity, yVelocity, zVelocity);
            point.rotate(xRotation, yRotation, zRotation);
        }
    }

    private void renderText() {
        fill(0, 0, 0);
        textSize(20);
        text("FPS: " + Math.round(frameRate), 10, 30);
        text("Focal: " + focalLength, 10, 60);
        //text("Rendered lines: " + projectedLines.size(), 10, 90);
    }

    private Point projectPoint(Point point) {
        double x = width / 2. + point.x * focalLength / point.z;
        double y = height / 2. - point.y * focalLength / point.z;
        double z = point.z;

        return new Point(x, y, z);
    }

    private void renderLines() {
        strokeCap(ROUND);
        stroke(0, 0, 0);
        for (Line line : model.getLines()) {
            if (areVisible(line.point1) && areVisible(line.point2)) {
                Point p1 = projectPoint(line.point1);
                Point p2 = projectPoint(line.point2);

                strokeWeight(2);
                line((float) p1.x, (float) p1.y, (float) p2.x, (float) p2.y);
            }
        }
    }

    private void renderFaces(TreeNode<Face> tree) {
        if (tree == null) {
            return;
        }
        double[] planeIndexes = Geometry.calcThePlane(Arrays.asList(tree.key.getPoints()));

        int leftSameSide = -10;
        int rightSameSide = -10;

        if (tree.left != null) {
            leftSameSide = Geometry.areFaceAndPointSameSide(tree.left.key, new Point(0, 0, 0), planeIndexes);
        }
        if (tree.right != null) {
            rightSameSide = Geometry.areFaceAndPointSameSide(tree.right.key, new Point(0, 0, 0), planeIndexes);
        }

        if (leftSameSide == 1 || rightSameSide == -1) {
            renderFaces(tree.right);
            renderFace(tree.key);
            renderFaces(tree.left);
        } else if (leftSameSide == -1 || rightSameSide == 1) {
            renderFaces(tree.left);
            renderFace(tree.key);
            renderFaces(tree.right);
        } else {
            renderFaces(tree.left);
            renderFace(tree.key);
            renderFaces(tree.right);
        }
    }

    private void renderFace(Face face) {
        if (areVisible(face.getPoints())) {
            float[] quadPoints = new float[8];
            for (int n = 0; n < 4; n++) {
                Point projected = projectPoint(face.getPoint(n));
                quadPoints[2 * n] = (float) projected.x;
                quadPoints[2 * n + 1] = (float) projected.y;
            }

            stroke(face.getColor().getRed(), face.getColor().getBlue(), face.getColor().getGreen());
            fill(face.getColor().getRed(), face.getColor().getBlue(), face.getColor().getGreen());

            quad(quadPoints[0], quadPoints[1], quadPoints[2], quadPoints[3],
                    quadPoints[4], quadPoints[5], quadPoints[6], quadPoints[7]);
        }
    }

    private boolean areVisible(Point... points) {
        return Arrays.stream(points).allMatch(point -> point.z > 0);
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
