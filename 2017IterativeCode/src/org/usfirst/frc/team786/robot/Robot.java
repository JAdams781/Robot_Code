package org.usfirst.frc.team786.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

public class Robot extends IterativeRobot {
	Joystick driver, op;
	CANTalon leftMaster, leftSlave, rightMaster, rightSlave, intake, shooterTop, shooterBottom, elevator, climber,
			floor;
	Talon agitator;
	Solenoid shifter;
	DoubleSolenoid gearIn, gearOut;

	double p = 0;
	double i = 0;
	double d = 0;
	double f = 0;
	public double ticksPerInch = 270.08;
	Compressor comp;

	RobotDrive myDrive;
	Boolean AutoGear = false, rightPosition = false, leftPosition = false;

	public void robotInit() {
		driver = new Joystick(0);
		op = new Joystick(1);
		leftMaster = new CANTalon(11);
		leftSlave = new CANTalon(12);
		rightMaster = new CANTalon(21);
		rightSlave = new CANTalon(22);
		intake = new CANTalon(31);
		agitator = new Talon(0);
		shooterTop = new CANTalon(32);
		shooterBottom = new CANTalon(33);
		elevator = new CANTalon(34);
		climber = new CANTalon(35);

		leftMaster.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		rightMaster.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		rightSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		rightSlave.set(rightMaster.getDeviceID());
		leftSlave.set(leftMaster.getDeviceID());
		leftMaster.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogEncoder);
		rightMaster.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogEncoder);
		// rightMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		// leftMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		rightMaster.configNominalOutputVoltage(+0f, -0f);
		rightMaster.configPeakOutputVoltage(10f, -10f);
		leftMaster.configNominalOutputVoltage(+0f, -0f);
		leftMaster.configPeakOutputVoltage(+10f, -10f);
		// rightMaster.setPID(p, i, d);
		// leftMaster.setPID(p, i, d);

		// elevator.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		shooterTop.changeControlMode(CANTalon.TalonControlMode.Speed);
		shooterBottom.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		intake.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		shooterTop.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		// agitator.changeControlMode(Talon.TalonControlMode.PercentVbus);
		// shooterBottom.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		shooterTop.setPID(0.09, 0, 0.9);
		shooterTop.setF(0.0275);
		// shooterBottom.setPID(1, 0, 0);

		shifter = new Solenoid(0);
		gearIn = new DoubleSolenoid(4, 5);
		gearOut = new DoubleSolenoid(1, 2);

		comp = new Compressor(1);
		myDrive = new RobotDrive(leftMaster, rightMaster);
		//CameraServer server = CameraServer.getInstance();
	
		//server.startAutomaticCapture(0);
		rightMaster.setPID(2.3, 0, 23);
		leftMaster.setPID(2.1, 0, 0);
		// AutoGear=false;

	}

	public void autonomousInit() {
		rightMaster.setPosition(0);
		leftMaster.setPosition(0);
		rightMaster.changeControlMode(CANTalon.TalonControlMode.Position);
		leftMaster.changeControlMode(CANTalon.TalonControlMode.Position);
	}

	public void autonomousPeriodic() {
		SmartDashboard.putNumber("rightMasterPostition", rightMaster.getPosition());
		SmartDashboard.putNumber("leftMasterPostition", leftMaster.getPosition());
		SmartDashboard.putNumber("RightRPM", rightMaster.getPosition());
		SmartDashboard.putNumber("LeftRPM", leftMaster.getPosition());
		// SmartDashboard.putBoolean(rightPosition.getBoolean(rightPosition));
		// Front Auto
		/*
		 * if (rightMaster.getPosition() <= 15892.08){
		 * rightMaster.set(15884.08); rightPosition=true; }
		 *  if(leftMaster.getPosition()>= -16132.08){ 
		 *  leftMaster.set(-16132.08);
		 * leftPosition=true; }
		 */
		// if (rightPosition==true && leftPosition==true){
		// gearOut.set(DoubleSolenoid.Value.kForward);
		// rightMaster.set(-5000);
		// leftMaster.set(5000);
		// }
		// RightAuto
		if (rightMaster.getPosition() <= 11892.08) {
			rightMaster.set(11884.08);
		}
		if (leftMaster.getPosition() >= -12132.08) {
			leftMaster.set(-12132.08);
			if (rightMaster.getPosition() <= 25972.08) {
				rightMaster.set(25972.08);
				rightPosition = true;
			}
			if (leftMaster.getPosition() >= -20840) {
				leftMaster.set(-20840);
				leftPosition = true;
			}
		}
		/*
		 * if (!AutoGear) {
		 * 
		 * if (rightMaster.getPosition() <= 271200000) { rightMaster.set(0.25);
		 * } else { rightMaster.set(0); }
		 * 
		 * if (leftMaster.getPosition() <= -103900) { leftMaster.set(-0.25); }
		 * else { leftMaster.set(0); }
		 * gearOut.set(DoubleSolenoid.Value.kForward); } else {
		 * rightMaster.set(-0.25); leftMaster.set(0.25); }
		 * 
		 * // rightMaster.set(0.75); // leftMaster.set(0.75);
		 * 
		 */ }

	/* rightMaster.set(3000); leftMaster.set(3000); */
	public void teleopPeriodic() {
		rightMaster.configNominalOutputVoltage(+0f, -0f);
		rightMaster.configPeakOutputVoltage(10f, -10f);
		leftMaster.configNominalOutputVoltage(+0f, -0f);
		leftMaster.configPeakOutputVoltage(+10f, -10f);
		
		rightMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		leftMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		SmartDashboard.putNumber("ShooterRpm", shooterTop.getSpeed());
		SmartDashboard.putNumber("Shooter2Rpm", shooterBottom.getBusVoltage());
		SmartDashboard.putNumber("rightPosition", rightMaster.getPosition());
		SmartDashboard.putNumber("leftPosition", leftMaster.getPosition());
		myDrive.arcadeDrive(driver.getRawAxis(1), driver.getRawAxis(4), true);

		// elevator.set(op.getRawAxis(4));
		/*
		 * rightMaster.set(driver.getRawAxis(4) - driver.getRawAxis(1));
		 * leftMaster.set(driver.getRawAxis(4) + driver.getRawAxis(1));
		 */
		climber.set(driver.getRawAxis(3));
		//
		if (op.getRawButton(1)) {
			intake.set(-0.75);
		}
		if (!op.getRawButton(1)) {
			intake.set(0);
		}

		if (op.getRawButton(5)) {
			elevator.set(.4);
			agitator.set(-.7);

		}
		if (!op.getRawButton(5)) {
			elevator.set(0);
			agitator.set(0);
			// intake.set(0);
		}
		if (op.getRawButton(6)) {
			shooterTop.set(3300);

			shooterBottom.set(-0.89);
		}
		if (!op.getRawButton(6)) {
			shooterTop.set(0);
			shooterBottom.set(0);
		}
		if (driver.getRawButton(5)) {
			shifter.set(true);

		}

		if (!driver.getRawButton(5)) {
			shifter.set(false);
		}

		if (op.getRawButton(4)) {
			gearIn.set(DoubleSolenoid.Value.kForward);
		}
		if (!op.getRawButton(4)) {
			gearIn.set(DoubleSolenoid.Value.kReverse);
		}

		if (!op.getRawButton(2)) {
			gearOut.set(DoubleSolenoid.Value.kForward);
		}
		if (op.getRawButton(2)) {
			gearOut.set(DoubleSolenoid.Value.kReverse);
		}
	}
}
