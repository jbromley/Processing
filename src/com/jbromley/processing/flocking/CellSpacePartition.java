package com.jbromley.processing.flocking;

import java.util.ArrayList;
import java.util.LinkedList;

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
	
	private ArrayList<CellSpacePartition.Cell<T>> cells;
	private float spaceWidth;
	private float spaceHeight;
	private int numberCellsX;
	private int numberCellsY;
	private float cellWidth;
	private float cellHeight;
	private boolean wrapMode;
	
	/**
	 * Creates a new cell space partition. 
	 * @param width the width of the space to be divided
	 * @param height the height of the space to be dividedd
	 * @param cellsX the number of horizontal cells
	 * @param cellsY the number of vertical cells
	 * @param maxEntities the maximum number of entities to allow in a cell
	 */
	public CellSpacePartition(float width, float height, int cellsX, int cellsY) {
		cells = new ArrayList<CellSpacePartition.Cell<T>>();
		spaceWidth = width;
		spaceHeight = height;
		numberCellsX = cellsX;
		numberCellsY = cellsY;
		wrapMode = false;
		
		cellWidth = spaceWidth / numberCellsX;
		cellHeight = spaceHeight / numberCellsY;
		
		for (int y = 0; y < numberCellsY; ++y) {
			for (int x = 0; x < numberCellsX; ++x) {
				float top = y * cellHeight;
				float left = x * cellWidth;
				float bottom = top + cellHeight - 1.0f;
				float right = left + cellWidth - 1.0f;
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
		Cell<T> cell = cells.get(index);
		cell.members.add(entity);
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
			Cell<T> oldCell = cells.get(oldIndex);
			Cell<T> newCell = cells.get(newIndex);
			oldCell.members.remove(entity);
			newCell.members.add(entity);
		}
	}
	
	public ArrayList<T> getNeighborList(PVector target, float queryRadius) {
		ArrayList<T> neighbors = new ArrayList<T>();
		
		// Find the coordinates of the region of interest's corners. This will
		// depend on the wrap mode.
		float left = target.x - queryRadius;
		float top = target.y - queryRadius;
		float right = target.x + queryRadius;
		float bottom = target.y + queryRadius;
		
		// Rectify the corner coordinates if we are not in wrap mode.
		if (!wrapMode) {
			left = Math.max(0.0f, left);
			top = Math.max(0.0f, top);
			right = Math.min(spaceWidth - 1, right);
			bottom = Math.min(spaceHeight - 1, bottom);
		}
		
		// Turn coordinates into x, y indices.
		int leftIndex = (int) (Math.floor(left / cellWidth));
		int topIndex = (int) (Math.floor(top / cellHeight));
		int rightIndex = (int) (Math.floor(right / cellWidth));
		int bottomIndex = (int) (Math.floor(bottom / cellHeight));
		
		for (int rowIndex = topIndex; rowIndex <= bottomIndex; ++rowIndex) {
			for (int colIndex = leftIndex; colIndex <= rightIndex; ++colIndex) {
				int index = rowColumnToIndex(rowIndex, colIndex);
				Cell<T> cell = cells.get(index);
				if (!cell.members.isEmpty()) {
					for (T member : cell.members) {
						PVector point = !wrapMode ? member.getPosition() : 
								translateWrappedPoint(member.getPosition(), 
										rowIndex, colIndex);
						float distance = PVector.dist(point, target);
						if (distance < queryRadius) {
							neighbors.add(member);
						}
					}
				}
			}
		}
		
		return neighbors;
	}
	
	public void setWrapMode(boolean useWrapping) {
		wrapMode = useWrapping;
	}
	
	public boolean getWrapMode() {
		return wrapMode;
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
				(int) (position.y / cellHeight) * numberCellsX;
		
		// If the entity is exactly in the bottom right corner of the space, we
		// have to adjust down.
		if (index > cells.size() - 1) {
			index = cells.size() - 1;
		}

		return index;
	}

	/**
	 * Converts a matrix row and column into an index into the cell space 
	 * partition.
	 * @param row the matrix row of the desired element
	 * @param column the matrix column of the desired element
	 * @return the index to the desired element
	 */
	private int rowColumnToIndex(int row, int column) {
		if (row < 0){
			row += numberCellsY;
		} else if (row >= numberCellsY) {
			row %= numberCellsY;
		}
		if (column < 0) {
			column += numberCellsX;
		} else if (column >= numberCellsX) {
			column %= numberCellsX;
		}
		int index = column + row * numberCellsX;
		if (index > cells.size()) {
			index = cells.size() - 1;
		}
		return index;
	}
	
	private PVector translateWrappedPoint(PVector sourcePoint, int rowIndex, int colIndex) {
		PVector translatedPoint = sourcePoint.get();
		
		if (rowIndex < 0) {
			translatedPoint.y -= spaceHeight;
		} else if (rowIndex >= numberCellsY) {
			translatedPoint.y += spaceHeight;
		}
		if (colIndex < 0) {
			translatedPoint.x -= spaceWidth;
		} else if (colIndex >= numberCellsX) {
			translatedPoint.x += spaceWidth;
		}
		
		return translatedPoint;
	}
}
