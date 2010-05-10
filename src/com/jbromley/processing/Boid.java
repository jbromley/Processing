package com.jbromley.processing;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * This class represents a "boid". It represents the location and position of a
 * boid and also contains the steering behaviors.
 * @author <a href="mailto:jbromley@gmail.com">J. Bromley</a>
 */
public class Boid {
	
	private static final int THROB_PERIOD = 500;

	private PVector position;
	private PVector velocity;
	private PVector accel;
	private float startRadius;
	private float radius;
	private float maxForce;    // Maximum steering force
	private float maxSpeed;    // Maximum speed
	private int color;
	private int throbOffset;
	private PApplet parent;

	/**
	 * Create a single boid.
	 * @param pos the initial position of the boid on the screen
	 * @param ms the maximum speed for the boid
	 * @param mf the maximum force that can be applied to a boid
	 * @param owner the PApplet owner of the boid
	 */
	public Boid(PVector pos, float ms, float mf, PApplet owner) {
		parent = owner;
		position = pos.get();
		velocity = new PVector(parent.random(-1, 1), parent.random(-1, 1));
		accel = new PVector(0,0);
		startRadius = 4.0f;
		maxSpeed = ms;
		maxForce = mf;
		color = parent.color(parent.random(0, 256), parent.random(0, 256), 
							 parent.random(0, 256));
		throbOffset = (int) parent.random(0, THROB_PERIOD);
		//color = parent.color(255, 255, 255);
	}

	/**
	 * Advances the state of the boid through a single step
	 * @param boids a list of all Boids
	 */
	public void update(ArrayList<Boid> boids) {
		flock(boids);
		updateMotion();
		checkBoundaries();
		render();
	}

	/**
	 * Calculates and weights the forces from all steering forces.
	 * @param boids a list of all boids
	 */
	private void flock(ArrayList<Boid> boids) {
		PVector separation = separate(boids);
		PVector alignment = align(boids);
		PVector cohesion = cohesion(boids);
		
		// Weight the steering forces.
		separation.mult(1.5f);
		alignment.mult(1.0f);
		cohesion.mult(1.0f);

		// Add the force vectors to acceleration.
		accel.add(separation);
		accel.add(alignment);
		accel.add(cohesion);
	}

	/**
	 * Updates the position of the boids based on applied steering forces.
	 */
	private void updateMotion() {
		// Make boid "throb".
		radius = startRadius * (1.5f + PApplet.sin(throbOffset + 
				(float) parent.millis() / THROB_PERIOD));
		
		// Calculate motion for this step.
		velocity.add(accel);
		velocity.limit(maxSpeed);
		position.add(velocity);

		// Reset the acceleration for the next step.
		accel.mult(0);
	}

	/**
	 * Applies a steering force towards the given target.
	 * @param target the point towards which the boid should steer
	 */
	private void seek(PVector target) {
		accel.add(steer(target, false));
	}

	/**
	 * Applies a braking force when approaching the target.
	 * @param target the point at which the boid should arrive
	 */
	private void arrive(PVector target) {
		accel.add(steer(target,true));
	}

	/**
	 * Calculates a steering vector towards a target. The boid may optionally
	 * brake as it approaches the target.
	 * @param target the point toward which the boid should steer
	 * @param slowdown if true, apply the braking steering behavior on arriving
	 * @return the steering vector needed to move towards the target 
	 */
	private PVector steer(PVector target, boolean slowdown) {
		PVector steer;
		PVector desired = PVector.sub(target, position);
		float d = desired.mag();
		
		// If the distance is greater than 0, calculate the steering force
		// (otherwise return zero vector).
		if (d > 0) {
			desired.normalize();

			// Two options for desired vector magnitude (1 -- based on 
			// distance, 2 -- maxSpeed).
			if (slowdown && d < 100.0f) {
				desired.mult(maxSpeed * (d / 100.0f)); 
			} else {
				desired.mult(maxSpeed);
			}
			// Steering = Desired minus Velocity
			steer = PVector.sub(desired, velocity);
			steer.limit(maxForce);
		} else {
			steer = new PVector(0,0);
		}
		
		return steer;
	}

