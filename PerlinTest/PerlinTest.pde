import peasy.*;

PeasyCam camera;
float noiseScale=0.02;

void setup() {
  size(400, 400, P3D);
  camera = new PeasyCam(this, 200);
  camera.setMinimumDistance(0);
  camera.setMaximumDistance(400);
}

void draw() {
  background(0);
  for (int x = -200; x < width / 2; ++x) {
    for (int y = -200; y < height / 2; ++y) {
      float noiseVal = noise((mouseX + x) * noiseScale, (mouseY + y) *noiseScale);
      stroke(noiseVal * 255);
      // line(x, mouseY+noiseVal*80, x, height);
      point(x, y, -128 + noiseVal * 255);
    }
  }
}
