package org.usfirst.frc.team4681.robot;

import java.text.DecimalFormat;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import org.usfirst.frc.team4681.robot.commands.ExampleCommand;
import org.usfirst.frc.team4681.robot.subsystems.ExampleSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;

	Joystick joy1 = new Joystick(0);
	Talon cim = new Talon(0);
	Talon window = new Talon(1);
	DigitalInput limswitch = new DigitalInput(0);
	// This is the encodero for the lifting motor
	Encoder Tuna = new Encoder(8, 9);
	int oldEncode = 0;
	PowerDistributionPanel power = new PowerDistributionPanel();
	Command autonomousCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		oi = new OI();
		// instantiate the command used for the autonomous period
		autonomousCommand = new ExampleCommand();
		Tuna.setDistancePerPulse(.004);
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void autonomousInit() {
		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called when the disabled button is hit. You can use it
	 * to reset subsystems before shutting down.
	 */
	public void disabledInit() {

	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
		if ((Math.abs(joy1.getY()) > .1)) {
			window.set(joy1.getY());
		} else {
			window.set(0);
		}
		
		/**
		 * while(joy1.getRawButton(1)){
			window.set(1);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			window.set(-1);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		window.set(0.0);
		**/
		DecimalFormat df = new DecimalFormat("#.00000");
		if (Math.abs(Tuna.get() - oldEncode) > 0) {
			//System.out.println("Count:" + Tuna.get() + "\tRate:" + Tuna.getRate() + "\tTurns:" + Tuna.getDistance());
			oldEncode = Tuna.get();
			//System.out.println("Vin:" + power.getVoltage() + "\tTotal Current:" + power.getCurrent(14));
			System.out.print("Current on PDP 14:" + df.format(power.getCurrent(14)) + "\t");
			for(int i = 0; i < (int)(power.getCurrent(14) * 10); i++) {
				System.out.print("*");
			}
			System.out.println();
		}
	}
}