// Entity.java
// Interface for drawable entities.
package org.jbromley.crystallize;


import processing.core.PVector;


public interface Entity {
    public void setPosition(final PVector position);
    public PVector getPosition();
}

