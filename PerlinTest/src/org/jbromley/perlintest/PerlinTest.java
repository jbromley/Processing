// PerlinTest.java
// Simple test of Perlin noise generation.
package org.jbromley.perlintest;


import processing.core.PApplet;
import peasy.*;


public class PerlinTest extends PApplet {

    private static final int SIZE = 640;

    private PeasyCam camera;
    private float noiseScale = 0.02f;

    public void settings()
    {
        size(SIZE, SIZE, "processing.opengl.PGraphics3D");
    }

    public void setup() {
        size(SIZE, SIZE, P3D);

        camera = new PeasyCam(this, 200);
        camera.setMinimumDistance(0);
        camera.setMaximumDistance(512);

        colorMode(HSB, 255);
    }

    public void draw() {
        background(255);
        for (int x = -SIZE / 2; x < SIZE / 2; ++x) {
            for (int y = -SIZE / 2; y < SIZE / 2; ++y) {
                float noiseVal = noise((mouseX + x) * noiseScale, (mouseY + y) *noiseScale);
                stroke(noiseVal * 255, 255.0f, 255.0f);
                // line(x, mouseY + noiseVal * 80, x, height);
                point(x, y, -128 + noiseVal * 255);
            }
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[] {"org.jbromley.perlintest.PerlinTest"});
    }

}
