package pl.wydzials.virtualcamera.phongmodel;

import processing.core.PApplet;

import java.awt.*;

//TODOS
//Napisać wyliczenie trzech rodzai natężeń

public class Main {

    public static void main(String[] args) {
        String[] processingArgs = {"Phong Model"};
        Sketch mySketch = new Sketch();
        PApplet.runSketch(processingArgs, mySketch);
    }
}
