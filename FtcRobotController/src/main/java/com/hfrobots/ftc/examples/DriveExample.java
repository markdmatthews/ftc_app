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

package com.hfrobots.ftc.examples;

import com.hfrobots.ftc.core.hmi.VariableControl;
import com.hfrobots.ftc.core.motion.HfDcMotor;
import com.hfrobots.ftc.core.motion.drive.TankDrive;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveExample {
    HardwareMap hardwareMap;

    public void TankDriveExample() {

        // we need a mock gamepad, but that's an exercise for the team,
        // so we'll use this clunky way for now

        final float[] leftStickYValue = new float[] { 0 };

        VariableControl leftStickY = new VariableControl() {
            @Override
            public float getValue() {
                return leftStickYValue[0];
            }
        };

        final float[] rightStickYValue = new float[] { 0 };

        VariableControl rightStickY = new VariableControl() {
            @Override
            public float getValue() {
                return rightStickYValue[0];
            }
        };


        HfDcMotor.Builder motorBuilder = HfDcMotor.Builder.builder(hardwareMap);

        // Create mock versions of these motors, but plugging in the real ones would be as simple
        // as removing the .mock() method call

        HfDcMotor leftDriveMotor = motorBuilder.withName("leftDriveMotor").mock().build();
        HfDcMotor rightDriveMotor = motorBuilder.withName("rightDriveMotor").mock().build();

        // Create a new tank drive with all of our mocks (which could also be real, but they
        // aren't right now

        TankDrive drive = new TankDrive(new HfDcMotor[] {leftDriveMotor},
                new HfDcMotor[] { rightDriveMotor },
                leftStickY, rightStickY);

        // Drive.update() needs to be called in loop()s for OpModes, in tele-op, presumably
        // it's working from either values from the controller, or a small state machine,
        // in autonomous, it will be working from values supplied by the program itself..

        drive.update();

        // "push" left stick forward, right stick back
        leftStickYValue[0] = -1;
        rightStickYValue[0] = 1;

        drive.update();

        // You should see now that the motors have registered power accordingly

        if (leftDriveMotor.getPower() > -1 || rightDriveMotor.getPower() < 1) {
            // failure
        }

    }
}
