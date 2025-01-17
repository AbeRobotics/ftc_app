package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class OPModeDriveHelper {
	private static OPModeDriveHelper instance;
    private  OPModeConstants opModeConstants;
    private  Telemetry telemetry;
    private  HardwareMap hardwareMap;
    private LinearOpMode opMode;
    private DcMotor leftWheel;
    private DcMotor rightWheel;
    
    // Returns instance or creates a new one if null
    public static OPModeDriveHelper getInstance() {
        if(instance==null) {
            instance = new OPModeDriveHelper();
        }
        return instance;
    }
    
    public void init(Telemetry telemetry, HardwareMap hardwareMap, OPModeConstants opModeConstants, LinearOpMode opMode) {
    	this.telemetry = telemetry;
    	this.hardwareMap = hardwareMap;
        this.opModeConstants = opModeConstants;
        this.opMode = opMode;

        leftWheel = hardwareMap.dcMotor.get("left_wheel");
        rightWheel = hardwareMap.dcMotor.get("right_wheel");
    }
    
    private OPModeDriveHelper(){}
    
    // Drives with previously used settings
    public boolean drive(Double inches) {
    	return drive(inches, opModeConstants.getAutoSpeed(), opModeConstants.getDriveDirection());
    }
    
    // Drives in a line
    public boolean drive(Double inches, OPModeConstants.AutonomousSpeed speed, OPModeConstants.DriveDirection direction) {
    	setAllStop();
    	resetDriveEncoders();
    	double totalTicks = opModeConstants.ticksPerInch * inches;
    	
    	setDriveDirection(direction);
    	leftWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		leftWheel.setPower(getPower(speed));
		rightWheel.setPower(getPower(speed));
        leftWheel.setTargetPosition((int)totalTicks);
        rightWheel.setTargetPosition((int)totalTicks);
        
        while((leftWheel.isBusy() || rightWheel.isBusy()) && opMode.isStopRequested() == false) {
            telemetry.addData("Left Current Position -",leftWheel.getCurrentPosition());
            telemetry.addData("Right Current Position -",rightWheel.getCurrentPosition());
			telemetry.addData("Target positions: ", totalTicks);
            telemetry.update();
            sleep(10);
        }
        setAllStop();
        resetDriveEncoders();
        return true;
    }
	// Turns the robot using encoders
    public boolean driveTurn(OPModeConstants.TurnDirection direction, OPModeConstants.AutonomousSpeed speed, double angle){
    	setAllStop();
    	resetDriveEncoders();

    	setTurnDirection(direction);
		while(angle > 360) {angle -= 360;}
		while(angle < 0) {angle += 360;}

		double totalTicks = angle * opModeConstants.degreesToInch * opModeConstants.ticksPerInch;

		leftWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		rightWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		leftWheel.setPower(getPower(speed));
		rightWheel.setPower(getPower(speed));
		leftWheel.setTargetPosition((int)totalTicks);
		rightWheel.setTargetPosition((int)totalTicks);

		while((leftWheel.isBusy() || rightWheel.isBusy()) && opMode.isStopRequested() == false) {
			telemetry.addData("Left Current Position -",leftWheel.getCurrentPosition());
			telemetry.addData("Right Current Position -",rightWheel.getCurrentPosition());
			telemetry.addData("Target positions: ", totalTicks);
			telemetry.update();
			sleep(10);
		}
		setAllStop();
		resetDriveEncoders();
		return true;
	}

    // Turns the robot *THE GYROSCOPE DOESN'T WORK, DO NOT USE*
    public boolean gyroTurn(OPModeConstants.TurnDirection direction, OPModeConstants.AutonomousSpeed speed, double angle) {
    	setAllStop();
    	resetDriveEncoders();
    	
    	setTurnDirection(direction);
    	while(angle > 360) {angle -= 360;}
    	while(angle < 0) {angle += 360;}
    	
    	leftWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    	rightWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    	
    	OPModeGyroHelper gyroHelper = new OPModeGyroHelper();
    	while(!onHeading(angle, gyroHelper) && opMode.isStopRequested() == false) {
    		leftWheel.setPower(getPower(speed));
            rightWheel.setPower(getPower(speed));
    	}
    	
    	setAllStop();
    	resetDriveEncoders();
    	return true;
    }
    
    // Checks if robot is facing an angle
    private boolean onHeading(double targetAngle, OPModeGyroHelper gyroHelper) {
    	boolean onTarget = false;
    	
    	double DegreesOffTarget = Math.abs(targetAngle - gyroHelper.getGyroAngle(telemetry, hardwareMap));
    	if(DegreesOffTarget < opModeConstants.gyroErrorThreshold) {
    		onTarget = true;
    	}
    	return onTarget;
    }

    // Returns power for wheels
	private double getPower(OPModeConstants.AutonomousSpeed speed) {
		switch(speed) {
		case LOW:
			return 0.5;
		case MEDIUM:
			return 0.75;
		case HIGH:
			return 1.0;
		default:
			return 0.0;
		}
	}
	
	// Sets wheels to drive forward or backward
	private void setDriveDirection(OPModeConstants.DriveDirection direction) {
		switch(direction) {
		case FORWARD:
			rightWheel.setDirection(DcMotor.Direction.FORWARD);
			leftWheel.setDirection(DcMotor.Direction.REVERSE);
			break;
		case REVERSE:
			rightWheel.setDirection(DcMotor.Direction.REVERSE);
			leftWheel.setDirection(DcMotor.Direction.FORWARD);
			break;
		}
	}
	
	// Sets wheels to turn right or left
	private void setTurnDirection(OPModeConstants.TurnDirection direction) {
		switch(direction) {
		case RIGHT:
			rightWheel.setDirection(DcMotor.Direction.REVERSE);
			leftWheel.setDirection(DcMotor.Direction.REVERSE);
			break;
		case LEFT:
			rightWheel.setDirection(DcMotor.Direction.FORWARD);
			leftWheel.setDirection(DcMotor.Direction.FORWARD);
			break;
		}
	}

	// Resets the drive encoders
	private void resetDriveEncoders() {
		leftWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		rightWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
	}
	
	// Stops the robot
    public void setAllStop() {
        leftWheel.setPower(0);
        rightWheel.setPower(0);
    }
    
    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
