package pl.wydzials.virtualcamera.phongmodel;

import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;

public class Sketch extends PApplet {

    private final float radius = 300f;
    private final float pixelSize = 1f;

    private final float circleX = 0;
    private final float circleY = 0;
    private final float circleZ = -2000;

    private float lightX = -400;
    private float lightY = 400;
    private float lightZ = -1000;

    private final float cameraX = 0;
    private final float cameraY = 0;
    private final float cameraZ = 0;

    //Wsþółczynniki napisane arbitralnie
    private final float ka = 0.3f; //Musi być mniejsze niż 0.33
    private final float faat = 0.4f; //Najbezpieczniej jak jest mniejsze od 0.33 ale 0.4 powinno styknąć
    private final float kd = 0.5f; //Od 0.25 do 0.95
    private final float ks = 0.5f; //Od 0.25 do 0.95
    private final float n = 50f; //Od 2 do 100

    private Color circleStandardColor = new Color(200,20,50);
    private Color lightStandardColor = new Color(255,200,200);

    public void settings() {
        size(1700, 900);
    }

    public void setup() {
        frameRate(60);
        surface.setTitle("Phong Model");
    }

    private ArrayList<Pixel> createPixelArray() {
        ArrayList<Pixel> pixelArr = new ArrayList<>();
        float y = 0;
        float x = 0;
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

    private void addPixel(float x, float y, float z, ArrayList<Pixel> pixelArr){
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

    private float[] normalizeVector(float[] vector){
        float vectorLength = sqrt(vector[0]*vector[0] + vector[1]*vector[1] + vector[2]*vector[2]);
        float[] newVector = {vector[0]/vectorLength, vector[1]/vectorLength, vector[2]/vectorLength};
        return newVector;
    }
    
    private float scalarMultVector(float[] firstVector, float[] secVector){
        float sum=0;
        for (int i = 0; i < firstVector.length; i++)
            sum += firstVector[i]*secVector[i];
        return sum;
    }

    private Color calcAmbient(Pixel pixel){
        float[] color_arr = {circleStandardColor.getRed(), circleStandardColor.getGreen(), circleStandardColor.getBlue()};
        for(int i=0; i<3; i++){
            color_arr[i] = color_arr[i]*ka;
        }
        return new Color((int)color_arr[0], (int)color_arr[1], (int)color_arr[2]);
    }

    private Color calcDiffuse(Pixel pixel){
        float[] color_arr = {lightStandardColor.getRed(), lightStandardColor.getGreen(), lightStandardColor.getBlue()};
        float[] LVector = calcVector(pixel.getX(), pixel.getY(), pixel.getZ(), lightX, lightY, lightZ);
        float multVecotrNL = scalarMultVector(pixel.getNormalVector(), normalizeVector(LVector));
        multVecotrNL = max(0,multVecotrNL);
        for(int i=0; i<3; i++){
            color_arr[i] = faat*kd*multVecotrNL*color_arr[i];
        }
        return new Color((int)color_arr[0], (int)color_arr[1], (int)color_arr[2]);
    }

    private float calcAlfa(Pixel pixel){
        float[] LVector = calcVector(pixel.getX(), pixel.getY(), pixel.getZ(), lightX, lightY, lightZ);
        float multVecotrNL = scalarMultVector(pixel.getNormalVector(), normalizeVector(LVector));
        float betaAngle = acos(multVecotrNL);
        float[] VVector = calcVector(pixel.getX(), pixel.getY(), pixel.getZ(), cameraX, cameraY, cameraZ);
        float multVecotrNV = scalarMultVector(pixel.getNormalVector(), normalizeVector(VVector));
        float alfaAngle = acos(multVecotrNV) - betaAngle;
        float alfaCos = cos(alfaAngle);
        alfaCos = max(0,alfaCos);

        return alfaCos;
    }

    private Color calcSpecular(Pixel pixel){
        float[] color_arr = {lightStandardColor.getRed(), lightStandardColor.getGreen(), lightStandardColor.getBlue()};
        float alfaCos = calcAlfa(pixel);

        for(int i=0; i<3; i++){
            color_arr[i] = faat*ks*pow(alfaCos,n)*color_arr[i];
        }
        return new Color((int)color_arr[0], (int)color_arr[1], (int)color_arr[2]);
    }

    private Color sumColors(Color ambientColor, Color diffuseColor, Color specularColor){
        Color[] colorArr = {ambientColor, diffuseColor, specularColor};
        int[] colorValues = {0,0,0};
        for(Color singleColor: colorArr){
            colorValues[0] = colorValues[0] + singleColor.getRed();
            colorValues[1] = colorValues[1] + singleColor.getGreen();
            colorValues[2] = colorValues[2] + singleColor.getBlue();
        }
        return new Color(colorValues[0], colorValues[1], colorValues[2]);
    }

    public void draw() {
        if (keyPressed || frameCount == 1) {
            background(0, 0, 0);
            scale(1, -1);
            translate(width / 2, -height / 2);
            ArrayList<Pixel> pixelArr = createPixelArray();
            noStroke();
            for (Pixel pixel : pixelArr) {
                Color pixelColor = sumColors(calcAmbient(pixel), calcDiffuse(pixel), calcSpecular(pixel));
                fill(pixelColor.getRGB());
                square(pixel.getX(), pixel.getY(), pixelSize);
            }
        }
    }

    public void keyPressed() {
        int step = 100;
        switch (key) {
            case 'w' -> lightX += step;
            case 's' -> lightX -= step;

            case 'a' -> lightY += step;
            case 'd' -> lightY -= step;

            case 'q' -> lightZ += step;
            case 'e' -> lightZ -= step;
        }
    }


}
