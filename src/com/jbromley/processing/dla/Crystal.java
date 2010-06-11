package com.jbromley.processing.dla;

import java.util.ArrayList;
import java.util.LinkedList;

import com.jbromley.processing.util.CellSpacePartition;

import processing.core.PApplet;
import processing.core.PVector;

public class Crystal {
	
	static final float MIN_RADIUS = 20.0f;
	static final float INNER_RADIUS_SCALE = 1.1f;
	static final float OUTER_RADIUS_SCALE = 2.0f;
	static final float QUERY_RADIUS = 2.0f;
	static final float MIN_DISTANCE = (float) Math.sqrt(2.0f);
	static final float SPARK_RADIUS = 4;
	
	private LinkedList<Particle> crystal;
	private CellSpacePartition<Particle> csp;
	private PVector center;
	private float radius;

	private Particle freeParticle;
	ArrayList<PVector> stickingParticles;
	
	private PApplet p;
	
	public Crystal(PApplet applet) {
		p = applet;
		crystal = new LinkedList<Particle>();
		csp = new CellSpacePartition<Particle>(p.width, p.height, 128, 80);
		radius = MIN_RADIUS;
		stickingParticles = new ArrayList<PVector>();
	}
	
	public void addParticle(Particle particle) {
		crystal.add(particle);
		csp.addEntity(particle);
		if (crystal.size() == 1) {
			center = particle.getPosition().get();
			freeParticle = createParticle();
		}
		float distance = PVector.dist(getCenter(), particle.getPosition());
		if (distance > radius) {
			setRadius(distance);
		}
	}
	
	public boolean isTouching(Particle particle) {
		boolean touching = false;
		ArrayList<Particle> neighbors = csp.getNeighborList(
				particle.getPosition(), QUERY_RADIUS);
		
		for (Particle crystalParticle : neighbors) {
			PVector dp = PVector.sub(crystalParticle.getPosition(), 
					particle.getPosition());
			if (dp.mag() <= MIN_DISTANCE) {
				touching = true;
				break;
			}
		}
		
		return touching;
	}
	
	public int size() {
		return crystal.size();
	}
	

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

	public PVector getCenter() {
		return center;
	}
	
	public void update(int iterations) {
		float innerRadius = INNER_RADIUS_SCALE * radius + 1.0f;
		float outerRadius = OUTER_RADIUS_SCALE * radius;
		//float outerRadius = innerRadius + RING_RADIUS;

		for (int i = 0; i < iterations; ++i) {
			freeParticle.update();
			
			if (isTouching(freeParticle)) {
				addParticle(freeParticle);
				stickingParticles.add(freeParticle.getPosition());
				innerRadius = INNER_RADIUS_SCALE * radius + 1.0f;
				outerRadius = OUTER_RADIUS_SCALE * radius;
				freeParticle = createParticle();
			}
			
			// Check if the particle has wandered too far.
			float distance = PVector.dist(center, freeParticle.getPosition());
			if (distance > outerRadius) {
				freeParticle.reposition(center, innerRadius, outerRadius);
			}
		}
	}

	public void draw() {
		int numParticles = crystal.size();
		int particleIndex = 0;
		for (Particle particle : crystal) {
			float hue = 240.0f * (float) particleIndex / numParticles;
			int color = p.color(hue, 100, 100);
			particle.draw(color);
			++particleIndex;
		}
		
		p.pushStyle();
		p.stroke(0, 0, 100);
		p.fill(0, 0, 100);
		for (PVector pos : stickingParticles) {
			p.ellipse(pos.x, pos.y, SPARK_RADIUS, SPARK_RADIUS);
		}
		p.popStyle();
		stickingParticles.clear();
	}
	
	public void reset() {
		crystal.clear();
		csp.clear();
		radius = MIN_RADIUS;
		freeParticle = createParticle();
	}
	
	private Particle createParticle() {
		float innerRadius = INNER_RADIUS_SCALE * radius + 1.0f;
		float outerRadius = OUTER_RADIUS_SCALE * radius;
		Particle particle = new Particle(center, innerRadius, 
				outerRadius);
		
		return particle;
	}
}
