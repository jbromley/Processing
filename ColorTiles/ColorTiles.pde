// WhiteSquares
// Simulation of the white tile sculpture at SJC.
import peasy.*;

final int TILE_WIDTH = 5;
final int TILE_HEIGHT = 8;
final int TILE_DEPTH = 1;
final int HALF_WIDTH = 4;

PeasyCam camera;
ArrayList<Tile> tiles;


void setup() {
  size(1000, 1000, P3D);
  colorMode(HSB, 1.0);
  
  camera = new PeasyCam(this, 600);
  camera.setMinimumDistance(0);
  camera.setMaximumDistance(1600);
  
  // Set up the tiles.
  tiles = new ArrayList<Tile>();
  for (int x = -HALF_WIDTH; x <= HALF_WIDTH; ++x) {
    for (int y = -HALF_WIDTH; y <= HALF_WIDTH; ++y) {
      for (int z = -HALF_WIDTH; z <= HALF_WIDTH; ++z) {
        int xpos = 3 * x * TILE_WIDTH;
        int ypos = 3 * y * TILE_HEIGHT;
        int zpos = z * 4 * TILE_WIDTH;
        Tile tile = new Tile(xpos, ypos, zpos, TILE_WIDTH, TILE_HEIGHT, TILE_DEPTH, 0.001);
        tiles.add(tile);
      }
    }
  }
}

void draw() {
		background(0);
  for (int i = 0; i < tiles.size(); ++i) {
    Tile tile = tiles.get(i);
    tile.update();
    tile.draw();
  }
}
