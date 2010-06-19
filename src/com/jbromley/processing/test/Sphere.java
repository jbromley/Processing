package com.jbromley.processing.test;

import processing.core.PApplet;
import toxi.geom.Vec3D;

public class Sphere {
	
	private static PApplet p;

	private Vec3D pos;
	private float radius;
	private int color;
	  
	public Sphere(Vec3D initPos, float radius) {
		pos = initPos;
		this.radius = radius;
		color = p.color(p.random(0, 255), p.random(0, 255), p.random(0, 255), 
				p.random(0, 255));
	}
	
	public static void setPApplet(PApplet applet) {
		p = applet;
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
