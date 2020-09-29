package org.jbromley.spheres3d;


import processing.core.PApplet;
import processing.core.PVector;


public class Sphere {
  
    private PApplet p;
    private PVector pos;
    private float radius;
    private int color;
    
    public Sphere(PVector initPos, float radius, PApplet applet) {
        p = applet;
        pos = initPos;
        this.radius = radius;
        color = p.color(p.random(0, 255), p.random(0, 255), p.random(0, 255), p.random(0, 255));
    }
      
    public void draw() {
        p.pushMatrix();
        p.noStroke();
        p.fill(color);
        p.translate(pos.x, pos.y, pos.z);
        p.sphere(radius);
        p.popMatrix();
    }
}

