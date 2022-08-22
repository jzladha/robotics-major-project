package major_project;
import java.util.ArrayList;

/**
 * This is main class, responsible for overall behaviour of robot
 * @author vijay and marwan
 */
public class Main {
	
	private static Sensors sensor = new Sensors();
	
	// Default constructor
	public Main() {
		// do nothing
	}
	
	/**
	 * main method localise, travel to first goal, read colour and travel back to start
	 * @author vijay
	 */
	public static void main(String[] args) {
		localise(); // localise the robot first
		sensor.rotate(-45); // the line is 45 degree right to north so rotate 45 degree left it thus robot faces north
		sensor.resetOrientation(); // now reset orientation so when the robot faces north the angle should be 0
		traverseOne(2);   // this method generates shortest path from cell (4,4) to cell (6,13), so the robot should travel from localisation line to the entrance of pi-shaped box
		sensor.rotate((sensor.getAngle()*-1)+90); //the robot will arrive in any direction so rotate the robot 90 degree right to north, now the robot will face the entrance of pi-shaped box
		// moving inside the box until it hits the wall
		sensor.moveDistance(30);
		while(!sensor.isTouchDetected()) {
			sensor.moveDistance(1);;
		}
		sensor.beep(); //make beeping sound
		int colDetected = 4; // initially assume the colour is green
		if(sensor.isRedorGreen().equals("red")) colDetected = 5; // if the colour is not green then update the colour
		sensor.moveDistance(-35); // now go backwards to get outside the box
		//sensor.rotate(-90);
		//sensor.resetOrientation();
		traverseTwo(colDetected); // this method generates shortest path from cell (6,13) to cell (4,2), so the robot should travel from the entrance of the box to the start point but it should take path other side of the stick
		sensor.rotate((sensor.getAngle()*-1)); // rotate and face north
		sensor.rotate(-135f); 				// rotate the robot towards the wall
		// go straight until it hits the wall
		while(!sensor.isTouchDetected()) {
			sensor.moveDistance(1);;
		}
		// make beep sound when it hits the wall
		sensor.beep();
	}
	
	/**
	 *  travel to first goal (pi-shaped box)
	 *  @param id = position of first obstacle {1,2 or 3}
	 *  @author vijay
	 */
	private static void traverseOne(int id) {
		PathPlanning planner = new PathPlanning(getMap(id),new Cell((byte)4,(byte)4),new Cell((byte)6,(byte)13));
		ArrayList<Cell> path = planner.getPath(); // gets the path from line to first goal
		// travels from line to goal
		for(int i = path.size()-1; i > 0; i--) {
			sensor.travelCells(path.get(i), path.get(i-1));
		}
	}
	
	/**
	 * travel to the second goal (towards the wooden wall)
	 * @param id = position of second obstacle {4 or 5}
	 * @author vijay
	 */
	private static void traverseTwo(int id) {
		PathPlanning planner = new PathPlanning(getMap(id),new Cell((byte)6,(byte)13),new Cell((byte)4,(byte)2));
		ArrayList<Cell> path = planner.getPath(); // gets the path from wooden box to start point
		// travels from goal 1 to goal 2
		for(int i = path.size()-1; i > 0; i--) {
			sensor.travelCells(path.get(i), path.get(i-1));
		}
	}
	
	/**
	 * loops through the 2d array grid of map and creates arrayList that contains obstacles
	 * @param i = position of obstacles {1,2,3,4 or 5}
	 * @return ArrayList<Cell> containing all obstacles correspondent to the map id
	 * @author vijay
	 */
	private static ArrayList<Cell> getMap(int i){
		ArrayList<Cell> map = new ArrayList<Cell>();	
			byte[][] grid = getGrid(i);
			for(int l = 0; l < grid.length; l++) {
				for(int m = 0; m < grid.length; m++) {
					if(grid[l][m] == (byte)(1)) {
						map.add(new Cell((byte)l,(byte)m));
					}
				}
			}
		return map;
	}
	
