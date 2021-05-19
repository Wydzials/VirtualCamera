package pl.wydzials.virtualcamera.bspmodel;

import processing.core.PApplet;

public class Main {

    public static void main(String[] args) {
        String[] processingArgs = {"Virtual Camera"};
        Sketch mySketch = new Sketch();
        PApplet.runSketch(processingArgs, mySketch);
    }
}
