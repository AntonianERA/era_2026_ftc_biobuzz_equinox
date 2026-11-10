package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@TeleOp(name="Driving", group="Linear OpMode")
//@Disabled
public class Driving extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    //Servos are 300 degrees. Use IntendedDegree/300
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;

    private DcMotor intake = null;
    private DcMotorEx outtake = null;
    CRServo outServo;
    double TPS = 0;
    double RPM_ADJUST = 3000;
    boolean outtakePower = false;

    double intakePower = 0;

    @Override
    public void runOpMode() {
        frontLeftDrive = hardwareMap.get(DcMotor.class, "front_left_drive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "back_left_drive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
        backRightDrive = hardwareMap.get(DcMotor.class, "back_right_drive");
        intake = hardwareMap.get(DcMotor.class, "intake");
        outtake = hardwareMap.get(DcMotorEx.class, "outtake");

        outServo = hardwareMap.get(CRServo.class, "load_Servo");

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
        intake.setDirection(DcMotor.Direction.REVERSE);
        outtake.setDirection(DcMotorEx.Direction.FORWARD);

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double max;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial   = -gamepad1.left_stick_y;  // Pushing stick forward gives negative value
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

            // Set up a variable for each drive wheel to save the power level for telemetry.
            double frontLeftPower  = axial - lateral + yaw;
            double frontRightPower = axial + lateral - yaw;
            double backLeftPower   = axial + lateral + yaw;
            double backRightPower  = axial - lateral - yaw;

            boolean outtakeBoost = gamepad2.rightTriggerWasPressed();
            boolean outtakeSlow = gamepad2.rightBumperWasPressed();
            boolean outtakeOn = gamepad2.xWasPressed();
            boolean powerOut = gamepad2.b;

            //Max: 312 RPM
            if (gamepad2.left_trigger_pressed){
                intakePower = 1;
            }
            else if (gamepad2.left_bumper){
                intakePower= -1;
            }
            else{
                intakePower = 0;
            }

            if (outtakeBoost){
                RPM_ADJUST += 1000;
            }
            else if (outtakeSlow){
                RPM_ADJUST -= 1000;
            }
            //Limited to 5000 RPM
            if (RPM_ADJUST > 5000) {
                TPS = 5000;
            }
            //Minimum of 1000 RPM
            if (RPM_ADJUST < 0) {
                RPM_ADJUST = 1000;
            }

            if (outtakeOn){
                outtakePower = !outtakePower;
            }
            if (outtakePower) {
                //Max: 6000 RPM; Pulses per rotation: 28; Pulses * RPM / 60
                TPS = 28 * RPM_ADJUST / 60;
            }
            else {
                TPS = 0;
            }

            // Chooses highest power, and if it exceeds max, it gets reset to max.
            max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
            max = Math.max(max, Math.abs(backLeftPower));
            max = Math.max(max, Math.abs(backRightPower));

            if (max > 1.0) {
                frontLeftPower  /= max;
                frontRightPower /= max;
                backLeftPower   /= max;
                backRightPower  /= max;
            }

            // Send calculated power to wheels
            frontLeftDrive.setPower(frontLeftPower);
            frontRightDrive.setPower(frontRightPower);
            backLeftDrive.setPower(backLeftPower);
            backRightDrive.setPower(backRightPower);
            intake.setPower(intakePower);

            outtake.setVelocity(TPS);

            //Loading Servo
            if(powerOut){
                outServo.setPower(-0.5);
            }
            else{
                outServo.setPower(0);
            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
            telemetry.addData("Outtake RPM", (outtake.getVelocity() / 28) * 60);
            telemetry.addData("Outtake Current (mA)", outtake.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Outtake Voltage", outtake.getPower());
            telemetry.update();
        }
    }
}