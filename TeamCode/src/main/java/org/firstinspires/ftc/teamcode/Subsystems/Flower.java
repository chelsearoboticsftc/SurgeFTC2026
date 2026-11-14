package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Flower {
    Servo flowerServoR;
    Servo flowerServoL;
    private double rightServoPos = 0;
    private double leftServoPos = 0;
    public Flower(HardwareMap hardwareMap) {
        this.flowerServoR = hardwareMap.get(Servo.class,"flowerServo");
        this.flowerServoL = hardwareMap.get(Servo.class,"flowerServo2");
    }
    //Swaps the right flower servo from up to down and down to up
    public void flowerToggle() {
        if (!FlowerConstants.flowerToggleVar) {
            flowerServoR.setPosition(FlowerConstants.flowerDownR);
            flowerServoL.setPosition(FlowerConstants.flowerDownL);
            FlowerConstants.flowerToggleVar = true;
        } else {
            flowerServoR.setPosition(FlowerConstants.flowerUpR);
            flowerServoL.setPosition(FlowerConstants.flowerUpL);
            FlowerConstants.flowerToggleVar = false;
        }
    }
    //Resets the flower servos to down
    public void resetFlowerR() {
        flowerServoR.setPosition(FlowerConstants.flowerUpL);
        FlowerConstants.flowerToggleVar = false;
    }
    public void resetFlowerL() {
        flowerServoL.setPosition(FlowerConstants.flowerUpR);
        FlowerConstants.flowerToggleVar2 = false;
    }
    public void resetBothFlower() {
        flowerServoL.setPosition(FlowerConstants.flowerUpR);
        FlowerConstants.flowerToggleVar2 = false;
        flowerServoR.setPosition(FlowerConstants.flowerUpL);
        FlowerConstants.flowerToggleVar = false;
    }
    public void rightFlowerPosSwap(double position) {
        flowerServoR.setPosition(position);
    }
    public void leftFlowerPosSwap(double position) {
        flowerServoL.setPosition(position);
    }
    public double getRightFlowerPos() {
        double rightServoPos = flowerServoR.getPosition();
        return rightServoPos;
    }
    public double getLeftFlowerPos() {
        double leftServoPos = flowerServoL.getPosition();
        return leftServoPos;
    }
}
