public class Sphere {
  
  private PVector pos;
  private float radius;
  private int colour;
    
  public Sphere(PVector initPos, float radius) {
    pos = initPos;
    this.radius = radius;
    colour = color(random(0, 255), random(0, 255), random(0, 255), random(0, 255));
  }
      
  public void draw() {
    pushMatrix();
    noStroke();
    fill(colour);
    translate(pos.x, pos.y, pos.z);
    sphere(radius);
    popMatrix();
  }
}

