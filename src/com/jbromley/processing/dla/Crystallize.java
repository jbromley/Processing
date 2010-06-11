package com.jbromley.processing.dla;


import processing.core.PApplet;
import processing.core.PFont;


public class Crystallize extends PApplet {

	private static final long serialVersionUID = 6648122436755789567L;
	private static final int ITERATIONS = 200000;

	private Crystal crystal;
	private PFont font;
	private boolean showInfo;
	
	public void setup() {
		size(1680, 1050, P3D);
		frameRate(30);
		colorMode(HSB, 360.0f, 100.0f, 100.0f);
		background(0);
		showInfo = false;

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

		if (showInfo) {
			String info = String.format("%1$4.1f fps  crystal size=%2$d  crystal radius=%3$4.0f", 
					frameRate, crystal.size(), crystal.getRadius());
			textFont(font);
			text(info, 16, 16);
		}
	}
	
	public void keyPressed() {
		switch (key) {
		case 'i': 
			showInfo = !showInfo;
			break;
			
		case 's':
			background(0);
			crystal.draw();
			saveFrame("dla-########.tif");
			break;
			
		case 'r':
			crystal.reset();
			crystal.addParticle(new Particle(width / 2, height / 2));
			break;
		}
	}
	
	public static void main(String[] args) {
		if (args.length == 0) {
			PApplet.main(new String[] {"com.jbromley.processing.crystallize.Crystallize"});
		} else {
			PApplet.main(new String[] {args[0], "com.jbromley.processing.crystallize.Crystallize"});
		}
	}

}
