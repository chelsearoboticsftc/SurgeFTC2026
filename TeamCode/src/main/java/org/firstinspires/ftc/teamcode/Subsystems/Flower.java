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
    public void flowerToggle() {
        if (!FlowerConstants.flowerToggleVar) {
            flowerServoR.setPosition(FlowerConstants.flowerDown);
            FlowerConstants.flowerToggleVar = true;
        }
        else {
            flowerServoR.setPosition(FlowerConstants.flowerUp);
            FlowerConstants.flowerToggleVar = false;
        }
    }
    public void flowerToggle2() {
        if (!FlowerConstants.flowerToggleVar2) {
            flowerServoL.setPosition(FlowerConstants.flowerDown);
            FlowerConstants.flowerToggleVar2 = true;
        }
        else {
            flowerServoL.setPosition(FlowerConstants.flowerUp);
            FlowerConstants.flowerToggleVar2 = false;
        }
    }
    public void resetFlower1() {
        flowerServoR.setPosition(0.25);
        FlowerConstants.flowerToggleVar = false;
    }
    public void resetFlower2() {
        flowerServoL.setPosition(0.25);
        FlowerConstants.flowerToggleVar2 = false;
    }
}
