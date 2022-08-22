package major_project;

/**
 * this is a BlockCell class represents the block in straight line at the start.
 * @author Tamerlan
 */

public class BlockCell {
	
	private String colour;
	private float probability;
	private int blockID;
	
	//constructor assigns the colour and length
	public BlockCell(String col, float prob, int id) {
		colour = col;
		probability = prob;
		blockID = id;
	}

	// returns the colour of the block
	public String getColour() {
		return colour;
	}
	
	// returns the probability of the block
	public float getProb() {
		return probability;
	}
	
	// returns the block id
		public int getBlockID() {
			return blockID;
		}
		
	// set's the colour to input value
	public void setColour(String col) {
		colour = col;
	}
	
	// set's the probability to input value
	public void setProbability(float prob) {
		probability = prob;
	}
	
	// set's the block id to input value
	public void setBlockID(int id) {
		blockID = id;
	}
	
}
