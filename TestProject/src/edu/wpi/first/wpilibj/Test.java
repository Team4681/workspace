package edu.wpi.first.wpilibj;

public class Test {
	public static void main(String[] args){
		Joystick joyLeft = new Joystick(1);
		Joystick joyRight = new Joystick(2);
		Victor treadLeft = new Victor(1);
		Victor treadRight = new Victor(2);
		double leftCT;
		double rightCT;
		Victor shoot1 = new Victor(3);
		Victor shoot2 = new  Victor(4);
		Victor shootBelt = new Victor(5);
		
		while (true){
			leftCT = joyLeft.getY();
			rightCT = joyRight.getY();
			if ((leftCT <= .05) && (leftCT >= -.05)){
				leftCT = 0;
			}
			if (rightCT <= .05 && rightCT >= -.05){
				rightCT = 0;
			}
			treadLeft.set(leftCT);
			treadRight.set(rightCT);
			if (joyLeft.getTrigger()){
				shoot1.set(.6);
				shoot2.set(1.0);
			}
			else{
				shoot1.set(0);
				shoot2.set(0);
			}
			if (joyLeft.getTrigger() && joyRight.getTrigger()){
				shootBelt.set(1.0);
			}
			else{
				shootBelt.set(0);
			}
			Timer.delay(.05);
			
		}
	}
}
