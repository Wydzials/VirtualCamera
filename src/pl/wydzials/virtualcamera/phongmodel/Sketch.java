package pl.wydzials.virtualcamera.phongmodel;

import processing.core.PApplet;
import processing.core.PImage;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Sketch extends PApplet {

    private final float radius = 300f;
    private final int pixelSize = 1;

    private final float circleX = 0;
    private final float circleY = 0;
    private final float circleZ = -2000;

    private float lightX = -400;
    private float lightY = 400;
    private float lightZ = -1000;

    private final float cameraX = 0;
    private final float cameraY = 0;
    private final float cameraZ = 0;

    private final Set<Character> pressedKeys = new HashSet<>();

    private final float ka = 0.3f; // max 0.33
    private final float faat = 0.4f; // max 0.4
    private float kd = 0.5f; // od 0.25 do 0.95
    private float ks = 0.75f; // od 0.25 do 0.95
    private float n = 100f; // od 2 do 100

    private final Color circleStandardColor = new Color(200, 20, 50);
    private final Color lightStandardColor = new Color(255, 200, 200);

    private int material = 0;

    ArrayList<Pixel> pixelArr = createPixelArray();

    public void settings() {
        size(1700, 900);
    }

    public void setup() {
        frameRate(60);
        surface.setTitle("Phong Model");
        textSize(20);
    }

    private ArrayList<Pixel> createPixelArray() {
        ArrayList<Pixel> pixelArr = new ArrayList<>();
        int y = 0;
        int x = 0;
        while (y + pixelSize / 2 < radius) {
            float xBoundary = sqrt(radius * radius - (y + pixelSize / 2) * (y + pixelSize / 2));
            while (x + pixelSize / 2 < xBoundary) {
                float z = circleZ + sqrt(radius * radius - x * x - y * y);
                addPixel(x, y, z, pixelArr);
                addPixel(-x + pixelSize, y, z, pixelArr);
                addPixel(x, -y + pixelSize, z, pixelArr);
                addPixel(-x + pixelSize, -y + pixelSize, z, pixelArr);
                x = x + pixelSize;
            }
            x = 0;
            y = y + pixelSize;
        }
        return pixelArr;
    }

    private void addPixel(float x, float y, float z, ArrayList<Pixel> pixelArr) {
        float[] normalVector = calcVector(circleX, circleY, circleZ, x, y, z);
        float[] normalizedVector = normalizeVector(normalVector);
        Pixel newPixel = new Pixel(x, y, z, normalizedVector);
        pixelArr.add(newPixel);
    }

    private float[] calcVector(float firstX, float firstY, float firstZ, float secX, float secY, float secZ) {
        float VectorX = secX - firstX;
        float VectorY = secY - firstY;
        float VectorZ = secZ - firstZ;
        return new float[]{VectorX, VectorY, VectorZ};
    }

    private float[] normalizeVector(float[] vector) {
        float vectorLength = sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);
        return new float[]{vector[0] / vectorLength, vector[1] / vectorLength, vector[2] / vectorLength};
    }

    private float scalarMultVector(float[] firstVector, float[] secVector) {
        float sum = 0;
        for (int i = 0; i < firstVector.length; i++)
            sum += firstVector[i] * secVector[i];
        return sum;
    }

    private Color calcAmbient() {
        float[] color_arr = {circleStandardColor.getRed(), circleStandardColor.getGreen(), circleStandardColor.getBlue()};
        for (int i = 0; i < 3; i++) {
            color_arr[i] = color_arr[i] * ka;
        }
        return new Color((int) color_arr[0], (int) color_arr[1], (int) color_arr[2]);
    }

    private Color calcDiffuse(Pixel pixel) {
        float[] color_arr = {lightStandardColor.getRed(), lightStandardColor.getGreen(), lightStandardColor.getBlue()};
        float[] LVector = calcVector(pixel.getX(), pixel.getY(), pixel.getZ(), lightX, lightY, lightZ);
        float multVectorNL = scalarMultVector(pixel.getNormalVector(), normalizeVector(LVector));
        multVectorNL = max(0, multVectorNL);
        for (int i = 0; i < 3; i++) {
            color_arr[i] = faat * kd * multVectorNL * color_arr[i];
        }
        return new Color((int) color_arr[0], (int) color_arr[1], (int) color_arr[2]);
    }

    private float calcAlfa(Pixel pixel) {
        float[] LVector = calcVector(lightX, lightY, lightZ, pixel.getX(), pixel.getY(), pixel.getZ());
        LVector = normalizeVector(LVector);
        float multVectorLN = scalarMultVector(pixel.getNormalVector(), LVector);
        float[] RVector = {cameraX, cameraY, cameraZ};
        for (int i = 0; i < 3; i++) {
            RVector[i] = LVector[i] - 2 * multVectorLN * pixel.getNormalVector()[i];
        }
        float multVectorVN = scalarMultVector(pixel.getNormalVector(), RVector);
        multVectorVN = max(0, multVectorVN);
        return multVectorVN;
    }

    private Color calcSpecular(Pixel pixel) {
        float[] color_arr = {lightStandardColor.getRed(), lightStandardColor.getGreen(), lightStandardColor.getBlue()};
        float alfaCos = calcAlfa(pixel);

        for (int i = 0; i < 3; i++) {
            color_arr[i] = faat * ks * pow(alfaCos, n) * color_arr[i];
        }
        return new Color((int) color_arr[0], (int) color_arr[1], (int) color_arr[2]);
    }

    private Color sumColors(Color ambientColor, Color diffuseColor, Color specularColor) {
        Color[] colorArr = {ambientColor, diffuseColor, specularColor};
        int[] colorValues = {0, 0, 0};
        for (Color singleColor : colorArr) {
            colorValues[0] = colorValues[0] + singleColor.getRed();
            colorValues[1] = colorValues[1] + singleColor.getGreen();
            colorValues[2] = colorValues[2] + singleColor.getBlue();
        }
        return new Color(colorValues[0], colorValues[1], colorValues[2]);
    }

    private void imageSquare(PImage image, int x, int y, int color, int size) {
        for (int pixelX = x; pixelX < x + size; pixelX++) {
            for (int pixelY = y; pixelY < y + size; pixelY++) {
                image.pixels[pixelY * width + pixelX] = color;
            }
        }
    }

    public void draw() {
        if (keyPressed || frameCount == 1) {
            handlePressedKeys();

            PImage img = createImage(width, height, RGB);
            for (Pixel pixel : pixelArr) {
                Color pixelColor = sumColors(calcAmbient(), calcDiffuse(pixel), calcSpecular(pixel));

                int x = (int) pixel.getX() + width / 2;
                int y = (int) pixel.getY() + height / 2;

                imageSquare(img, x, y, pixelColor.getRGB(), pixelSize);
            }
            image(img, 0, 0);
            text("Light position: " + (int) lightX + ", " + (int) lightY + ", " + (int) lightZ, 10, 30);
            text("Preset material (1,2,3,4): " + (material > 0 ? material : "-"), 10, 60);
        }
    }

    public void handlePressedKeys() {
        int step = 100;
        if (pressedKeys.contains('w')) lightY -= step;
        if (pressedKeys.contains('s')) lightY += step;

        if (pressedKeys.contains('a')) lightX -= step;
        if (pressedKeys.contains('d')) lightX += step;

        if (pressedKeys.contains('q')) lightZ += step;
        if (pressedKeys.contains('e')) lightZ -= step;

        if (pressedKeys.contains('1')) { //No specular
            material = 1;
            ks = 0;
            kd = 1f;
            n = 2;
        }
        if (pressedKeys.contains('2')) {
            material = 2;
            ks = 0.25f;
            kd = 0.75f;
            n = 20;
        }
        if (pressedKeys.contains('3')) {
            material = 3;
            ks = 0.5f;
            kd = 0.5f;
            n = 50;
        }
        if (pressedKeys.contains('4')) { //Full specular
            material = 4;
            ks = 1f;
            kd = 0.3f;
            n = 100;
        }
    }

    public void keyPressed() {
        pressedKeys.add(key);
    }

    public void keyReleased() {
        pressedKeys.remove(key);
    }


}
