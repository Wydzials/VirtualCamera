package pl.wydzials.virtualcamera.phongmodel;

import processing.core.PApplet;

//TODOS
//Normalizacja wektora
    //Aby znormalizować wektor trzeba policzyć jego długość i każdy z wymiarów podzielić przez tą długość
    //We wzorze Phonga zastosowano iloczyn skalarny dwóch znormalizowanych wektorów

public class Main {

    public static void main(String[] args) {
        String[] processingArgs = {"Phong Model"};
        Sketch mySketch = new Sketch();
        PApplet.runSketch(processingArgs, mySketch);
    }
}
