package com.jbromley.processing;

import java.util.ArrayList;

/**
 * This class manages a flock of boids. It allows the flock to be updated en 
 * mass and the addition of new boids.
 * @author <a href="mailto:jbromley@gmail.com">J. Bromley</a>
 */
public class Flock {

	private ArrayList<Boid> boids;
	
	/**
	 * Creates an empty flock.
	 */
	public Flock() {
		boids = new ArrayList<Boid>();
	}
	
	/**
	 * Updates all boids in the flock.
	 */
	public void update() {
		for (Boid boid : boids) {
			boid.update(boids);
		}
	}
	
	/**
	 * Adds a new boids.
	 * @param b the boid to add
	 */
	public void addBoid(Boid b) {
		boids.add(b);
	}
}
