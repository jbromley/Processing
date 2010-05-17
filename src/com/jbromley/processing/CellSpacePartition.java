package com.jbromley.processing;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PVector;


/**
 * This class divides a geometric space into cells. Each cell keeps track of
 * the entities within it. This allows us to query for objects within a certain
 * area without having to iterate over all entities.
 * @param <T> the type of object that this cell space partition will track
 * @author <a href="mailto:jbromley@gmail.com">J. Bromley</a>
 */
public class CellSpacePartition<T extends Boid> {

	/**
	 * This class represents a single cell in a cell space partition. Each
	 * cell keeps a list of entities that it contains.
	 * @param <T> the type of object that this cell space partition will track
	 */
	private static class Cell<T> {
		/** The entities contained by the cell. */
		public ArrayList<T> members;
		
		/** The bounding box for the cell. */
		public Rectangle2D.Float boundingBox;
		
		/** 
		 * Creates a new cell with the given corners. Note that the result is 
		 * undefined if the bottom right coordinate is above or to the left of
		 * the top left corner.
		 * @param topLeft the coordinate of the top left corner of the cell
		 * @param bottomRight the coordinate of the bottom right corner of the cell
		 */
		public Cell(PVector topLeft, PVector bottomRight) {
			members = new ArrayList<T>();
			boundingBox = new Rectangle2D.Float(topLeft.x, topLeft.y,
					bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
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
			members = new ArrayList<T>();
			boundingBox = new Rectangle2D.Float(left, top, right - left, bottom - top);
		}
	}
	
	private ArrayList<CellSpacePartition.Cell<T>> cells;
	private ArrayList<Boid> neighbors;
	private float spaceWidth;
	private float spaceHeight;
	private int numberCellsX;
	private int numberCellsY;
	private int maxEntities;
	private float cellWidth;
	private float cellHeight;
	
	/**
	 * Creates a new cell space partition. 
	 * @param width the width of the space to be divided
	 * @param height the height of the space to be dividedd
	 * @param cellsX the number of horizontal cells
	 * @param cellsY the number of vertical cells
	 * @param maxEntities the maximum number of entities to allow in a cell
	 */
	public CellSpacePartition(float width, float height, int cellsX, int cellsY,
			int maxMembers) {
		spaceWidth = width;
		spaceHeight = height;
		numberCellsX = cellsX;
		numberCellsY = cellsY;
		maxEntities = maxMembers;
		
		cellWidth = spaceWidth / numberCellsX;
		cellHeight = spaceHeight / numberCellsY;
		
		for (int y = 0; y < numberCellsY; ++y) {
			for (int x = 0; x < numberCellsX; ++x) {
				float top = y * cellHeight;
				float left = x * cellWidth;
				float bottom = top + cellHeight;
				float right = left + cellWidth;
				cells.add(new Cell<T>(left, top, right, bottom));
			}
		}
	}
	
	/**
	 * Adds an entity to the cell space partition.
	 * @param entity the entity to be added.
	 */
	public void addEntity(T entity) {
		int index = positionToIndex(entity.getPosition());
		cells.get(index).members.add(entity);
	}
	
	/**
	 * Updates an entity already in the cell space partition.
	 * @param entity the entity to be updated
	 * @param oldPosition the previous position of the entity
	 */
	public void updateEntity(T entity, PVector oldPosition) {
		int oldIndex = positionToIndex(oldPosition);
		int newIndex = positionToIndex(entity.getPosition());
		
		if (oldIndex != newIndex) {
			cells.get(oldIndex).members.remove(entity);
			cells.get(newIndex).members.add(entity);
		}
	}
	
	public ArrayList<T> getNeighborList(PVector targetPosition, float queryRadius) {
		ArrayList<T> neighbors = new ArrayList<T>();
		
		// Create the bounding box for the query.
		float left = targetPosition.x - queryRadius;
		float top = targetPosition.y - queryRadius;
		float width = 2.0f * queryRadius;
		float height = 2.0f * queryRadius;
		Rectangle2D.Float queryBox = new Rectangle2D.Float(left, top, width, height);
		
		// Iterate over each cell and test if its bounding box overlaps the
		// query box. If it does overlap and it contains entities, do further
		// proximity tests.
		for (Cell<T> cell : cells) {
			if (!cell.members.isEmpty() && cell.boundingBox.intersects(queryBox)) {
				for (T member : cell.members) {
					PVector separation = PVector.sub(member.getPosition(), targetPosition);
					if (separation.mag() < queryRadius) {
						neighbors.add(member);
					}
				}
			}
		}
		
		return neighbors;
	}
	
	/**
	 * Removes all entites from the cells of this space partition.
	 */
	public void clear() {
		for (Cell<T> cell : cells) {
			cell.members.clear();
		}
	}

	/**
	 * Converts the given position into an index into the cell space partition.
	 * @param position the position to index
	 * @return the index of the cell in the partition containing the position
	 */
	private int positionToIndex(final PVector position) {
		int index = (int) (position.x / cellWidth) + 
				(int) (position.y / cellHeight * numberCellsX);
		
		// If the entity is exactly in the bottom right corner of the space, we
		// have to adjust down.
		if (index > cells.size() - 1) {
			index = cells.size() - 1;
		}
			
		return index;
	}
}
