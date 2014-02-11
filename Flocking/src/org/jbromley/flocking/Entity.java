// Entity.java
// Base class for moving objects.
package org.jbromley.flocking;


import processing.core.PVector;


public interface Entity {
  public void setPosition(final PVector position);
  public PVector getPosition();
}

