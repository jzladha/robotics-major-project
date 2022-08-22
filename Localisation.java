package major_project;
import java.util.ArrayList;

/**
 * Localisation of EV3 robot using Bayesian	Filter-1D
 * @author Vijay
 */

public class Localisation {
	
	private ArrayList<BlockCell> blockList = new ArrayList<BlockCell>();
	private Sensors sensor = new Sensors();
	
	
	//default constructor
	public Localisation(ArrayList<BlockCell> list) {
		blockList = list;
	}
	
	// determines the robot pose
	public void localize() {
		readColour();
		for(int i = 0; i < 25; i++) {
			sensor.moveDistance(-0.5);
			shiftProbBackward();
			readColour();
		}
		for(int i = 0; i < 50; i++) {
			sensor.moveDistance(0.5);
			shiftProbForward();
			readColour();
			float highProb = getBlockWithHighProb().getProb();
			if(highProb > 0.9) break;
		}
		
		// for debugging
		int maxBlockID = getBlockWithHighProb().getBlockID();
		float distance = maxBlockID*0.5f;
		alignCentreOfLine(distance);
	}
	
	// senses the colour and updates the probability
	private void readColour() {
		String colour = sensor.getBlockColour();
		if(colour.equals("blue")) {
			for(int i = 0; i < blockList.size(); i++) {
				if(blockList.get(i).getColour().equals("blue")) {
					float prev = blockList.get(i).getProb();
					blockList.get(i).setProbability(prev*0.9f);
				}
				else {
					float prev = blockList.get(i).getProb();
					blockList.get(i).setProbability(prev*0.1f);
				}
			}
			normalise();
		}
		else if(colour.equals("white")) {
			for(int i = 0; i < blockList.size(); i++) {
				if(blockList.get(i).getColour().equals("white")) {
					float prev = blockList.get(i).getProb();
					blockList.get(i).setProbability(prev*0.9f);
				}
				else {
					float prev = blockList.get(i).getProb();
					blockList.get(i).setProbability(prev*0.1f);
				}
			}
			normalise();
		}
		else {
			// when the sensor reads colours other than blue and white do nothing
		}
	}
	
	// normalise the probability so that all probability add upto 1
	private void normalise() {
		float sumOfProb = 0.0f;
		for (int i = 0; i < blockList.size(); i++) {
			sumOfProb += blockList.get(i).getProb();
		}
		for(int i = 0; i < blockList.size(); i++) {
			float prob = blockList.get(i).getProb();
			blockList.get(i).setProbability((prob/sumOfProb));
		}
	}
	
	// shift the prob of blocks to right
	private void shiftProbForward() {
		for(int i = (blockList.size()-1); i > 0; i--){
			blockList.get(i).setProbability(blockList.get(i-1).getProb());
		}
		blockList.get(0).setProbability(0.0f);
	}
	
	// shift the prob of blocks to left
	private void shiftProbBackward() {
		for(int i = 0; i < blockList.size()-1; i++){
			blockList.get(i).setProbability(blockList.get(i+1).getProb());
		}
		blockList.get(blockList.size()-1).setProbability(0.0f);
	}
	
	// returns the block with highest probability
	private BlockCell getBlockWithHighProb() {
		BlockCell maxBlock = blockList.get(0);
		for(int i = 0; i < blockList.size(); i++) {
			if(blockList.get(i).getProb() > maxBlock.getProb()) {
				maxBlock = blockList.get(i);
			}
		}
		return maxBlock;
	}
	
	// makes the robot move to the centre of the line
	private void alignCentreOfLine(float dist) {
		float toMove = 35.5f - dist;
		sensor.moveDistance(toMove);
	}
	
}