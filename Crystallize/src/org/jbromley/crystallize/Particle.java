import java.awt.geom.Rectangle2D;

public class Particle implements Entity {

  private PVector position;
  private int colour;
  
  public Particle(final float x, final float y) {
    position = new PVector(x, y);
  }
  
  public Particle(PVector center, final float innerRadius, 
      final float outerRadius) {
    reposition(center, innerRadius, outerRadius);
  }
  
  public void update() {
    int direction = (int) random(4);
    switch (direction) {
      case 0: position.y += 1; break;
      case 1: position.x += 1; break;
      case 2: position.y -= 1; break;
      case 3: position.x -= 1; break;
    }
  }
  
  public void reposition(PVector crystalCenter, final float innerRadius, 
      final float outerRadius) {
    Rectangle2D.Float boundingBox = new Rectangle2D.Float(0, 0, 
        width, height);
    PVector newPosition = null;
    
    do {
      float radius = random(innerRadius, outerRadius);
      float theta = random(0.0f, 2 * PApplet.PI);
      PVector offset = new PVector(radius * cos(theta), 
          radius * sin(theta));
    
      newPosition = PVector.add(crystalCenter, offset);
    } while (!boundingBox.contains(newPosition.x, newPosition.y));
      
    position = newPosition;
  }
  
  public void draw() {
    stroke(colour);
    point(position.x, position.y);
  }
  
  public void draw(int colour) {
    stroke(colour);
    point(position.x, position.y);
  }

  public void setColor(int colour) {
    this.colour = colour;
  }
  
  @Override
  public PVector getPosition() {
    return position;
  }

  @Override
  public void setPosition(PVector position) {
    this.position = position; 
  }
}

