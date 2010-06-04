package com.jbromley.processing;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * This class manages a flock of boids. It allows the flock to be updated en 
 * mass and the addition of new boids.
 * @author <a href="mailto:jbromley@gmail.com">J. Bromley</a>
 */
public class Flock {

	private CellSpacePartition<Boid> csp;
	private ArrayList<Boid> boids;
	private Flocking parent;
	
	/**
	 * Creates an empty flock.
	 * @param parent the PApplet that hosts this class
	 */
	public Flock(Flocking owner) {
		parent = owner;
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
	
	/**
	 * Ensures all boids are inside the walls.
	 */
	public void ensureContainment() {
		ArrayList<Line2D.Float> walls = parent.getWalls();
		for (Boid boid : boids) {
			int intersections = 0;
			PVector position = boid.getPosition();
			for (Line2D.Float wall : walls) {
				if (wall.intersectsLine(position.x, position.y, Float.MAX_VALUE, position.y)) {
					++intersections;
				}
			}
			if (intersections % 2 == 0) {
				boid.setPosition(new PVector(parent.width / 2.0f, parent.height / 2.0f));
				csp.updateEntity(boid, position);
			}
		}
	}
}
