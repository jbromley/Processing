// ColorTiles
// Simulation of the white tile sculpture at SJC.
package org.jbromley.colortiles;

import processing.core.*;
import peasy.*;
import java.util.ArrayList;

public class ColorTiles extends PApplet {
    private final static int TILE_WIDTH = 8;
    private final static int TILE_HEIGHT = 8;
    private final static int TILE_DEPTH = 1;
    private final static int HALF_WIDTH = 8;

    private PeasyCam camera;
    private ArrayList<Tile> tiles;

    public void settings()
    {
        size(1600, 1600, "processing.opengl.PGraphics3D");
    }

    public void setup() {
        surface.setTitle("Color Tiles");
        colorMode(HSB, 1.0f);
      
        camera = new PeasyCam(this, 600);
        camera.setMinimumDistance(0);
        camera.setMaximumDistance(1600);
      
        // Set up the tiles.
        tiles = new ArrayList<Tile>();
        for (int x = -HALF_WIDTH; x <= HALF_WIDTH; ++x) {
            for (int y = -HALF_WIDTH; y <= HALF_WIDTH; ++y) {
            for (int z = -HALF_WIDTH; z <= HALF_WIDTH; ++z) {
                int xpos = 3 * x * TILE_WIDTH / 2;
                int ypos = 3 * y * TILE_HEIGHT / 2;
                int zpos = z * 4 * TILE_WIDTH / 2;
                Tile tile = new Tile(xpos, ypos, zpos, TILE_WIDTH, 
                         TILE_HEIGHT, TILE_DEPTH, 0.001f, 
                         this);
                tiles.add(tile);
            }
            }
        }
    }

    public void draw() {
        background(0);
        for (int i = 0; i < tiles.size(); ++i) {
            Tile tile = tiles.get(i);
            tile.update();
            tile.draw();
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[] {"org.jbromley.colortiles.ColorTiles"});
    }
}


