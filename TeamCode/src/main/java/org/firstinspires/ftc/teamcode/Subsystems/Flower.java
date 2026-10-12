package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.intellij.lang.annotations.Flow;

public class Flower {
    Servo flowerServo;
    Servo flowerServo2;
    public Flower(HardwareMap hardwareMap) {
        this.flowerServo = hardwareMap.get(Servo.class,"flowerServo");
        this.flowerServo2 = hardwareMap.get(Servo.class,"flowerServo2");
    }
    public void flowerToggle() {
        if (!FlowerConstants.flowerToggleVar) {
            flowerServo.setPosition(FlowerConstants.flowerDown);
            FlowerConstants.flowerToggleVar = true;
        }
        else {
            flowerServo.setPosition(FlowerConstants.flowerUp);
            FlowerConstants.flowerToggleVar = false;
        }
    }
    public void flowerToggle2() {
        if (!FlowerConstants.flowerToggleVar2) {
            flowerServo2.setPosition(FlowerConstants.flowerDown);
            FlowerConstants.flowerToggleVar2 = true;
        }
        else {
            flowerServo2.setPosition(FlowerConstants.flowerUp);
            FlowerConstants.flowerToggleVar2 = false;
        }
    }
}
