package com.jbromley.processing;

import java.util.ArrayList;

import processing.core.PApplet;

/**
 * This class manages a flock of boids. It allows the flock to be updated en 
 * mass and the addition of new boids.
 * @author <a href="mailto:jbromley@gmail.com">J. Bromley</a>
 */
public class Flock {

	private CellSpacePartition<Boid> csp;
	private ArrayList<Boid> boids;
	
	/**
	 * Creates an empty flock.
	 * @param parent the PApplet that hosts this class
	 */
	public Flock(PApplet parent) {
		boids = new ArrayList<Boid>();
		csp = new CellSpacePartition<Boid>(parent.width, parent.height, 32, 18);
	}
	
	/**
	 * Updates all boids in the flock.
	 */
	public void update() {
		for (Boid boid : boids) {
			boid.update(csp);
		}
	}
	
	/**
	 * Adds a new boids.
	 * @param b the boid to add
	 */
	public void addBoid(Boid b) {
		boids.add(b);
		csp.addEntity(b);
	}
}
