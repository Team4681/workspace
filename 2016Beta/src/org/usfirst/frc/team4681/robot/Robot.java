/**
 * 
 * CODE FOR 2016
 * 
 * 
 */
package org.usfirst.frc.team4681.robot;

import edu.wpi.first.wpilibj.*;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	
	//values to be set later
	double distanceTo;
	boolean altCam;
	boolean altCtrl;
	
	//values for controllers
	int driveCtr0 = 0;
	int driveCtr1 = 1;
	int loadCtr = 2;
	int shootCtr0 = 3;
	int shootCtr1 = 4;
	
	//inputs
	String camIn0 = "cam0";
	String camIn1 = "cam1";
	/*
	int rangeSensorIn = 0;
	
	//range sensor
	Ultrasonic rangeIn = new Ultrasonic(1,rangeSensorIn);
	*/
	//inputs and output classes
	private Drive move;
	private Shooter shoot;
	private Loader load;
	private Joystick joy1;
	private Joystick joy2;
	
	//stuff for camera (most of it was copy and pasted last minute, need to take time to understand later)
	Image frame;
	int session0;
	NIVision.Rect rect;
	
    public void robotInit() {
    	//defining inputs and outputs upon startup
    	move = new Drive(driveCtr0, driveCtr1);
    	joy1 = new Joystick(0);
    	joy2 = new Joystick(1);
    	shoot = new Shooter(shootCtr0, shootCtr1);
    	load = new Loader(loadCtr);
    	
    	//for camera
    	frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
    	altCam = false;
    	
    	session0 = NIVision.IMAQdxOpenCamera(camIn0,NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        NIVision.IMAQdxConfigureGrab(session0);
        
        //define a rectangle for aiming purposes
        NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);

    }

    /**
     * This function is called periodically during autonomous
     * NOTE:THIS NEEDS TWEAKING	
     * LOTS OF IT
     */
    public void autonomousInit() {
    	move.autoDrive(1);
    	Timer.delay(3.5);
    	move.autoDrive(0);
    }

    
    public void telopInit(){
    	altCtrl=false;
    }
    
    /**
     * This function is called periodically during operator control
     */
   public void teleopPeriodic() {

   		NIVision.IMAQdxStartAcquisition(session0);
    	//attempt at getting around roboRio warnings for camera, completely separates the two cameras into one instance
    	//if(joy2.getRawButton(6)){
    		//NIVision.IMAQdxStopAcquisition(session0);
    		
            
    	//} else if(joy2.getRawButton(7)){
    		//NIVision.IMAQdxStopAcquisition(session0);
    		//session0 = NIVision.IMAQdxOpenCamera(camIn1,NIVision.IMAQdxCameraControlMode.CameraControlModeController);
           // NIVision.IMAQdxConfigureGrab(session0);
    		//NIVision.IMAQdxStartAcquisition(session0);
    	// }
    	
    	
    	//distanceTo = getRange();
    	//tell robot to move
    	if (joy1.getRawButton(10)){
    		altCtrl = false;
    	} else if(joy1.getRawButton(11)) {
    		altCtrl = true;
    	}
    	if(!altCtrl){
    		move.drive(joy1.getY(), joy2.getY(), joy2.getTrigger());
    	} else {
    		move.altDrive(joy1.getY(), joy1.getX());
    	}
    	//plays with shooting speed value
    	shoot.changeSpeed(joy2.getRawButton(11), joy2.getRawButton(10));
        //if trigg is pressed, shoot
        shoot.projectileLaunch(joy1.getTrigger());
        //moves ball backwards if necessary
        shoot.sendBack(joy2.getRawButton(2));
        //if but3, load in, if but 2, push out
        load.loadBall(joy1.getRawButton(3),joy1.getRawButton(2));
        
        //for camera
        NIVision.IMAQdxGrab(session0, frame, 1);
        NIVision.imaqDrawShapeOnImage(frame, frame, rect, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);
        CameraServer.getInstance().setImage(frame);
        NIVision.IMAQdxStopAcquisition(session0);
   }
    
    //called when diabled
    public void disabledInit(){
    	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    //returns the range sensor input as meters
    /*public double getRange(){
    	return rangeIn.getRangeMM()/1000;
    }
    */
}
