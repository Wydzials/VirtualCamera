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

    private final float lightX = 400;
    private final float lightY = 400;
    private final float lightZ = -1000;

    private final float cameraX = 0;
    private final float cameraY = 0;
    private final float cameraZ = 0;

    //Wsþółczynniki napisane arbitralnie
    private final float ka = 0.4f;
    private final float faat = 0.5f;
    private final float kd = 0.7f;
    private final float ks = 0.9f;
    private final float n = 5f;

    private Color circleStandardColor = new Color(200,50,100);
    private Color lightStandardColor = new Color(70,10,200);

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
        float[] LVector = {lightX - pixel.getX(), lightY - pixel.getY(), lightZ - pixel.getZ()};
        float multVecotrNL = scalarMultVector(pixel.getNormalVector(), normalizeVector(LVector));
        multVecotrNL = max(0,multVecotrNL);
        for(int i=0; i<3; i++){
            color_arr[i] = faat*kd*multVecotrNL*color_arr[i];
        }
        return new Color((int)color_arr[0], (int)color_arr[1], (int)color_arr[2]);
    }

    private float calcAlfa(Pixel pixel){
        float[] LVector = {lightX - pixel.getX(), lightY - pixel.getY(), lightZ - pixel.getZ()};
        float multVecotrNL = scalarMultVector(pixel.getNormalVector(), normalizeVector(LVector));
        float betaAngle = acos(multVecotrNL);
        float[] VVector = {cameraX - pixel.getX(), cameraY - pixel.getY(), cameraZ - pixel.getZ()};
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


}