	/** 
	 * Renders a single frame of the boid.
	 */
	private void render() {
		// Draw a triangle rotated in the direction of velocity
		float theta = velocity.heading2D() + PApplet.HALF_PI;
		parent.fill(color, 128);
		parent.stroke(color);
		parent.pushMatrix();
		parent.translate(position.x, position.y);
		parent.rotate(theta);
		parent.beginShape(PApplet.TRIANGLES);
		parent.vertex(0, -radius * 2);
		parent.vertex(-radius, radius * 2);
		parent.vertex(radius, radius * 2);
		parent.endShape();
		parent.popMatrix();
	}

	/**
	 * Handles boids moving off the edge of the screen. The screen is treated 
	 * as the surface of a torus. Boids moving off the top are wrapped to the 
	 * top of the screen and vice versa.
	 */
	private void checkBoundaries() {
		if (position.x < -radius) { 
			position.x = parent.width + radius;
		} else if (position.x > parent.width + radius) {
			position.x = -radius; 
		}
		if (position.y < -radius) {
			position.y = parent.height + radius;
		} else if (position.y > parent.height + radius) {
			position.y = -radius;
		}
	}

	/**
	 * Calculates a steering force that moves a boid away from nearby boids.
	 * @param boids a list of all boids
	 * @return the steering force to keep this boid separated from the flock
	 */
	private PVector separate(ArrayList<Boid> boids) {
		float desiredSeparation = 20.0f;
		PVector steer = new PVector(0,0,0);
		int count = 0;
		for (Boid other : boids) {
			float d = PVector.dist(position, other.position);
			// If the distance is greater than 0 and less than an arbitrary 
			// amount (0 when you are yourself).
			if (d > 0 && d < desiredSeparation) {
				// Calculate vector pointing away from neighbor
				PVector diff = PVector.sub(position,other.position);
				diff.normalize();
				diff.div(d);
				steer.add(diff);
				count++;
			}
		}
		// Average -- divide by how many
		if (count > 0) {
			steer.div((float)count);
		}

		// As long as the vector is greater than 0
		if (steer.mag() > 0) {
			// Implement Reynolds: Steering = Desired - Velocity
			steer.normalize();
			steer.mult(maxSpeed);
			steer.sub(velocity);
			steer.limit(maxForce);
		}
		return steer;
	}

	  // Alignment
	  // For every nearby boid in the system, calculate the average velocity
	/**
	 * Aligns the boid with the average velocity of nearby boids.
	 * @param boids a list of all boids
	 * @return a vector aligned with the flock's average velocity
	 */
	private PVector align (ArrayList<Boid> boids) {
		float neighborDist = 25.0f;
		PVector steer = new PVector(0,0,0);
		int count = 0;
		for (Boid other : boids) {
			float d = PVector.dist(position, other.position);
			if ((d > 0) && (d < neighborDist)) {
				steer.add(other.velocity);
				count++;
			}
		}
		if (count > 0) {
			steer.div((float) count);
		}

		// As long as the vector is greater than 0
		if (steer.mag() > 0) {
			// Implement Reynolds: Steering = Desired - Velocity
			steer.normalize();
			steer.mult(maxSpeed);
			steer.sub(velocity);
			steer.limit(maxForce);
		}
		return steer;
	}

	// Cohesion
	// For the average location (i.e. center) of all nearby boids, calculate steering vector towards that location
	/**
	 * Calculates steering vector towards average position of neighbors.
	 * @param boids a list of all boids
	 * @return steering force to go towards average position of neighbors
	 */
	private PVector cohesion (ArrayList<Boid> boids) {
		float neighborDist = 25.0f;
		PVector sum = new PVector(0,0);
		int count = 0;
		for (Boid other : boids) {
			float d = position.dist(other.position);
			if ((d > 0) && (d < neighborDist)) {
				sum.add(other.position);
				count++;
			}
		}
		
		if (count > 0) {
			sum.div((float) count);
			return steer(sum, false);  // Steer towards the location
		}
		return sum;
	}
}
