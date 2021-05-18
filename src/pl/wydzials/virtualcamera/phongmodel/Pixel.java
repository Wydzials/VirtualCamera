package pl.wydzials.virtualcamera.phongmodel;

import java.awt.*;

public class Pixel {
    private Color color;
    private final float x;
    private final float y;
    private final float z;

    public Pixel(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
        color = new Color(100,100,100);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Color getColor() {
        return color;
    }
}
