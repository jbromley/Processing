package org.jbromley.spheres3d;


import processing.core.*;
import java.util.List;
import java.util.ArrayList;


public class Spheres3D extends PApplet {

    private static final int NUM_SPHERES = 500;

    private List<Sphere> spheres;
	  
    public void setup() {
	size(1280, 800, P3D); 
	background(0);
	lights();
	  	  
	spheres = new ArrayList<Sphere>();
	for (int i = 0; i < NUM_SPHERES; ++i) {
	    float xLimit = width / 2;
	    float yLimit = height / 2;
	    PVector pos = new PVector(random(-xLimit, xLimit), 
				      random(-yLimit, yLimit), 
				      random(-yLimit, yLimit));
	    float radius = 8;
	    Sphere sphere = new Sphere(pos, radius, this);
	    spheres.add(sphere);
	}
    }

    public void draw() {
	background(0);
	lights();
	pushMatrix();
	//scale(2);
	translate(width / 2, height / 2, 0);
	rotateX(mouseY * 0.005f);
	rotateY(mouseX * 0.005f);
	for (Sphere sphere : spheres) {
	    sphere.draw();
	}
	popMatrix();
    }

    public static void main(String args[]) {
	PApplet.main(new String[] {"org.jbromley.spheres3d.Spheres3D"});
    }

}
