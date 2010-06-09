package com.jbromley.processing.crystallize;

import com.jbromley.processing.util.Entity;

import processing.core.PApplet;
import processing.core.PVector;

public class Particle implements Entity {

	static private PApplet p;

	private PVector position;
	private int color;
	
	public Particle(float x, float y, int color) {
		position = new PVector(x, y);
		this.color = color;
	}
	
	public Particle(PVector center, float innerRadius, float outerRadius, int color) {
		float radius = p.random(innerRadius, outerRadius);
		float theta = p.random(0, 2 * PApplet.PI);
		PVector offset = new PVector(PApplet.cos(theta), PApplet.sin(theta));
		offset.mult(radius);
		position = PVector.add(center, offset);
		this.color = color;
	}
	
	public void update() {
		int direction = (int) p.random(4);
		switch (direction) {
		case 0: position.y += 1; break;
		case 1: position.x += 1; break;
		case 2: position.y -= 1; break;
		case 3: position.x -= 1; break;
		}
	}
	
	public void reposition(PVector crystalCenter, float innerRadius, float outerRadius) {
		float radius = p.random(innerRadius, outerRadius);
		float theta = p.random(0.0f, 2 * PApplet.PI);
		PVector offset = new PVector(radius * PApplet.cos(theta), radius * PApplet.sin(theta));
		position = PVector.add(crystalCenter, offset);
	}
	
	public void draw() {
		p.stroke(color);
		p.point(position.x, position.y);
	}
	
	public void draw(int color) {
		p.stroke(color);
		p.point(position.x, position.y);
	}

	static public void setApplet(PApplet applet) {
		p = applet; 
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	@Override
	public PVector getPosition() {
		return position;
	}

	@Override
	public void setPosition(PVector position) {
		this.position = position; 
	}
}
