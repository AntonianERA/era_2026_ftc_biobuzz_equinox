package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@Autonomous(name ="base auto")
public class auto extends LinearOpMode {
    private DcMotor front_left;
    private DcMotor back_right;
    private DcMotor back_left;
    private DcMotor front_right;
    private DcMotor intake;
    private DcMotorEx outtake;
    private Servo blockServo;
    private CRServo outServo;
    private ElapsedTime time_guy = new ElapsedTime();
    private double pastX;
    private double pastY;
    private double pastH;
    GoBildaPinpointDriver con;
    public void runOpMode(){
        front_left=hardwareMap.get(DcMotor.class,"front_left_drive");
        back_left=hardwareMap.get(DcMotor.class,"back_left_drive");
        front_right=hardwareMap.get(DcMotor.class,"front_right_drive");
        back_right=hardwareMap.get(DcMotor.class,"back_right_drive");
        con=hardwareMap.get(GoBildaPinpointDriver.class,"pins");

        intake = hardwareMap.get(DcMotor.class, "intake");
        outtake = hardwareMap.get(DcMotorEx.class, "outtake");

        blockServo = hardwareMap.get(Servo.class, "block_servo");
        outServo = hardwareMap.get(CRServo.class, "load_Servo");

        front_left.setDirection(DcMotor.Direction.REVERSE);
        back_left.setDirection(DcMotor.Direction.REVERSE);
        front_right.setDirection(DcMotor.Direction.FORWARD);
        back_right.setDirection(DcMotor.Direction.FORWARD);
        intake.setDirection(DcMotor.Direction.REVERSE);
        outtake.setDirection(DcMotorEx.Direction.FORWARD);

        //reset the4 thingy thingh
        front_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //remeber to ask for the offset in millimeters
        con.setOffsets(6,7,DistanceUnit.INCH);
        con.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        //check the diretion of both encoders Still need to test
        con.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        con.resetPosAndIMU();
        con.setPosition(new Pose2D(DistanceUnit.INCH,0,0, AngleUnit.DEGREES,0));
        pastX = 0;
        pastY=0;
        pastH=0;
        Pose2D pose = con.getPosition();
        // if shoot no work
        forward(10,false);
        //if shoot work
        forward(20,false);
        strafe(60,true);
        forward(30,false);
        turn(180,false);





    }
    // positive distance  if back true it goes backwards
    public void forward(double dist ,boolean back){
        Pose2D pose = con.getPosition();

        double tar;

        if (back){
            tar=pastX-dist;
            while (tar>pose.getX(DistanceUnit.INCH)){
                con.update();
                front_right.setPower(-1);
                back_right.setPower(-1);
                front_left.setPower(-1);
                back_left.setPower(-1);

            }

        }else{
            tar=pastX+dist;
            while (tar<pose.getX(DistanceUnit.INCH)){
                con.update();
                front_right.setPower(1);
                back_right.setPower(1);
                front_left.setPower(1);
                back_left.setPower(1);


            }
        }
        front_right.setPower(0);
        back_right.setPower(0);
        front_left.setPower(0);
        back_left.setPower(0);


    }
    //dist is the distance you want if right true makes bot go right.
    //base goes left
    public void strafe(double dist,boolean right) {

        Pose2D pose = con.getPosition();

        double tar;

        if (right) {
            tar = pastY - dist;
            while (tar > pose.getY(DistanceUnit.INCH)) {
                con.update();
                front_right.setPower(-1);
                back_right.setPower(1);
                front_left.setPower(1);
                back_left.setPower(-1);

            }

        } else {
            tar = pastY + dist;
            while (tar < pose.getY(DistanceUnit.INCH)) {
                con.update();
                front_right.setPower(1);
                back_right.setPower(-1);
                front_left.setPower(-1);
                back_left.setPower(1);


            }
        }
    }
    public void turn(double degree,boolean right) {


        Pose2D pose = con.getPosition();

        double tar;

        if (right) {
            tar = pastH - degree;
            while (tar > pose.getHeading(AngleUnit.DEGREES)) {
                con.update();
                front_right.setPower(1);
                back_right.setPower(1);
                front_left.setPower(-1);
                back_left.setPower(-1);

            }

        } else {
            tar = pastH + degree;
            while (tar < pose.getHeading(AngleUnit.DEGREES)) {
                con.update();
                front_right.setPower(-1);
                back_right.setPower(-1);
                front_left.setPower(1);
                back_left.setPower(1);

            }
        }
        front_right.setPower(0);
        back_right.setPower(0);
        front_left.setPower(0);
        back_left.setPower(0);

    }
    public void shoot(double velocity,double time){
        while (time_guy.time()<=time);{
            outtake.setVelocity(velocity);
            outServo.setPower(1);
        }
        outtake.setVelocity(0);
        outServo.setPower(0);

    }
}
