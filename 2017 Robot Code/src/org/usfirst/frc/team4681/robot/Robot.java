/*code for 2017
 * */
package org.usfirst.frc.team4681.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.CameraServer;
//import edu.wpi.first.wpilibj.Ultrasonic;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	//Boolean used for autonomous periodic
	String choice;
	int counter = 0;
	
	/*Values for Motor Controllers*/
	int driveFrontLeft=0;
	int driveFrontRight=1;
	int driveRearLeft=2;
	int driveRearRight=3;
	//int loadMC=4;
	int climbMC=5;
	
	//System Motor Controllers
	Victor loader;
	Victor climber;
	
	CameraServer camera = CameraServer.getInstance();
	
	//Range Finder: first parameter is ping channel, second is echo channel.
	//Ultrasonic ultra = new Ultrasonic(1,1);
	
	RobotDrive drive;
	
	/* Xbox controller notes:
	 * 5 Axis: 
	 * 0 = left stick X
	 * 1 = left stick Y
	 * 2 = right stick X
	 * 3 = right stick Y
	 
	 * 
	 * Button Layout:
	 * 1 = X
	 * 2 = A
	 * 3 = B
	 * 4 = Y
	 * 5 = Left bumper
	 * 6 = Right bumper
	 * 7 = Left trigger
	 * 8 = Right trigger
	 * 9 = Back Button
	 * 10 = Start Button
	 * 11 = Left Stick Click In
	 * 12 = Right Stick Click In
	 * 
	 * Note: After an update to the dashboard, this layout changed.
	 * It has since been updated, but this is a warning that this may not be accurate.
	 * To make sure you know the layout, plug in the controller and go to the smartdashboard.
	 * In the controller tab (bottom left side of the drive station) you should see the axiis
	 * and buttons. If you push an axis or a button and something lights up or changes, that
	 * is the associated axis/button. From top to bottom, left to right, the axiis start from 0,
	 * yet the buttons start at 1 (I think, i might have messed that up).
	 */
	Joystick xboxControl;
	
	SendableChooser<String> chooser;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 * 
	 * We are using the RobotDrive class to control
	 * the Drive system.
	 * 
	 * This should be pretty self-explanatory
	 */
	

	@Override
	public void robotInit() {
		System.out.println("Robot Init");
		drive = new RobotDrive(driveFrontLeft,driveRearLeft,driveFrontRight,driveRearRight);
		xboxControl = new Joystick(0);
		
		//loader = new Victor(loadMC);
		climber = new Victor(climbMC);
		
		//Inverts the right side motors so that the drive system works correctly
		//IE what would've been a value of 1 is now -1.
		drive.setInvertedMotor(MotorType.kFrontRight,true);
		drive.setInvertedMotor(MotorType.kRearRight, true);
		
		
		// Autonomous Chooser initialization; adds several choices then places them on dashboard.
		
		/*
		 * Here's how this works:
		 * SendableChooser is essentially a list of String objects.
		 * These objects have a sort of identifier, which is the second parameter (IE "baseline").
		 * The first parameter is the title of the object that will be displayed on the dashboard.
		 * The dashboard recieves the chooser data in the last line.
		 * 
		 * Note: You don't have to worry about the smartdashboard sending info back
		 * The code for that is integrated with the Java dashboard type, which is the type you should be using
		 * If you cant see the chooser, it seriously might be invisible in the top left
		 * corner of the Java dashboard. Make sure Editing is turned on if this occurs, and click wildly to find it.
		 * In its properties makes sure that the second option is not ticked off.
		 * Play with settings until it works again.
		 */
		
				chooser = new SendableChooser<String>();
				chooser.addDefault("Center Position", "center" );
				chooser.addObject( "Left or Right Position Baseline", "baseline");
				chooser.addObject("Right Position Blue", "rightb");
				chooser.addObject("Left Position Blue", "leftb");
				chooser.addObject("Right Position Red", "rightr");
				chooser.addObject("Left Position Red", "leftr");
		        SmartDashboard.putData("Autonomous mode chooser", chooser);
		        
		        
		//Camera stuff//
		        
		/*
		 * Cameras are terrifying when it comes to code.
		 * Liams code doesnt work because they cut functionality down for this year.
		 * I pray that they make it easier next year.
		 * 
		 * All I figured out how to do is start up the camera and have it stream.
		 * First parameter is the name assigned to the stream
		 * The second parameter is which USB port the camera uses.
		 */
		
		camera.startAutomaticCapture("Front Camera", 0);
		camera.startAutomaticCapture("Back Camera",1);
		
		//Range Finder, another scrapped idea I guess.
		//ultra.setAutomaticMode(true);
		
        
        
	}

