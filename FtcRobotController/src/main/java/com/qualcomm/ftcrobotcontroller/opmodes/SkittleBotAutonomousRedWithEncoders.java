package com.qualcomm.ftcrobotcontroller.opmodes;

public class SkittleBotAutonomousRedWithEncoders extends SkittleBotAutonomous {
    public SkittleBotAutonomousRedWithEncoders() {
        // Setup to run as part of the Red Alliance
        super(false, true /* with encoders */);
    }
}
