// Flocking.java
package org.jbromley.flocking;


import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import java.awt.geom.Line2D;
import java.util.ArrayList;


public class Flocking extends PApplet {
    private static final long serialVersionUID = 9221726134245604843L;
    private static final int INITIAL_BOIDS = 800;
        
    private Flock flock = null;
    private ArrayList<Line2D.Float> walls = null;
    private PFont font = null;
        
    private boolean useWalls = true;
    private boolean showWalls = false;
    private boolean showInfo = true;
        
    /**
     * Creates the flock boids demo. 
     */
    public void setup() {
        //size(screen.width, screen.height, P3D);
        size(1600, 1000, P3D);
                
        // Create walls
        float d = width / 8.0f;
        walls = new ArrayList<Line2D.Float>();
        walls.add(new Line2D.Float(d, 1, width - d, 1));
        walls.add(new Line2D.Float(width - d, 1, width - 1, d));
        walls.add(new Line2D.Float(width - 1, d, width - 1, height - d));
        walls.add(new Line2D.Float(width - 1, height - d, width - d, height - 1));
        walls.add(new Line2D.Float(width - d, height - 1, d, height - 1));
        walls.add(new Line2D.Float(d, height - 1, 1, height - d));
        walls.add(new Line2D.Float(1, height - d, 1, d));
        walls.add(new Line2D.Float(1, d, d, 1));
                
        flock = new Flock(this);
        float rMax = Math.min(width, height) / 8.0f;
        for (int i = 0; i < INITIAL_BOIDS; ++i) {
            float r = rMax * (float) Math.sqrt(random(1.0f));
            float theta = random(TWO_PI);
            float x = (float) (width / 2.0f + r * Math.cos(theta));
            float y = (float) (height / 2.0f + r * Math.sin(theta));
            flock.addBoid(new Boid(new PVector(x, y), random(1.0f, 3.0f), 0.10f, this));
        }
                
        font = createFont("Helvetica", 12);
    }
        
    /**
     * Causes the flock to be drawn.
     */
    public void draw() {
        background(0);
        flock.update();
                
        if (showWalls) {
            stroke(255);
            for (Line2D.Float wall : walls) {
                line(wall.x1, wall.y1, wall.x2, wall.y2);
            }
        }
                
        if (showInfo) {
            String info = String.format("%1$4.1f fps  alignment: %2$4.1f  " +
                                        "cohesion: %3$4.1f  separation: %4$4.1f  " +
                                        "neighborhood: %5$4.1f  separation: %6$4.1f", frameRate,
                                        Boid.getAlignment(), Boid.getCohesion(), 
                                        Boid.getSeparation(), Boid.getNeighborhoodSize(), 
                                        Boid.getSeparationDistance());
            fill(255);
            textFont(font);
            text(info, 16, 36);
        }
    }
        
    /**
     * Handles mouse clicks. In this sketch, clicking a mouse button creates a
     * new boid at the position of the mouse.
     */
    public void mousePressed() {
        flock.addBoid(new Boid(new PVector(mouseX, mouseY), 2.0f, 0.05f, this));
    }
        
    public void keyPressed() {
        switch (key) {
        case 'Z': Boid.setAlignment(Boid.getAlignment() - 1.0f); break;
        case 'z': Boid.setAlignment(Boid.getAlignment() - 0.1f); break;
        case 'a': Boid.setAlignment(Boid.getAlignment() + 0.1f); break;
        case 'A': Boid.setAlignment(Boid.getAlignment() + 1.0f); break;
                
        case 'C': Boid.setCohesion(Boid.getCohesion() - 1.0f); break;
        case 'c': Boid.setCohesion(Boid.getCohesion() - 0.1f); break;
        case 'd': Boid.setCohesion(Boid.getCohesion() + 0.1f); break;
        case 'D': Boid.setCohesion(Boid.getCohesion() + 1.0f); break;
                
        case 'X': Boid.setSeparation(Boid.getSeparation() - 1.0f); break;
        case 'x': Boid.setSeparation(Boid.getSeparation() - 0.1f); break;
        case 's': Boid.setSeparation(Boid.getSeparation() + 0.1f); break;
        case 'S': Boid.setSeparation(Boid.getSeparation() + 1.0f); break;
                
        case 'N':
        case 'n': Boid.setNeighborhoodSize(Boid.getNeighborhoodSize() - 1.0f); 
	    break;
        case 'h':
        case 'H': Boid.setNeighborhoodSize(Boid.getNeighborhoodSize() + 1.0f); 
	    break;
                        
        case 'M':
        case 'm': Boid.setSeparationDistance(Boid.getSeparationDistance() - 1.0f); break;
        case 'j':
        case 'J': Boid.setSeparationDistance(Boid.getSeparationDistance() + 1.0f); break;
                        
        case 'w': 
            useWalls = !useWalls;
            if (useWalls) {
                flock.setWrapMode(false);
                flock.ensureContainment();
            } else {
                flock.setWrapMode(true);
                showWalls = false;
            }
            break;

        case 'q': showWalls = !showWalls; break;
        case 'i': showInfo = !showInfo; break;

        default: break;
        }
    }

    public boolean getUseWalls() {
        return useWalls;
    }
        
    /**
     * Returns the list of walls.
     * @return an ArrayList of Wall objects
     */
    public ArrayList<Line2D.Float> getWalls() {
        return walls;
    }

    public static void main(String args[]) {
	PApplet.main(new String[] {"org.jbromley.flocking.Flocking"});
    }

}
