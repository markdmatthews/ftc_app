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

package com.hfrobots.ftc.season.firstresq;

import com.hfrobots.ftc.core.sensors.ColorSensorValues;

public abstract class SkittleBotTelemetry extends SkittleBotHardware {

    protected void updatePriorityTelemetry(String message) {
        telemetry.addData("00", message);
    }

    /**
     * Update the telemetry with current values from the hardware.
     */
    public void updateTelemetry() {
        if (wasWarningGenerated()) {
            setFirstMessage(getWarningMessage());
        }

        //
        // Send telemetry data to the driver station.
        //
        telemetry.addData
                ( "02"
                        , "Y Axis: (P) "
                                + getY1MotorDrivePower()
                                + ", (E) "
                                + getYAxisEncoderCount()
                );
        telemetry.addData
                ( "03"
                        , "X Axis: (P) "
                                + getX1MotorDrivePower()
                                + ", (E)"
                                + getXAxisEncoderCount()
                );
        ColorSensorValues rgbValues = sensorRGB.getColorSensorValues();
        telemetry.addData("04", "Color Sensor: " + rgbValues.toString());
    }

    public void updateGamepadTelemetry() {
        //
        // Send telemetry data concerning gamepads to the driver station.
        //
        telemetry.addData ("06", "GP1 Left x: " + -gamepad1.left_stick_x);
        telemetry.addData ("07", "GP1 Left y: " + -gamepad1.left_stick_y);
        telemetry.addData ("08", "GP1 Right x: " + -gamepad1.right_stick_x);
        telemetry.addData ("09", "GP1 Right y: " + -gamepad1.right_stick_y);
    }

    public void setFirstMessage(String message) {
        telemetry.addData ( "00", message);
    }


    /**
     * Update the telemetry's first message to indicate an error.
     */
    public void setErrorMessage(String message) {
        setFirstMessage("ERROR: " + message);
    }
}