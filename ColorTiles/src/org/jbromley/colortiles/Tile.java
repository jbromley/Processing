package org.jbromley.colortiles;

import processing.core.*;

public class Tile {
    private static final float WALK_STEP = 0.005f;
  
    private int width;
    private int height;
    private int depth;
    private int x;
    private int y;
    private int z;
    private float duty;
    private boolean lit = true;
    private float h = 0.5f;
    private float s = 0.5f;
    private float b = 1.0f;
    private float a = 1.0f;
    private PApplet p = null;
  
    public Tile(int xx, int yy, int zz, int tileWidth, int tileHeight, 
		int tileDepth, float litDuty, PApplet applet) {
	p = applet;
	x = xx;
	y = yy;
	z = zz;
	width = tileWidth;
	height = tileHeight;
	depth = tileDepth;
	duty = litDuty;
	h = p.random(1.0f);
	lit = (p.random(1.0f) < duty ? true : false);
    }
  
    public void update() {
	if (p.random(1.0f) < duty) {
	    lit = !lit;
	}
    
	// Random walk each color.
	h = randomWalk(h, true);
	s = randomWalk(s, false);
	b = randomWalk(b, false);
    }
  
    public void draw() {
	p.pushMatrix();
	p.translate(x, y, z);
	p.stroke(h, s, b, 0.25f);
	if (lit) {
	    p.fill(0.8f * h, 0.8f * s, 0.8f * b, 0.25f);
	} else {
	    p.fill(0.5f * h, 0.5f * s, 0.5f * b, 0.125f);
	}
	p.box(width, height, depth);
	p.popMatrix();
    }
  
    private float randomWalk(float v, boolean wrap) {
	float r = p.random(1.0f);
	if (r < 0.33) {
	    v -= 0.01;
	} else if (r > 0.67) {
	    v += 0.01;
	}
 
	if (v > 1.0) {
	    v = (wrap ? 0.0f : 1.0f);
	} else if (v < 0) {
	    v = (wrap ? 1.0f : 0.0f);
	}
	return v;
    }
  
}
