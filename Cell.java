package major_project;

/**
 * this class represents the cell
 * @author Vijay
 *
 */

public class Cell {
	
	public byte x;
	public byte y;
	public Cell parent = null;
	public float h;
	public float g;
	
	/**
	 * constructor
	 * @param x = x coordinate of cell
	 * @param y = y coordibate of cell
	 */
	public Cell(byte x, byte y) {
		this.x = x;
		this.y = y;
		h = 0f;
		g = 0f;
	}
	
	/**
	 * returns the sum of both heuristic
	 */
	public float getFunc() {
		return (h+g);
	}
	
	/**
	 * compares the given cell with current cell return true if both cell has same x and y coordinate
	 */
	public boolean equals(Cell cell) {
		return ((cell.x == x) && (cell.y == y));
	}
	
	/**
	 * compares the function of current cell with given cell and return true if current cell has lower heuristic function
	 */
	public boolean isLowerFunc(Cell cell) {
		return (getFunc() < cell.getFunc());
	}
	
}
