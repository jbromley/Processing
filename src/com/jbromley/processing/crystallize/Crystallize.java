package com.jbromley.processing.crystallize;


import processing.core.PApplet;
import processing.core.PFont;


public class Crystallize extends PApplet {

	private static final long serialVersionUID = 6648122436755789567L;
	private static final int ITERATIONS = 160000;

	private Crystal crystal;
	private  PFont font;
	
	public void setup() {
		size(1024, 1024, P3D);
		frameRate(30);
		colorMode(HSB, 360.0f, 100.0f, 100.0f);
		background(0);

		Particle.setApplet(this);

		// Create the empty crystal and seed it.
		crystal = new Crystal(this);
		crystal.addParticle(new Particle(width / 2, height / 2));
		
		font = createFont("Helvetica", 10);
		textMode(SCREEN);
	}
	
	public void draw() {
		background(0);

		crystal.update(ITERATIONS);
		crystal.draw();

		String info = String.format("%1$4.1f fps  crystal size=%2$d  crystal radius=%3$4.0f", 
				frameRate, crystal.size(), crystal.getRadius());
		textFont(font);
		text(info, 16, 16);
	}
	
	public static void main(String[] args) {
		if (args.length == 0) {
			PApplet.main(new String[] {"com.jbromley.processing.crystallize.Crystallize"});
		} else {
			PApplet.main(new String[] {args[0], "com.jbromley.processing.crystallize.Crystallize"});
		}
	}

}
