package com.jbromley.processing.test;

import java.util.ArrayList;
import java.util.List;

import processing.opengl.*;

import processing.core.PApplet;
import toxi.geom.Vec3D;


public class Spheres3D extends PApplet {

	private static final long serialVersionUID = -6031415034798679428L;
	private static final int NUM_SPHERES = 200;

	private List<Sphere> spheres;
	  
	public void setup() {
	  size(1280, 800, P3D); 
	  background(0);
	  lights();
	  
	  Sphere.setPApplet(this);
	  
	  spheres = new ArrayList<Sphere>();
	  for (int i = 0; i < NUM_SPHERES; ++i) {
	    float xLimit = width / 4;
	    float yLimit = height / 4;
	    Vec3D pos = new Vec3D(random(-xLimit, xLimit), random(-yLimit, yLimit), random(-yLimit, yLimit));
	    float radius = 8;
	    Sphere sphere = new Sphere(pos, radius);
	    spheres.add(sphere);
	  }
	}

	public void draw() {
	  background(0);
	  lights();
	  pushMatrix();
	  scale(2);
	  translate(width / 4, height / 4, 0);
	  rotateX(mouseY * 0.005f);
	  rotateY(mouseX * 0.005f);
	  for (Sphere sphere : spheres) {
		  sphere.draw();
	  }
	  popMatrix();
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			PApplet.main(new String[] {"com.jbromley.processing.test.Spheres3D"});
		} else {
			PApplet.main(new String[] {args[0], "com.jbromley.processing.test.Spheres3D"});
		}
	}
}
