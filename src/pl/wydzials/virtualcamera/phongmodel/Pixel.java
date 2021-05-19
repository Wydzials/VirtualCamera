package pl.wydzials.virtualcamera.phongmodel;

import java.awt.*;

public class Pixel {
    private final float x;
    private final float y;
    private final float z;
    private final float[] normalVector;

    public Pixel(float x, float y, float z, float[] normalVector){
        this.x = x;
        this.y = y;
        this.z = z;
        this.normalVector = normalVector;
    }

    public float[] getNormalVector() {
        return normalVector;
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
}