/*Called at the beginning of autonomous*/
	@Override
	public void autonomousInit() {
		System.out.println("Autonomous Init");
		counter = 0;
		choice = chooser.getSelected();
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	//Periodic functions are called roughly every 20 ms. This means
	//that the code runs about 50 cycles per second.
	
	/*
	 * Here's how this works:
	 * choice is a string, determined by the SendableChooser/Smartdashboard interface.
	 * choice is used to access one of the possible autonomous codes.
	 * inside each choice is a check to see how long autonomous has run.
	 * It does this by increasing int counter at the end of each cycle
	 * When counter=50, for example, one second has elapsed.
	 * Depending on which part of autonomous, the robot will do one of three things;
	 * 1. Move forward (there is a small rotation value to counter the imperfection of the drive system)
	 * 2. Rotate (for gear delivery line-up)
	 * 3. Stop
	 */
	public void autonomousPeriodic() {
		
		// This should choose one of these autonomous programs, which should be tailored to start position.
		
		if(choice.equals("center")){
			if(counter>52){
				drive.mecanumDrive_Cartesian(0, 0, 0, 0);
			}
			else{
				drive.mecanumDrive_Cartesian(0, -.5, 0, 0);
			}
		}
		else if(choice.equals("leftr")){
			if(counter>135||counter>65&&counter<75){
				drive.mecanumDrive_Cartesian(0, 0, 0, 0);
			}
			else if(counter>75 && counter<97.5){
			drive.mecanumDrive_Cartesian(0, 0, .5, 0);	
			}
			
			else{
				drive.mecanumDrive_Cartesian(0, -.5, 0, 0);
			
			}
		}
			
		else if(choice.equals("leftb")){
			if(counter>153||counter>60&&counter<70){
				drive.mecanumDrive_Cartesian(0, 0, 0, 0);
			}
			else if(counter>70 && counter<88){
			drive.mecanumDrive_Cartesian(0, 0, .5, 0);	
			}
			
			else{
				drive.mecanumDrive_Cartesian(0, -.5, 0, 0);
			}
		}
			
		else if(choice.equals("rightb")){
			if(counter>135||counter>60&&counter<70){
				drive.mecanumDrive_Cartesian(0, 0, 0, 0);
			}
			else if(counter>70 && counter<92.5){
			drive.mecanumDrive_Cartesian(0, 0, -.5, 0);	
			}
			
			else{
				drive.mecanumDrive_Cartesian(0, -.5, 0, 0);
			}
			
			
		}
		else if(choice.equals("rightr")){
			if(counter>157||counter>55&&counter<70){
				drive.mecanumDrive_Cartesian(0, 0, 0, 0);
			}
			else if(counter>70 && counter<92.5){
			drive.mecanumDrive_Cartesian(0, 0, -.5, 0);	
			}
			
			else{
				drive.mecanumDrive_Cartesian(0, -.5, 0, 0);
			}
		}
		else if(choice.equals("baseline")){
			if(counter>75){
				drive.mecanumDrive_Cartesian(0, 0, 0, 0);
			}
			else{
				drive.mecanumDrive_Cartesian(0, -.5, 0, 0);
			}
		}
		
		counter++;
		
		/*
		 * This is what we used since the chooser wasn't working.
		 * Counts the number of times periodic is called,
		 * uses that to stop the robot.
		 * 
		 * 
		 
		if(counter>90){
			drive.mecanumDrive_Cartesian(0, 0, 0, 0);
		}
		else{
			drive.mecanumDrive_Cartesian(0,-.5,0, 0);
		}
		counter++;
		 */
	}

	/**
	 * This function is called periodically during operator control
	 *  
	 * This one is super easy. During teleop, the bot listens for controller inputs.
	 * Using the joystick inputs X, Y, and rotation vectors.
	 * Pushing buttons spins motors.
	 * If no buttons are pushed, motors are shut off. (A critical step that can be overlooked).
	 * Axiis always have a value, so it usually just passes 0 when there is no user input to a joystick.
	 */
	@Override
	public void teleopPeriodic() {
		drive.mecanumDrive_Cartesian(adjust(xboxControl.getRawAxis(0)),adjust(xboxControl.getRawAxis(1)),adjust(xboxControl.getRawAxis(2)),0);
		
		//System Controls
		//Loader, bound to Y and A buttons (A spins it out, Y spins it in)
		
		/* This system was scrapped, so this can't be trusted as far as button inputs go.
		 * 
		if(xboxControl.getRawButton(4)==true){
		loader.set(.75);	
		}
		else if(xboxControl.getRawButton(1)==true){
			loader.set(-.75);
		}
		else{
			loader.set(0);
		}
		*/
		
		//Climber, binds it to left trigger axis
		
		climber.set(adjust(-Math.abs(xboxControl.getRawAxis(3))));
		
	}
	

	/**
	 * This function is called periodically during test mode
	 * 
	 * We clearly never used this. I don't know why you would use code that wouldn't be run the same way for competition.
	 * 
	 */
	@Override
	public void testPeriodic() {
	}
	
/*Taken from 2016 Robot code (Thanks Liam!). This is used to give the joystick input some sensitivity in teleop.
	 * 
	 * special function to allow better control of robot
	basically squares the input (making it a smaller number b/c it is a decimal) while keeping sign
	also creates a dead zone so it is not constantly jerking (magic square method)*/
	private double adjust(double in){
		if(in > .05){
			if(in > 1) return 1;
			return in*in;
		} else if(in < -.05){
			if(in < -1) return -1;
			return -(in*in);
		} else {
			return 0;
		}
	}
	
}