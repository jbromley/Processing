package com.jbromley.processing.crystallize;

import java.awt.geom.Rectangle2D;

import com.jbromley.processing.util.Entity;

import processing.core.PApplet;
import processing.core.PVector;

public class Particle implements Entity {

	static private PApplet p;

	private PVector position;
	private int color;
	
	public Particle(final float x, final float y) {
		position = new PVector(x, y);
	}
	
	public Particle(PVector center, final float innerRadius, 
			final float outerRadius) {
		reposition(center, innerRadius, outerRadius);
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
	
	public void reposition(PVector crystalCenter, final float innerRadius, 
			final float outerRadius) {
		Rectangle2D.Float boundingBox = new Rectangle2D.Float(0, 0, 
				p.width, p.height);
		PVector newPosition = null;
		
		do {
			float radius = p.random(innerRadius, outerRadius);
			float theta = p.random(0.0f, 2 * PApplet.PI);
			PVector offset = new PVector(radius * PApplet.cos(theta), 
					radius * PApplet.sin(theta));
		
			newPosition = PVector.add(crystalCenter, offset);
		} while (!boundingBox.contains(newPosition.x, newPosition.y));
			
		position = newPosition;
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
