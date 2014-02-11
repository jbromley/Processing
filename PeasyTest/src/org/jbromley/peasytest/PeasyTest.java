// PeasyTest.java
// Test for the Peasy camera control library.
package org.jbromley.peasytest;


import processing.core.PApplet;
import peasy.*;


public class PeasyTest extends PApplet {

    PeasyCam camera;

    public void setup() {
        size(640, 640, P3D);
        camera = new PeasyCam(this, 160);
        camera.setMinimumDistance(16);
        camera.setMaximumDistance(1024);
    }

    public void draw() {
        rotateX(-0.5f);
        rotateY(-0.5f);
        background(0);
        fill(255, 0, 0);
        box(64);
        pushMatrix();
        translate(0, 0, 40);
        fill(0, 0, 255);
        box(16);
        fill(240, 240, 240, 128);
        stroke(255, 255, 255);
        translate(0, 0, 32);
        box(32, 32, 1);
        popMatrix();
    }

    public static void main(String args[]) {
	PApplet.main(new String[] {"org.jbromley.peasytest.PeasyTest"});
    }

}
