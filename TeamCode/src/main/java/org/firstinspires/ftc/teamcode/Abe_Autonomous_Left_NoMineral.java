package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Kyle Stang on 22-Feb-2019
 */
@Autonomous(name="Competition Left No Mineral", group="Game Opmodes")
public class Abe_Autonomous_Left_NoMineral extends LinearOpMode{

    private OPModeConstants opModeConstants;
    private ElapsedTime elapsedTime;
    private Helper_OPModeDriverV3 driveHelper;

    @Override
    public void runOpMode(){

        elapsedTime = new ElapsedTime();
        opModeConstants = OPModeConstants.getInstance();
        driveHelper = Helper_OPModeDriverV3.getInstance();

        driveHelper.init(telemetry, hardwareMap, opModeConstants, this);

        // Create tasks
        Task_ArmRelease armRelease = new Task_ArmRelease(this, hardwareMap, elapsedTime, opModeConstants);
        Task_RaiseArm raiseArm = new Task_RaiseArm(this, hardwareMap, elapsedTime, opModeConstants);
        Task_DropFlag dropFlag = new Task_DropFlag(this, hardwareMap, elapsedTime, opModeConstants);

        waitForStart();

        elapsedTime.reset();

        telemetry.addData("Status", "Running");
        telemetry.update();

        // Start of game actions
        // Release brake
        armRelease.init();
        armRelease.performTask();
        while (armRelease.getTaskStatus() == false && !isStopRequested()){
            sleep(10);
        }

        sleep(opModeConstants.restTimeMilli);

        // Lower robot
        raiseArm.init();
        raiseArm.performTask();
        while (raiseArm.getTaskStatus() == false && !isStopRequested()){
            sleep(5);
        }

        sleep(opModeConstants.restTimeMilli);

        sleep(opModeConstants.restTimeMilli);

        // Make robot face forward
        driveHelper.driveTurn(OPModeConstants.TurnDirection.LEFT, OPModeConstants.AutonomousSpeed.LOW, 235.0);

        sleep(opModeConstants.restTimeMilli);
        driveHelper.drive(33.0, OPModeConstants.AutonomousSpeed.MEDIUM, OPModeConstants.DriveDirection.FORWARD);
        sleep(opModeConstants.restTimeMilli);
        driveHelper.driveTurn(OPModeConstants.TurnDirection.LEFT, OPModeConstants.AutonomousSpeed.LOW, 90.0);
        sleep(opModeConstants.restTimeMilli);
        driveHelper.drive(52.5, OPModeConstants.AutonomousSpeed.MEDIUM, OPModeConstants.DriveDirection.FORWARD);
        sleep(opModeConstants.restTimeMilli);
        driveHelper.driveTurn(OPModeConstants.TurnDirection.LEFT, OPModeConstants.AutonomousSpeed.LOW, 190.0);
        sleep(opModeConstants.restTimeMilli);
        driveHelper.drive(5.0, OPModeConstants.AutonomousSpeed.MEDIUM, OPModeConstants.DriveDirection.REVERSE);

        // Drop flag
        dropFlag.init();
        dropFlag.performTask();
        while (dropFlag.getTaskStatus() == false && !isStopRequested()){
            sleep(10);
        }

        // Drive to crater
        driveHelper.drive(60.0, OPModeConstants.AutonomousSpeed.MEDIUM, OPModeConstants.DriveDirection.FORWARD);

        requestOpModeStop();
    }

}
