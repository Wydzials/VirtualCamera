package pl.wydzials.virtualcamera.phongmodel;
import processing.core.PApplet;

import java.util.ArrayList;

public class Sketch extends PApplet {

    private final float radius = 300f;
    private final float pixelSize = 1f;

    private final float circleX = 0;
    private final float circleY = 0;
    private final float circleZ = -2000;

    private final float lightX = 400;
    private final float lightY = 400;
    private final float lightZ = 0;

    public void settings() {
        size(1700, 900);
    }

    public void setup() {
        frameRate(60);
        surface.setTitle("Phong Model");
    }

    private ArrayList<Pixel> createPixelArray(){
        ArrayList<Pixel> pixelArr = new ArrayList<>();
        float y=0;
        float x=0;
        while (y + pixelSize/2 < radius){
            float xBoundary = sqrt(radius*radius - (y+pixelSize/2)*(y+pixelSize/2));
            while (x+pixelSize/2 < xBoundary){
                pixelArr.add(new Pixel(x,y));
                pixelArr.add(new Pixel(-x+pixelSize,y));
                pixelArr.add(new Pixel(x,-y+pixelSize));
                pixelArr.add(new Pixel(-x+pixelSize,-y+pixelSize));
                x = x + pixelSize;
            }
            x=0;
            y = y + pixelSize;
        }
        return pixelArr;
    }

    public void draw() {
        if (keyPressed || frameCount == 1) {
            background(0,0,0);
            scale(1, -1);
            translate(width/2, -height/2);
            ArrayList<Pixel> pixelArr = createPixelArray();

            noStroke();
            for (Pixel pixel : pixelArr) {
                fill(pixel.getColor().getRGB());
                square(pixel.getX(), pixel.getY(), pixelSize);
            }
        }
    }









}
