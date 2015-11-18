package com.qualcomm.ftcrobotcontroller.opmodes;

public abstract class SkittleBotTelemetry extends SkittleBotHardware {

    /**
     * Update the telemetry with current values from the hardware.
     */
    public void updateTelemetry()

    {
        if (wasWarningGenerated()) {
            setFirstMessage(getWarningMessage());
        }

        //
        // Send telemetry data to the driver station.
        //
        telemetry.addData
                ( "01"
                        , "Y Axis: (P) "
                                + getY1MotorDrivePower()
                                + ", (E) "
                                + getYAxisEncoderCount()
                );
        telemetry.addData
                ( "02"
                        , "X Axis: (P) "
                                + getX1MotorDrivePower()
                                + ", (E)"
                                + getXAxisEncoderCount()
                );
        ColorSensorValues rgbValues = getColorSensorValues();
        telemetry.addData("03", "Color Sensor: " + rgbValues.toString());
    }

    public void updateGamepadTelemetry() {
        //
        // Send telemetry data concerning gamepads to the driver station.
        //
        telemetry.addData ("05", "GP1 Left x: " + -gamepad1.left_stick_x);
        telemetry.addData ("06", "GP1 Left y: " + -gamepad1.left_stick_y);
        telemetry.addData ("07", "GP1 Right x: " + -gamepad1.right_stick_x);
        telemetry.addData ("08", "GP1 Right y: " + -gamepad1.right_stick_y);
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