// Crystallize
// Two-dimensional diffusion-limited aggregation sketch.
package org.jbromley.crystallize;


import processing.core.*;


public class Crystallize extends PApplet {
    private static final int ITERATIONS = 200000;

    private Crystal crystal;
    private PFont font;
    private boolean showInfo;
	
    public void settings()
    {
        size(1200, 1200, "processing.opengl.PGraphics3D");
    }

    public void setup() {
        surface.setTitle("Crystallize");
        frameRate(30);
        colorMode(HSB, 360.0f, 100.0f, 100.0f);
        background(0);
        showInfo = false;

        // Create the empty crystal and seed it.
        crystal = new Crystal(this);
        crystal.addParticle(new Particle(width / 2, height / 2, this));

        // Create font for showing statistics.
        font = createFont("Helvetica", 10);
    }
	
    public void draw() {
        background(0);

        crystal.update(ITERATIONS);
        crystal.draw();

        if (showInfo) {
            String info = String.format(
            "%1$4.1f fps  crystal size=%2$d  crystal radius=%3$4.0f", 
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
            crystal.addParticle(new Particle(width / 2, height / 2, this));
            break;
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[] {"org.jbromley.crystallize.Crystallize"});
    }
}
