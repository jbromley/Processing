public class Tile {
  final float WALK_STEP = 0.005;
  
  private int m_width;
  private int m_height;
  private int m_depth;
  private int m_x;
  private int m_y;
  private int m_z;
  private float m_duty;
  private boolean m_lit = true;
  private float m_h = 0.5;
  private float m_s = 0.5;
  private float m_b = 1.0;
  private float m_alpha = 1.0;
  
  public Tile(int x, int y, int z, int w, int h, int d, float duty) {
    m_x = x;
    m_y = y;
    m_z = z;
    m_width = w;
    m_height = h;
    m_depth = d;
    m_duty = duty;
    m_lit = (random(1.0) < m_duty ? true : false);
  }
  
  public void update() {
    if (random(1.0) < m_duty) {
      m_lit = !m_lit;
    }
    
    // Random walk each color.
    m_h = randomWalk(m_h, true);
    m_s = randomWalk(m_s, false);
    m_b = randomWalk(m_b, false);
  }
  
  public void draw() {
    pushMatrix();
    translate(m_x, m_y, m_z);
    stroke(m_h, m_s, m_b, 0.75);
    if (m_lit) {
      fill(0.8 * m_h, 0.8 * m_s, 0.8 * m_b, 0.7);
    } else {
      fill(0.5 * m_h, 0.5 * m_s, 0.5 * m_b, 0.5);
    }
    box(m_width, m_height, m_depth);
    popMatrix();
  }
  
  private float randomWalk(float v, boolean wrap) {
    float r = random(1.0);
    if (r < 0.33) {
      v -= 0.01;
    } else if (r > 0.67) {
      v += 0.01;
    }
 
    if (v > 1.0) {
      v = (wrap ? 0.0 : 1.0);
    } else if (v < 0) {
      v = (wrap ? 1.0 : 0.0);
    }
    return v;
  }
  
}