	/**
	 * returns the 2d array grid of the map
	 * @param i = position of obstacles {1,2,3,4 or 5}
	 * @return byte[][] represents one of 5 possible different boards
	 * @author Marwan
	 */
	private static byte[][] getGrid(int i){
		if(i == 1) {
			return new byte[][] {
				{1,1,1,1,0,0,0,0,0,0,0,0,0,0,0}, //1
				{1,1,1,0,0,0,0,0,0,0,0,0,0,0,0}, //2
				{1,1,0,0,0,0,0,0,0,0,0,0,0,0,0}, //3
				{1,0,0,0,0,0,0,0,0,0,0,1,0,0,0}, //4
				{0,0,0,0,0,0,0,0,0,1,1,0,0,0,0}, //5
				{0,0,0,0,0,0,0,0,1,1,1,0,0,0,0}, //6
				{0,0,0,0,0,0,0,1,1,1,0,0,0,0,0}, //7
				{0,0,0,0,0,0,1,1,1,0,0,0,0,0,0}, //8
				{0,0,0,0,0,1,1,1,0,0,0,0,0,0,0}, //9
				{0,0,0,0,0,1,1,0,0,0,0,0,0,0,0}, //10
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //11
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, //12
				{0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}, //13
				{0,0,0,0,0,0,0,0,0,0,0,0,1,1,1}, //14
				{0,0,0,0,0,0,0,0,0,0,0,1,1,1,1}  //15
			};
		}
		else if(i == 2) {
			return new byte[][] {
				{1,1,1,1,0,0,0,0,0,0,0,0,0,0,0}, //1
				{1,1,1,0,0,0,0,0,0,0,0,0,0,0,0}, //2
				{1,1,0,0,0,0,0,0,0,0,0,0,1,0,0}, //3
				{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //4 
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //5 
				{0,0,0,0,0,0,0,0,1,1,0,0,0,0,0}, //6 
				{0,0,0,0,0,0,0,1,1,1,0,0,0,0,0}, //7
				{0,0,0,0,0,0,1,1,1,0,0,0,0,0,0}, //8
				{0,0,0,0,0,1,1,1,0,0,0,0,0,0,0}, //9
				{0,0,0,0,0,1,1,0,0,0,0,0,0,0,0}, //10
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //11
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, //12   
				{0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}, //13
				{0,0,0,0,0,0,0,0,0,0,0,0,1,1,1}, //14 
				{0,0,0,0,0,0,0,0,0,0,0,1,1,1,1}  //15
			};
		}
		else if(i == 3) {
			return new byte[][] {
				{1,1,1,1,0,0,0,0,0,0,0,0,0,0,0}, //1
				{1,1,1,0,0,0,0,0,0,0,0,0,0,1,0}, //2
				{1,1,0,0,0,0,0,0,0,0,0,0,0,0,0}, //3
				{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //4 
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //5 
				{0,0,0,0,0,0,0,0,1,1,0,0,0,0,0}, //6 
				{0,0,0,0,0,0,0,1,1,1,0,0,0,0,0}, //7
				{0,0,0,0,0,0,1,1,1,0,0,0,0,0,0}, //8
				{0,0,0,0,0,1,1,1,0,0,0,0,0,0,0}, //9
				{0,0,0,0,0,1,1,0,0,0,0,0,0,0,0}, //10
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //11
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, //12  
				{0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}, //13
				{0,0,0,0,0,0,0,0,0,0,0,0,1,1,1}, //14 
				{0,0,0,0,0,0,0,0,0,0,0,1,1,1,1}  //15
			};
		}
		else if(i == 4) {
			return new byte[][] {
				{1,1,1,1,0,0,0,0,0,0,0,0,0,1,1}, //1
				{1,1,1,0,0,0,0,0,0,0,0,0,0,1,1}, //2
				{1,1,0,0,0,0,0,0,0,0,0,0,1,1,0}, //3
				{1,0,0,0,0,0,0,0,0,0,0,1,1,0,0}, //4 
				{0,0,0,0,0,0,0,0,0,0,1,1,0,0,0}, //5 
				{0,0,0,0,0,0,0,0,0,1,1,0,0,0,0}, //6 
				{0,0,0,0,0,0,0,0,1,1,0,0,0,0,0}, //7
				{0,0,0,0,0,0,0,1,1,0,0,0,1,1,1}, //8
				{0,0,0,0,0,0,1,1,0,0,0,0,1,1,1}, //9
				{0,0,0,0,1,1,1,0,0,0,0,0,1,1,1}, //10
				{0,0,0,0,1,1,0,0,0,0,0,0,1,1,1}, //11
				{0,0,0,1,0,0,0,0,0,0,0,0,0,0,1}, //12   
				{0,0,1,0,0,0,0,0,0,0,0,0,0,1,1}, //13
				{0,0,0,0,0,0,0,0,0,0,0,0,1,1,1}, //14 
				{0,0,0,0,0,0,0,0,0,0,0,1,1,1,1}  //15
			};
		}
		else {
			return new byte[][] {
				{1,1,1,1,0,0,0,0,0,0,0,0,0,1,1}, //1
				{1,1,1,0,0,0,0,0,0,0,0,0,1,1,0}, //2
				{1,1,0,0,0,0,0,0,0,0,0,1,1,0,0}, //3
				{1,0,0,0,0,0,0,0,0,0,1,1,0,0,0}, //4 
				{0,0,0,0,0,0,0,0,0,1,1,0,0,0,0}, //5 
				{0,0,0,0,0,0,0,0,1,1,0,0,0,0,0}, //6 
				{0,0,0,0,0,0,0,1,1,0,0,0,0,0,0}, //7
				{0,0,0,0,0,0,1,1,0,0,0,0,1,1,1}, //8
				{0,0,0,0,0,1,1,0,0,0,0,0,1,1,1}, //9
				{0,0,0,0,0,1,0,0,0,0,0,0,1,1,1}, //10
				{0,0,0,0,1,0,0,0,0,0,0,0,1,1,1}, //11
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, //12  
				{0,0,1,0,0,0,0,0,0,0,0,0,0,1,1}, //13
				{0,1,0,0,0,0,0,0,0,0,0,0,1,1,1}, //14 
				{1,0,0,0,0,0,0,0,0,0,0,1,1,1,1}  //15
			};
		}
	}
	
	/**
	 * this method will localise the robot
	 * @author vijay
	 */
	private static void localise() {
		Localisation localizer = new Localisation(getSubBlockData(getLineBlockData()));
		localizer.localize();
	}
	
	/**
	 * this method creates data from observation
	 * @return the observed data as String[][]
	 * @author vijay
	 */
	private static String[][] getLineBlockData() {
		String[][] blockData = new String[18][2];
		for(int i = 0; i < 18; i++) {
			if((i+1)%2 == 1) {
				blockData[i][0] = "white";
			}
			else {
				blockData[i][0] = "blue";
			}
		}
		float[] lengths = {3.4f, 5.1f, 1.7f, 3.4f, 3.4f, 5.1f, 1.7f, 3.4f, 3.4f, 5.1f, 3.5f, 5.1f, 1.7f, 3.4f, 3.4f, 5.1f, 1.7f, 3.5f};
		for(int i = 0; i < 18; i++) {
			blockData[i][1] = ""+lengths[i];
		}
		return blockData;
	}
	
	/**
	 * use observed data to create list of blockCells of 0.5 cm each
	 * @param inp = observed data from method getLineBlockData
	 * @return ArrayList<BlockCell> containing every sub blocks of 0.5cm
	 * @author vijay
	 */
	private static ArrayList<BlockCell> getSubBlockData(String[][] inp){
		ArrayList<BlockCell> ret = new ArrayList<BlockCell>();
		for(int i = 0; i < inp.length; i++) {
			String colour = inp[i][0];
			float length = Float.parseFloat(inp[i][1]);
			while(length >= 0.25) {
				ret.add(new BlockCell(colour, 0f,(i+1)));
				length -= 0.5;
			}
		}
		float initProb = (1.0f/ret.size());
		for(int i = 0; i < ret.size(); i++) {
			ret.get(i).setProbability(initProb);
		}
		for(int i =0; i < ret.size(); i++) {
			ret.get(i).setBlockID(i+1);
		}
		return ret;
	}
}
