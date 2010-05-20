package com.jbromley.processing;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

/**
 * A flock of "boids".
 * @author <a href="mailto:jbromley@gmail.com">J. Bromley</a>
 */
public class Flocking extends PApplet {
	
	private static final long serialVersionUID = 9221726134245604843L;

	private static final int INITIAL_BOIDS = 500;
	
	private Flock flock = null;
	private ArrayList<Line2D.Float> walls = null;
	private PFont font = null;
	
	/**
	 * Creates the flock boids demo. 
	 */
	public void setup() {
		size(1280, 800);
		
		// Create walls
		walls = new ArrayList<Line2D.Float>();
		walls.add(new Line2D.Float(0.0f, 0.0f, 1279.0f, 0.0f));
		walls.add(new Line2D.Float(1279.0f, 0.0f, 1279.0f, 799.0f));
		walls.add(new Line2D.Float(1279.0f, 799.0f, 0.0f, 799.0f));
		walls.add(new Line2D.Float(0.0f, 799.0f, 0.0f, 0.0f));
		//walls.add(new Line2D.Float(640.0f, 200.0f, 640.0f, 600.0f));
		
		flock = new Flock(this);
		
		for (int i = 0; i < INITIAL_BOIDS; ++i) {
//			float x = random(0.0f, width);
//			float y = random(0.0f, height);
			float x = width / 2.0f;
			float y = height / 2.0f;
			flock.addBoid(new Boid(new PVector(x, y), random(1.0f, 3.0f), 0.10f, this));
		}
		
		font = createFont("Helvetica", 12);
	}
	
	/**
	 * Causes the flock to be drawn.
	 */
	public void draw() {
		background(0);
		flock.update();
		
		stroke(255);
		for (Line2D.Float wall : walls) {
			line(wall.x1, wall.y1, wall.x2, wall.y2);
		}
		
		fill(255);
		textFont(font);
		text("" + frameRate + " fps", 16, height - 16);
	}
	
	/**
	 * Handles mouse clicks. In this sketch, clicking a mouse button creates a
	 * new boid at the position of the mouse.
	 */
	public void mousePressed() {
		flock.addBoid(new Boid(new PVector(mouseX, mouseY), 2.0f, 0.05f, this));
	}
	
	/**
	 * Returns the list of walls.
	 * @return an ArrayList of Wall objects
	 */
	public ArrayList<Line2D.Float> getWalls() {
		return walls;
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			PApplet.main(new String[] {"com.jbromley.processing.Flocking"});
		} else {
			PApplet.main(new String[] {args[0], "com.jbromley.processing.Flocking"});
		}
	}
}
