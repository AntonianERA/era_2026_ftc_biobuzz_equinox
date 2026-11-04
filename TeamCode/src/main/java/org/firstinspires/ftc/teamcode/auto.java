package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@Autonomous(name ="base auto")
public class auto extends LinearOpMode {
    private DcMotor front_left;
    private DcMotor back_right;
    private DcMotor back_left;
    private DcMotor front_right;
    private double pastX;
    private double pastY;
    GoBildaPinpointDriver con;
    public void runOpMode(){
        front_left=hardwareMap.get(DcMotor.class,"front_left");
        back_left=hardwareMap.get(DcMotor.class,"back_left");
        front_right=hardwareMap.get(DcMotor.class,"front_right");
        back_right=hardwareMap.get(DcMotor.class,"back_right");
        con=hardwareMap.get(GoBildaPinpointDriver.class,"pins");
        //reset the4 thingy thingh
        front_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //remeber to ask for the offset in millimeters
        con.setOffsets(6,7,DistanceUnit.INCH);
        con.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        //check the diretion of both encoders
        con.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        con.resetPosAndIMU();
        con.setPosition(new Pose2D(DistanceUnit.INCH,0,0, AngleUnit.DEGREES,0));
        pastX = 0;
        pastY=0;
        Pose2D pose = con.getPosition();




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
}
