/*
Copyright (c) 2016, HF Robotics (http://hfrobots.com)
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions
and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of
conditions and the following disclaimer in the documentation and/or other materials provided with
the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.hfrobots.ftc.core.motion.drive;


import com.hfrobots.ftc.core.hmi.VariableControl;
import com.hfrobots.ftc.core.motion.HfDcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * A Simple tank/skid-steer drive. Takes any number of motors for the left and right sides,
 * and a VariableControl that sets the power for each side.
 */
public class TankDrive {
    private final HfDcMotor[] leftSideMotors;

    private final HfDcMotor[] rightSideMotors;

    private final VariableControl leftSidePowerControl;

    private final VariableControl rightSidePowerControl;

    public TankDrive(HfDcMotor[] leftSideMotors,
                     HfDcMotor[] rightSideMotors,
                     VariableControl leftSidePowerControl,
                     VariableControl rightSidePowerControl) {
        this.leftSideMotors = leftSideMotors;
        this.rightSideMotors = rightSideMotors;
        this.leftSidePowerControl = leftSidePowerControl;
        this.rightSidePowerControl = rightSidePowerControl;
    }

    public void update() {
        double leftSidePower = leftSidePowerControl.getValue();
        leftSidePower = Range.clip(leftSidePower, -1.0, 1.0);
        double rightSidePower = rightSidePowerControl.getValue();
        rightSidePower = Range.clip(rightSidePower, -1.0, 1.0);

        for (HfDcMotor leftSideMotor : leftSideMotors) {
            leftSideMotor.setPower(leftSidePower);
        }

        for (HfDcMotor rightSideMotor : rightSideMotors) {
            rightSideMotor.setPower(rightSidePower);
        }
    }
}
