package com.jbromley.processing.crystallize;

import java.util.ArrayList;
import java.util.LinkedList;

import com.jbromley.processing.util.CellSpacePartition;

import processing.core.PApplet;
import processing.core.PVector;

public class Crystal {
	
	static final float MIN_RADIUS = 20.0f;
	static final float QUERY_RADIUS = 2.0f;
	
	private LinkedList<Particle> crystal;
	private CellSpacePartition<Particle> csp;
	private PVector center;
	private float radius;
	private int color;
	private PApplet p;
	
	public Crystal(PApplet applet) {
		p = applet;
		crystal = new LinkedList<Particle>();
		csp = new CellSpacePartition<Particle>(p.width, p.height, 64, 40);
		radius = MIN_RADIUS;
		color = p.color(255);
	}
	
	public void addParticle(Particle particle) {
		particle.setColor(color);
		crystal.add(particle);
		csp.addEntity(particle);
		if (crystal.size() == 1) {
			center = particle.getPosition().get();
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
			if (dp.mag() <= QUERY_RADIUS) {
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

	public void draw() {
		for (Particle particle : crystal) {
			particle.draw();
		}
	}
}
