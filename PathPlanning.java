package major_project;
import java.util.*;

/**
 * this PathPlanning class responsible for A* search and generating shortest path between 2 cells
 * @author Vijay
 *
 */

public class PathPlanning {
	
	private ArrayList<Cell> openList;
	private ArrayList<Cell> closedList;
	private Cell start;
	private Cell current;
	private Cell goal;
	
	/**
	 * constructor
	 * @param obstacles = ArrayList<Cell> of obstacles
	 * @param start = start cell
	 * @param finish = finish cell
	 */
	public PathPlanning(ArrayList<Cell> obstacles, Cell start, Cell finish) {
		closedList = obstacles;
		openList = new ArrayList<Cell>();
		this.start = start;
		current = start;
		openList.add(current);
		goal = finish;
	}
	
	/**
	 * does A* search and returns shortest path from start to finish
	 * @return ArrayList<Cell> contains all the cell in reverse order that forms path from start to finish, empty list if path not possible
	 */
	public ArrayList<Cell> getPath(){
		astarSearch();
		ArrayList<Cell> path = new ArrayList<Cell>();
		if(getGoalFromCL() != null) {
			path.add(getGoalFromCL());
			while(!path.get(path.size()-1).equals(start)) {
				path.add(path.get(path.size()-1).parent);
			}
		}
		return path;
	}
	
	/**
	 * performs A* search from start to finish
	 */
	public void astarSearch() {
		while(!isInClosedList(goal) && !openList.isEmpty() && (current != null)) {
			updateOpenList(current);
			current = getCellWithSmallFunc();
		}
	}
	
	/**
	 * calculates H heuristic from the cell x to goal
	 * @param cell = cell x
	 * @return H heuristic
	 */
	private float calcH(Cell cell){
		float x = (goal.x - cell.x);
		float y = (goal.y - cell.y);
		return  (float) Math.sqrt((x*x)+(y*y));
	}
	
	/**
	 * checks for all possible neighbouring cells and adds or updates it in the openList
	 * @param cell = current cell whose neighbours need to added in the openList
	 */
	private void updateOpenList(Cell cell) {
		closedList.add(cell);
		openList.remove(cell);
		if(!cell.equals(goal)){
			checkNeighbour(cell,"n");
			checkNeighbour(cell,"s");
			checkNeighbour(cell,"e");
			checkNeighbour(cell,"w");
			checkNeighbour(cell,"ne");
			checkNeighbour(cell,"se");
			checkNeighbour(cell,"nw");
			checkNeighbour(cell,"sw");
		}
	}
	
	/**
	 * check if the given neighbour is valid if so then it updates or adds it to openList
	 * @param cell = cell whose neighbour needs to be checked
	 * @param dir = direction of neighbour cell corresponding to current cell
	 */
	private void checkNeighbour(Cell cell, String dir) {
		Cell newCell = null;
		if(dir.equals("n")) {newCell = new Cell(cell.x,(byte)(cell.y+1)); newCell.g = cell.g+8f;} // create north
		else if(dir.equals("s")) {newCell = new Cell(cell.x,(byte)(cell.y-1)); newCell.g = cell.g+8f;} // create south
		else if(dir.equals("e")) {newCell = new Cell((byte)(cell.x+1),cell.y); newCell.g = cell.g+8f;} // create east
		else if(dir.equals("w")) {newCell = new Cell((byte)(cell.x-1),cell.y); newCell.g = cell.g+8f;} // create west
		else if(dir.equals("ne")) { // create north east
			if(!isInClosedList(new Cell(cell.x,(byte)(cell.y+1))) || !isInClosedList(new Cell((byte)(cell.x+1),cell.y))){ // check north and east of cell
				newCell = new Cell((byte)(cell.x+1),(byte)(cell.y+1)); newCell.g = cell.g+11.3f;} }
		else if(dir.equals("se")) { // create south east
			if(!isInClosedList(new Cell(cell.x,(byte)(cell.y-1))) || !isInClosedList(new Cell((byte)(cell.x+1),cell.y))){ // check south and east of cell
				newCell = new Cell((byte)(cell.x+1),(byte)(cell.y-1)); newCell.g = cell.g+11.3f;} }
		else if(dir.equals("nw")) { // create north west
			if(!isInClosedList(new Cell(cell.x,(byte)(cell.y+1))) || !isInClosedList(new Cell((byte)(cell.x-1),cell.y))){ // check north and west of cell
				newCell = new Cell((byte)(cell.x-1),(byte)(cell.y+1)); newCell.g = cell.g+11.3f;} }
		else if(dir.equals("sw")) { // create south west
			if(!isInClosedList(new Cell(cell.x,(byte)(cell.y-1))) || !isInClosedList(new Cell((byte)(cell.x-1),cell.y))){ // check south and west of cell
				newCell = new Cell((byte)(cell.x-1),(byte)(cell.y-1)); newCell.g = cell.g+11.3f;} }
		// if possible update or create new cell
		if(newCell != null) {
			if(isValidCell(newCell) && !isInClosedList(newCell)) {
				newCell.h = calcH(newCell);
				newCell.parent = cell;
				addORupdateOL(newCell);
			}
		}
	}
	
	/**
	 * return true if the given cell is valid and exists in the board
	 */
	private boolean isValidCell(Cell cell) {
		return ((cell.x >= 0) && (cell.y >= 0) && (cell.x < 15) && (cell.y < 15));
	}
	
	/**
	 * checks if the given cell is in openList, if so it updates the heuristics and parent otherwise it adds the cell to openList
	 */
	private void addORupdateOL(Cell cell) {
		boolean contains = false;
		for(int i = 0; i < openList.size(); i++) {
			if(openList.get(i).equals(cell)) {
				if(!openList.get(i).isLowerFunc(cell)) {
					openList.set(i, cell);
				}
				contains = true;
				break;
			}
		}
	   if(contains == false) {
		   openList.add(cell);
	   }
	}
	
	/**
	 * checks if the given cell is in closedList
	 */
	private boolean isInClosedList(Cell cell) {
		boolean ret = false;
		for(Cell each : closedList) {
			if(each.equals(cell)) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	/**
	 * returns cell with smallest heuristic from openList
	 */
	private Cell getCellWithSmallFunc() {
		if(!openList.isEmpty()) {
			Cell ret = openList.get(0);
			for(Cell each : openList) {
				if(each.getFunc() < ret.getFunc()) {
					ret = each;
				}
			}
			return ret;
		}
		else {
			return null;
		}
	}
	
	/**
	 * returns the goal cell from closed list
	 */
	private Cell getGoalFromCL() {
		Cell ret = null;
		for(Cell each : closedList) {
			if(each.equals(goal)) { ret = each; break;}
		}
		return ret;
	}
	
	
}
