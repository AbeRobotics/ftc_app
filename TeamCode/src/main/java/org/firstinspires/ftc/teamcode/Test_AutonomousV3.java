package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Kyle Stang on 23-Jan-2019;
 */
@Autonomous(name="Autonomous Test V3", group="Autonomous Tests")
public class Test_AutonomousV3 extends LinearOpMode {

	private ElapsedTime runTime = new ElapsedTime();

    @Override
    public void runOpMode() {

    	OPModeConstants opModeConstants = OPModeConstants.getInstance();
    	Helper_OPModeDriverV3 driver = Helper_OPModeDriverV3.getInstance();

    	driver.init(telemetry, hardwareMap, opModeConstants, this);
    	
    	waitForStart();
		telemetry.addData("Status: ", "Started");
		telemetry.update();
		//sleep(1000);
    	//runTime.reset();
    	
    	opModeConstants.setAutoSpeed(OPModeConstants.AutonomousSpeed.LOW);
    	opModeConstants.setDriveDirection(OPModeConstants.DriveDirection.FORWARD);
    	telemetry.addData("Runtime: ", runTime.toString());
    	telemetry.update();
    	sleep(1000);
    	driver.drive(24.0, opModeConstants.getAutoSpeed(), opModeConstants.getDriveDirection());
    	sleep(2000);
    	
    	opModeConstants.setTurnDirection(OPModeConstants.TurnDirection.RIGHT);
    	driver.driveTurn(opModeConstants.getTurnDirection(), opModeConstants.getAutoSpeed(), 360.0);
    	sleep(2000);
    	
    	opModeConstants.setTurnDirection(OPModeConstants.TurnDirection.LEFT);
    	opModeConstants.setAutoSpeed(OPModeConstants.AutonomousSpeed.HIGH);;
    	driver.driveTurn(opModeConstants.getTurnDirection(), opModeConstants.getAutoSpeed(), 360.0);
    	sleep(2000);
    	
    	opModeConstants.setDriveDirection(OPModeConstants.DriveDirection.REVERSE);
    	driver.drive(24.0, opModeConstants.getAutoSpeed(), opModeConstants.getDriveDirection());
    }
}
