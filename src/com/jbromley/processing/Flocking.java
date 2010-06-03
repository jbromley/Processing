package com.jbromley.processing;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

/**
 * A flock of "boids".
 * @author <a href="mailto:jbromley@gmail.com">J. Bromley</a>
 */
public class Flocking extends PApplet {
	
	private static final long serialVersionUID = 9221726134245604843L;

	private static final int INITIAL_BOIDS = 1000;
	
	private Flock flock = null;
	private ArrayList<Line2D.Float> walls = null;
	private PFont font = null;
	
	private float alignment = 1.0f;
	private float cohesion = 1.0f;
	private float separation = 1.0f;
	private float neighborhoodSize = 25.0f;
	private float separationDistance = 20.0f;
	private boolean useWalls = true;
	private boolean showInfo = true;
	
	/**
	 * Creates the flock boids demo. 
	 */
	public void setup() {
		size(screen.width, screen.height, P3D);
		
		// Create walls
		float d = 160.0f;
		walls = new ArrayList<Line2D.Float>();
		walls.add(new Line2D.Float(d, 1, width - d, 1));
		walls.add(new Line2D.Float(width - d, 1, width - 1, d));
		walls.add(new Line2D.Float(width - 1, d, width - 1, height - d));
		walls.add(new Line2D.Float(width - 1, height - d, width - d, height - 1));
		walls.add(new Line2D.Float(width - d, height - 1, d, height - 1));
		walls.add(new Line2D.Float(d, height - 1, 1, height - d));
		walls.add(new Line2D.Float(1, height - d, 1, d));
		walls.add(new Line2D.Float(1, d, d, 1));
//		walls.add(new Line2D.Float(0.0f, 0.0f, w, 0.0f));
//		walls.add(new Line2D.Float(w, 0.0f, w, h));
//		walls.add(new Line2D.Float(w, h, 0.0f, h));
//		walls.add(new Line2D.Float(0.0f, h, 0.0f, 0.0f));
		
		flock = new Flock(this);
		for (int i = 0; i < INITIAL_BOIDS; ++i) {
//			float x = random(0.0f, width);
//			float y = random(0.0f, height);
			float x = width / 2.0f;
			float y = height / 2.0f;
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
		
//		stroke(255);
//		for (Line2D.Float wall : walls) {
//			line(wall.x1, wall.y1, wall.x2, wall.y2);
//		}
		
		if (showInfo) {
			String info = String.format("%1$4.1f fps  alignment: %2$4.1f  " +
					"cohesion: %3$4.1f  separation: %4$4.1f  " +
					"neighborhood: %5$4.1f  separation: %6$4.1f", frameRate,
					alignment, cohesion, separation, neighborhoodSize, 
					separationDistance);
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
		case 'Z': alignment -= 1.0f; break;
		case 'z': alignment -= 0.1f; break;
		case 'a': alignment += 0.1f; break;
		case 'A': alignment += 1.0f; break;
		
		case 'C': cohesion -= 1.0f; break;
		case 'c': cohesion -= 0.1f; break;
		case 'd': cohesion += 0.1f; break;
		case 'D': cohesion += 1.0f; break;
		
		case 'X': separation -= 1.0f; break;
		case 'x': separation -= 0.1f; break;
		case 's': separation += 0.1f; break;
		case 'S': separation += 1.0f; break;
		
		case 'N':
		case 'n':
			neighborhoodSize -= 1.0f;
			break;
		case 'h':
		case 'H':
			neighborhoodSize += 1.0f;
			break;
			
		case 'M':
		case 'm':
			separationDistance -= 1.0f;
			break;
		case 'j':
		case 'J':
			separationDistance += 1.0f;
			break;
			
		case 'w': useWalls = !useWalls; break;
		case 'i': showInfo = !showInfo; break;

		default:
			break;
		}
	}

	public float getAlignment() {
		return alignment;
	}
	
	public float getCohesion() {
		return cohesion;
	}
	
	public float getSeparation() {
		return separation;
	}
	
	public float getNeighborhoodSize() {
		return neighborhoodSize;
	}
	
	public float getSeparationDistance() {
		return separationDistance;
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

	public static void main(String[] args) {
		if (args.length == 0) {
			PApplet.main(new String[] {"com.jbromley.processing.Flocking"});
		} else {
			PApplet.main(new String[] {args[0], "com.jbromley.processing.Flocking"});
		}
	}
}
