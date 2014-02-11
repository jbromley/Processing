// Flock.java
// Top-level class to manage a flock.
package org.jbromley.flocking;


import processing.core.PApplet;
import processing.core.PVector;
import java.awt.geom.Line2D;
import java.util.ArrayList;


/**
 * This class manages a flock of boids. It allows the flock to be updated en 
 * mass and the addition of new boids.
 * @author <a href="mailto:jbromley@gmail.com">J. Bromley</a>
 */
public class Flock {

    private Flocking p;
    private CellSpacePartition<Boid> csp;
    private ArrayList<Boid> boids;
        
    /**
     * Creates an empty flock.
     * @param parent the PApplet that hosts this class
     */
    public Flock(Flocking applet) {
        p = applet;
        boids = new ArrayList<Boid>();
                
        // Size the cells so that we never have to search more
        // than four cells.
        float cellSize = 2.0f * Boid.getNeighborhoodSize();
        int cellsX = (int) (p.width / cellSize);
        int cellsY = (int) (p.height / cellSize);
                
        csp = new CellSpacePartition<Boid>(p.width, p.height, cellsX, cellsY);
    }
        
    /**
     * Updates all boids in the flock.
     */
    public void update() {
        for (Boid boid : boids) {
            boid.update(csp);
        }
    }
        
    /**
     * Adds a new boids.
     * @param b the boid to add
     */
    public void addBoid(Boid b) {
        boids.add(b);
        csp.addEntity(b);
    }
        
    public void setWrapMode(boolean wrap) {
        csp.setWrapMode(wrap);
    }
        
    /**
     * Ensures all boids are inside the walls.
     */
    public void ensureContainment() {
        ArrayList<Line2D.Float> walls = p.getWalls();
        for (Boid boid : boids) {
            int intersections = 0;
            PVector position = boid.getPosition();
            for (Line2D.Float wall : walls) {
                if (wall.intersectsLine(position.x, position.y, 
					Float.MAX_VALUE, position.y)) {
                    ++intersections;
                }
            }
            if (intersections % 2 == 0) {
                boid.setPosition(new PVector(p.width / 2.0f, p.height / 2.0f));
                csp.updateEntity(boid, position);
            }
        }
    }

}
