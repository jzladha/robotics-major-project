package major_project;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.Sound;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
/**
 * this class is responsible for providing functions for robot navigation
 * @author Vijay, Tamerlan , marwan , jazir
 *
 */
public class Sensors {
	private static EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	private static EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.D);
	private static EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S3);
	private static EV3TouchSensor touchSensorOne = new EV3TouchSensor(SensorPort.S1);
	private static EV3TouchSensor touchSensorTwo = new EV3TouchSensor(SensorPort.S2);
	private static EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S4);
	private static Wheel lWheel = WheeledChassis.modelWheel(leftMotor, 3.1).offset(-4.25);
	private static Wheel rWheel = WheeledChassis.modelWheel(rightMotor, 3.1).offset(4.25);
	private static Chassis wheeledChassis = new WheeledChassis(new Wheel[] {lWheel,rWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
	private static MovePilot mPilot = new MovePilot(wheeledChassis);
	private static EV3LargeRegulatedMotor[] syncList = {rightMotor};
	
	//default constructor
	public Sensors() {
		// do nothing
	}
	
	/**
	 * this method reads the colour from the colour sensor
	 * @author jazir
	 */
	public String getBlockColour() {
		if(colorSensor.getColorID() == Color.BLUE){
			return "blue";
		}
		else if (colorSensor.getColorID() == Color.WHITE){
			return "white";
		}
		else {
			return "error";
		}
	}
	
	/**
	 * this method move the robot forward certain distance
	 * @author jazir
	 */
	public void moveDistance(double dist) {
		mPilot.setLinearSpeed(mPilot.getMaxLinearSpeed()*0.5);
		mPilot.travel(dist);
	}
	
	/**
	 * this method returns true if the colour sensor reads red
	 * @author jazir
	 */
	public String isRedorGreen() {
		if(colorSensor.getColorID() == Color.RED) {
			return "red";
		}
		else {
			return "green";
		}
	}
	
	/**
	 * returns true if the touchSensor is pressed
	 * @author jazir
	 */
	public boolean isTouchDetected() {
		SampleProvider providerOne = touchSensorOne.getTouchMode();
		SampleProvider providerTwo = touchSensorTwo.getTouchMode();
		float[] sampleOne = new float[providerOne.sampleSize()];
		providerOne.fetchSample(sampleOne, 0);
		float[] sampleTwo = new float[providerTwo.sampleSize()];
		providerTwo.fetchSample(sampleTwo, 0);
		return (sampleOne[0] == 1 || sampleTwo[0] == 1);
	}
	/**
	 * returns the angle the robot is currently facing from north, +ve if the angle is calculated from east and -ve if the angle is calcualted from west
	 * @author jazir
	 */
	public float getAngle() {
		SampleProvider provider = gyroSensor.getAngleMode();
		float[] sample = new float[provider.sampleSize()];
		provider.fetchSample(sample, 0);
		return (-1f*sample[0]);
	}
	
	/**
	 * resets the north
	 * @author jazir
	 */
	public void resetOrientation() {
		gyroSensor.reset();
	}
	
	/**
	 * rotates the robot to given angle
	 * @author vijay
	 */
	public void rotate(float angle) {
		mPilot.setAngularSpeed(50);
		mPilot.rotate(angle);
		//rotateTwo(angle);
		//rotateThree(angle);
	}
	
	/**
	 * rotation method using real-time orientation with help of gyro
	 * @author vijay
	 */
	public void rotateTwo(float angle) {
		float finalAngle = getAngle()+angle;
		leftMotor.setSpeed(500);
		rightMotor.setSpeed(500);
		leftMotor.synchronizeWith(syncList);
		while(getAngle() != finalAngle) {
			if(getAngle() < finalAngle) {          // rotate right
				leftMotor.startSynchronization();
				leftMotor.forward();
				rightMotor.backward();
				leftMotor.endSynchronization();
			}
			else {                                 // rotate left
				leftMotor.startSynchronization();
				leftMotor.backward();
				rightMotor.forward();
				leftMotor.endSynchronization();
			}
			try {
				Thread.sleep(100);
			} catch(Exception e) {}
			float ratio = ((finalAngle - getAngle())/finalAngle);
			if(ratio < 0.1f) { 
				if(ratio > -0.1f) { ratio = 0.1f;}
				else {ratio *= -1f;}
				}
			leftMotor.setSpeed(ratio*500f);
			rightMotor.setSpeed(ratio*500f);
		}
	}
	
	/**
	 * rotation method using proportional controller
	 * @author vijay
	 */
	public void rotateThree(float angle) {
		float offset = getAngle()+angle;
		float kp = 5.5f;
		leftMotor.synchronizeWith(syncList);
		while(getAngle() != offset) {
			float error = offset - getAngle();
			if(error < 4.5f && error > -4.5f) {
				if(error > 0) {error = 4.5f;}
				else {error = -4.5f;}
			}
			float speed = kp*error;
			leftMotor.setSpeed(speed);
			rightMotor.setSpeed(-1f*speed);
			leftMotor.startSynchronization();
			leftMotor.forward();
			rightMotor.forward();
			leftMotor.endSynchronization();
			try {
				Thread.sleep(100);
			} catch(Exception e) {}
		}
	}
	
	/**
	 * moves the robot forward
	 * @author jazir
	 */
	public void forward() {
		rightMotor.synchronizeWith(syncList);
		rightMotor.startSynchronization();
		rightMotor.forward();
		leftMotor.forward();
		rightMotor.endSynchronization();
	}
	
	/**
	 * moves the robot backward
	 * @author jazir
	 */
	public void backward() {
		
		rightMotor.synchronizeWith(syncList);
		rightMotor.startSynchronization();
		rightMotor.backward();
		leftMotor.backward();
		rightMotor.endSynchronization();
	}
	
	/**
	 * returns the max speed of the robot
	 * @author jazir
	 */
	public float getMaxSpeed() {
		return rightMotor.getMaxSpeed();
	}
	
	/**
	 * set the speed of robot
	 * @author jazir
	 */
	public void setSpeed(float speed) {
		rightMotor.setSpeed(speed);
		leftMotor.setSpeed(speed);
	}
	
	/**
	 * make the robot to beep
	 * @author jazir
	 */
	public void beep() {
		Sound.beep();
	}
	
	/**
	 * reset the tachometer of the robot
	 * @author jazir
	 */
	public void resetTacho() {
		rightMotor.resetTachoCount();
		leftMotor.resetTachoCount();
	}
	
	/**
	 * returns the distance recorded by inbuilt tachoMeter
	 */
	public float getTachoCount() {
		return rightMotor.getTachoCount();
	}
	
	/**
	 * this method takes current cell and its neighbouring cell and travels from current cell to given neighbouring cell
	 * @param coordFrom = current cell
	 * @param coordTo = cell it needs to go
	 * @author Vijay, Tamerlan and Marwan (developed by pair programming so everyone is responsible for each line of code)
	 */
	public void travelCells(Cell coordFrom, Cell coordTo) {
		byte x1 = coordFrom.x;
		byte y1 = coordFrom.y;
		byte x2 = coordTo.x;
		byte y2 = coordTo.y;
		float displacement = 0f;
		float angle = 0f;
		if(x2 == x1) {
			displacement = 8f;
			if(y2-y1 < 0) {
				angle = 180f;
			}
		}
		else if(y2 == y1) {
			displacement = 8*(x2-x1);
			if(displacement > 0) {
				angle = 90f;
			}
			else {
				angle = -90f;
				displacement *= -1;
			}
		}
		else if((x2-x1 > 0) && (y2-y1 >0)) {
			displacement = 11.3f;
			angle = 45f;
		}
		else if((x2-x1 < 0) && (y2-y1 > 0)) {
			displacement = 11.3f;
			angle = -45f;
		}
		else if((x2-x1 < 0) && (y2-y1 <0)) {
			displacement = 11.3f;
			angle = -135f;
		}
		else if((x2-x1 > 0) && (y2-y1 < 0)) {
			displacement = 11.3f;
			angle = 135f;
		}
		
		angle += (-1*getAngle());
		rotate(angle);
		moveDistance(displacement);
	}
	
}
