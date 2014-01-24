import peasy.*;

PeasyCam camera;

void setup() {
  size(320, 320, P3D);
  camera = new PeasyCam(this, 160);
  camera.setMinimumDistance(50);
  camera.setMaximumDistance(500);
}

void draw() {
  rotateX(-0.5);
  rotateY(-0.5);
  background(0);
  fill(255, 0, 0);
  box(30);
  pushMatrix();
  translate(0, 0, 20);
  fill(0, 0, 255);
  box(5);
  fill(240, 240, 240, 128);
  stroke(255, 255, 255);
  translate(0, 0, 10);
  box(25, 25, 1);
  popMatrix();
}
