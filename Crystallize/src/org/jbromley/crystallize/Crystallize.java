private static final int ITERATIONS = 200000;

private Crystal crystal;
private PFont font;
private boolean showInfo;
	
public void setup() {
  size(800, 800, P3D);
		frameRate(30);
		colorMode(HSB, 360.0f, 100.0f, 100.0f);
		background(0);
		showInfo = false;


		// Create the empty crystal and seed it.
		crystal = new Crystal(this);
		crystal.addParticle(new Particle(width / 2, height / 2));
		
		font = createFont("Helvetica", 10);
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

