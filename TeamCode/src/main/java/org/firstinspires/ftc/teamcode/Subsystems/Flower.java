package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
public class Flower {
    Servo flowerServo;
    public Flower(HardwareMap hardwareMap) {
        this.flowerServo = hardwareMap.get(Servo.class,"flowerServo");
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
}
