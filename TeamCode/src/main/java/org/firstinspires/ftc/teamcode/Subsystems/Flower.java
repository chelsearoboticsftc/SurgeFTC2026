package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.intellij.lang.annotations.Flow;

public class Flower {
    Servo flowerServoR;
    Servo flowerServoL;
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
        flowerServoR.setPosition(0.2);
        FlowerConstants.flowerToggleVar = false;
    }
    public void resetFlowerL() {
        flowerServoL.setPosition(0.2);
        FlowerConstants.flowerToggleVar2 = false;
    }
}
