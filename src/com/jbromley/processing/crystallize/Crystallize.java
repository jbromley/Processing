package com.jbromley.processing.crystallize;

import java.util.ArrayList;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public class Crystallize extends PApplet {

	private static final long serialVersionUID = 6648122436755789567L;
	private static final int INITIAL_PARTICLES = 1;
	private static final int ITERATIONS = 20000;
	private static float INNER_RADIUS = 1.1f;
	private static float OUTER_RADIUS = 2.0f;
	private static float RING_RADIUS = 2;

	private Crystal crystal;
	private LinkedList<Particle> particles;
	private int particleColor;
	private  PFont font;
	
	public void setup() {
		size(800, 800, P3D);
		frameRate(120);
		colorMode(HSB, 360.0f, 100.0f, 100.0f);
		background(0);
		
		// Create the empty crystal and seed it.
		crystal = new Crystal(this);
		crystal.addParticle(new Particle(width / 2, height / 2, color(255)));
	
		// Create the random particles.
		Particle.setApplet(this);
		particles = new LinkedList<Particle>();
		particleColor = color(0, 0, 100);
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
		
		PVector crystalCenter = crystal.getCenter();
		float innerRadius = INNER_RADIUS * crystal.getRadius() + 1.0f;
		float outerRadius = OUTER_RADIUS * crystal.getRadius();
		//float outerRadius = innerRadius + RING_RADIUS;

		//for (Particle particle : particles) {
		for (int iter = 0; iter < ITERATIONS; ++iter) {
			Particle particle = particles.get(0);
			particle.update();
			
			if (crystal.isTouching(particle)) {
				crystal.addParticle(particle);
				deleteParticles.add(particle);
				newParticles.add(randomizeParticle());
				break;
			}
			
			//particle.draw();
			
			// Check if the particle has wandered too far.
			float distance = PVector.dist(crystalCenter, particle.getPosition());
			if (distance > outerRadius) {
				particle.reposition(crystalCenter, innerRadius, outerRadius);
			}
		}

		for (Particle particle : deleteParticles) {
			particles.remove(particle);
		}
		
		for (Particle particle : newParticles) {
			particles.add(particle);
		}
		
		// Scale number of free particles to crystal radius.
//		int numberParticles = Math.max(INITIAL_PARTICLES, (int) (4 * crystal.getRadius()));
//		if (numberParticles > particles.size()) {
//			for (int i = particles.size(); i < numberParticles; ++i) {
//				particles.add(randomizeParticle());
//			}
//		}

		// Draw the crystal.
		crystal.draw();

		String info = String.format("%1$4.1f fps  crystal size=%2$d  crystal radius=%3$4.0f", 
				frameRate, crystal.size(), crystal.getRadius());
		textFont(font);
		text(info, 16, 16);
	}
	
	private Particle randomizeParticle() {
		float innerRadius = INNER_RADIUS * crystal.getRadius() + 1.0f;
		float outerRadius = OUTER_RADIUS * crystal.getRadius();
		Particle particle = new Particle(crystal.getCenter(), innerRadius, 
				outerRadius, particleColor);
		
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
			PApplet.main(new String[] {args[0], "com.jbromley.processing.crystallize.Crystallize"});
		}
	}

}
