package com.jbromley.processing;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * A flock of "boids".
 * @author <a href="mailto:jbromley@gmail.com">J. Bromley</a>
 */
public class Flocking extends PApplet {
	
	private static final long serialVersionUID = 9221726134245604843L;

	private static final int INITIAL_BOIDS = 250;
	
	private Flock flock = null;
	
	/**
	 * Creates the flock boids demo. 
	 */
	public void setup() {
		size(1280, 800);
		
		flock = new Flock();
		
		for (int i = 0; i < INITIAL_BOIDS; ++i) {
			flock.addBoid(new Boid(new PVector(width / 2, height / 2), 3.0f, 0.05f, this));
		}
	}
	
	/**
	 * Causes the flock to be drawn.
	 */
	public void draw() {
		background(0);
		flock.update();
	}
	
	/**
	 * Handles mouse clicks. In this sketch, clicking a mouse button creates a
	 * new boid at the position of the mouse.
	 */
	public void mousePressed() {
		flock.addBoid(new Boid(new PVector(mouseX, mouseY), 2.0f, 0.05f, this));
	}

	public static void main(String[] args) {
		//PApplet.main(new String[] {"--present", "com.jbromley.processing.Flocking"});
		if (args.length == 0) {
			PApplet.main(new String[] {"com.jbromley.processing.Flocking"});
		} else {
			PApplet.main(new String[] {args[0], "com.jbromley.processing.Flocking"});
		}
	}
}
