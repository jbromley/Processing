package com.jbromley.processing.crystallize;

import java.util.ArrayList;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public class Crystallize extends PApplet {

	private static final long serialVersionUID = 6648122436755789567L;
	private static final int INITIAL_PARTICLES = 100;
	private static float INNER_RADIUS = 1.10f;
	private static float OUTER_RADIUS = 2.0f;

	private Crystal crystal;
	private LinkedList<Particle> particles;
	private int particleColor;
	private  PFont font;
	
	public void setup() {
		size(1600, 1000, P3D);
		background(0);
		
		// Create the empty crystal and seed it.
		crystal = new Crystal(this);
		crystal.addParticle(new Particle(width / 2, height / 2, color(255)));
	
		// Create the random particles.
		Particle.setApplet(this);
		particles = new LinkedList<Particle>();
		particleColor = color(0, 255, 0);
		for (int i = 0; i < INITIAL_PARTICLES; ++i) {
			particles.add(randomizeParticle());
		}
		
		font = createFont("Helvetica", 12);
		textMode(SCREEN);
	}
	
	public void draw() {
		ArrayList<Particle> deleteParticles = new ArrayList<Particle>();
		ArrayList<Particle> newParticles = new ArrayList<Particle>();
		background(0);
		
		for (Particle particle : particles) {
			particle.update();
			
			if (crystal.isTouching(particle)) {
				crystal.addParticle(particle);
				deleteParticles.add(particle);
				newParticles.add(randomizeParticle());
			}
			
			particle.draw();
			
			// Check if the particle has wandered too far.
			float distance = PVector.dist(crystal.getCenter(), particle.getPosition());
			if (distance > OUTER_RADIUS * crystal.getRadius()) {
				deleteParticles.add(particle);
				newParticles.add(randomizeParticle());
			}
		}

		for (Particle particle : deleteParticles) {
			particles.remove(particle);
		}
		
		for (Particle particle : newParticles) {
			particles.add(particle);
		}

		// Draw the crystal.
		crystal.draw();

		String info = String.format("%1$4.1f fps  crystal size=%2$d  crystal radius=%3$4.0f", 
				frameRate, crystal.size(), crystal.getRadius());
		textFont(font);
		text(info, 16, 16);
	}
	
	private Particle randomizeParticle() {
		float radius = crystal.getRadius();
		Particle particle = new Particle(crystal.getCenter(), 
				INNER_RADIUS * radius, OUTER_RADIUS * radius, particleColor);
		
		PVector position = particle.getPosition();
		if (position.x < 0) {
			position.x = 0;
		} else if (position.x >= width) {
			position.x = width - 1;
		}
		if (position.y < 0) {
			position.y = 0;
		} else if (position.y >= height) {
			position.y = height - 1;
		}
		
		return particle;
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			PApplet.main(new String[] {"com.jbromley.processing.crystallize.Crystallize"});
		} else {
			PApplet.main(new String[] {args[0], "com.jbromley.processing.flocking.Crystallize"});
		}
	}

}
