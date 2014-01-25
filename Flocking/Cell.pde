import java.util.LinkedList;


/**
 * This class represents a single cell in a cell space partition. Each
 * cell keeps a list of entities that it contains.
 * @param <T> the type of object that this cell space partition will track
 */
public class Cell<T> {
  /** The entities contained by the cell. */
  public LinkedList<T> members;
    
  /** 
   * Creates a new cell with the given corners. Note that the result is 
   * undefined if the bottom right coordinate is above or to the left of
   * the top left corner.
   * @param topLeft the coordinate of the top left corner of the cell
   * @param bottomRight the coordinate of the bottom right corner of the cell
   */
  public Cell(PVector topLeft, PVector bottomRight) {
    members = new LinkedList<T>();
  }
    
  /**
   * Creates a new cell with the given corners. Note that the result is 
   * undefined if the bottom right coordinate is above or to the left of
   * the top left corner.
   * @param left the x-coordinate of the left side of the cell
   * @param top the y-coordinate of the top of the cell
   * @param right the x-coordinate of the right side of the cell
   * @param bottom the y-coordinate of the bottom of the cell
   */
  public Cell(float left, float top, float right, float bottom) {
    this(new PVector(left, top), new PVector(right, bottom));
  }
}

