package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

public abstract class SkittleBotHardware extends OpMode {

    // Required hardware map
    // Motor Controller 1 (AL00XQ80)
    //     Port 1 - y1MotorDrive (encoder is in port 1)
    //     Port 2 - y2MotorDrive
    // Motor Controller 2 (AL00XSTZ)
    //     Port 1 - x1MotorDrive (encoder is in port 1)
    //     Port 2 - x2MotorDrive
    // Core Device Interface Module (mapped as "dim" AL00VCWV)
    //     Port 5 - mr (color sensor)

    /**
     * Perform any actions that are necessary when the OpMode is enabled.
     * <p/>
     * The system calls this member once when the OpMode is enabled.
     */
    @Override
    public void init() {
        //
        // Use the hardwareMap to associate class members to hardware ports.
        //
        // Note that the names of the devices (i.e. arguments to the get method)
        // must match the names specified in the configuration file created by
        // the FTC Robot Controller (Settings-->Configure Robot).
        //
        // The variable below is used to provide telemetry data to a class user.
        //
        warningGenerated = false;
        warningMessage = "Can't map; ";

        //
        // Connect the first color sensor (mapped as "mr")
        //

        try {
            sensorRGB = hardwareMap.colorSensor.get("mr");

        } catch (Exception p_exeception) {
            appendWarningMessage("sensorRGB");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            sensorRGB = null;
        }

        //
        // Connect the drive wheel motors.
        //

        try {
            y1MotorDrive = hardwareMap.dcMotor.get("y1MotorDrive");
        } catch (Exception p_exeception) {
            appendWarningMessage("y1MotorDrive");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            y1MotorDrive = null;
        }

        try {
            y2MotorDrive = hardwareMap.dcMotor.get("y2MotorDrive");
        } catch (Exception p_exeception) {
            appendWarningMessage("y2MotorDrive");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            y2MotorDrive = null;
        }

        try {
            x1MotorDrive = hardwareMap.dcMotor.get("x1MotorDrive");
        } catch (Exception p_exeception) {
            appendWarningMessage("x1MotorDrive");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            x1MotorDrive = null;
        }

        try {
            x2MotorDrive = hardwareMap.dcMotor.get("x2MotorDrive");
        } catch (Exception p_exeception) {
            appendWarningMessage("x2MotorDrive");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            x2MotorDrive = null;
        }
    }

    /**
     * Access whether a warning has been generated.
     */
    boolean wasWarningGenerated() {
        return warningGenerated;
    }

    /**
     * Access the warning message.
     */
    String getWarningMessage()

    {
        return warningMessage;
    }

    /**
     * Mutate the warning message by ADDING the specified message to the current
     * message; set the warning indicator to true.
     * <p/>
     * A comma will be added before the specified message if the message isn't
     * empty.
     */
    void appendWarningMessage(String exceptionMessage) {
        if (warningGenerated) {
            warningMessage += ", ";
        }
        warningGenerated = true;
        warningMessage += exceptionMessage;
    }

    /**
     * Scale the joystick input using a nonlinear algorithm.
     */
    float scaleMotorPower(float unscaledPower) {

        //
        // Ensure the values are legal.
        //
        float clippedPower = Range.clip(unscaledPower, -1, 1);

        float[] scaleFactors =
                {0.00f, 0.05f, 0.09f, 0.10f, 0.12f
                        , 0.15f, 0.18f, 0.24f, 0.30f, 0.36f
                        , 0.43f, 0.50f, 0.60f, 0.72f, 0.85f
                        , 1.00f, 1.00f
                };

        //
        // Get the corresponding index for the given unscaled power.
        //
        int scaleIndex = (int) (clippedPower * 16.0);

        if (scaleIndex < 0) {
            scaleIndex = -scaleIndex;
        } else if (scaleIndex > 16) {
            scaleIndex = 16;
        }

        final float scaledPower;

        if (clippedPower < 0) {
            scaledPower = -scaleFactors[scaleIndex];
        } else {
            scaledPower = scaleFactors[scaleIndex];
        }

        return scaledPower;
    }

    double getY1MotorDrivePower() {

        if (y1MotorDrive != null) {
            return y1MotorDrive.getPower();
        }

        return 0.0;
    }

    double getY2MotorDrivePower() {

        if (y2MotorDrive != null) {
            return y2MotorDrive.getPower();
        }

        return 0.0;
    }

    double getX1MotorDrivePower() {

        if (x1MotorDrive != null) {
            return x1MotorDrive.getPower();
        }

        return 0.0;
    }

    double getX2MotorDrivePower() {

        if (x2MotorDrive != null) {
            return x2MotorDrive.getPower();
        }

        return 0.0;
    }

    void driveAlongYAxis(double power) {
        setDrivePower(0, 0, power, -power);
    }

    void driveAlongXAxis(double power) {
        setDrivePower(power, -power, 0, 0);
    }

    void setDrivePower(double x1DrivePower, double x2DrivePower,
                       double y1DrivePower, double y2DrivePower) {
        if (y1MotorDrive != null) {
            y1MotorDrive.setPower(y1DrivePower);
        }
        if (y2MotorDrive != null) {
            y2MotorDrive.setPower(y2DrivePower);
        }

        if (x1MotorDrive != null) {
            x1MotorDrive.setPower(x1DrivePower);
        }

        if (x2MotorDrive != null) {
            x2MotorDrive.setPower(x2DrivePower);
        }
    }

    public void runUsingEncoders() {
        DcMotor[] allDriveMotors = new DcMotor[] { x1MotorDrive, x2MotorDrive,
                y1MotorDrive, y2MotorDrive};

        for (DcMotor aMotor : allDriveMotors) {
            if (aMotor != null) {
                aMotor.setMode
                        (DcMotorController.RunMode.RUN_USING_ENCODERS
                        );
            }
        }
    }

    public void runWithoutDriveEncoders() {
        DcMotor[] allDriveMotors = new DcMotor[] { x1MotorDrive, x2MotorDrive,
                y1MotorDrive, y2MotorDrive};

        for (DcMotor aMotor : allDriveMotors) {
            if (aMotor != null) {
                aMotor.setMode
                        (DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
                        );
            }
        }
    }

    public void resetYDriveEncoder() {
        if (y1MotorDrive != null) {
            y1MotorDrive.setMode
                    (DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    }

    public void resetXDriveEncoder() {
        if (x1MotorDrive != null) {
            x1MotorDrive.setMode
                    (DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    }

    /**
     * Reset both drive wheel encoders.
     */
    public void resetDriveEncoders() {
        //
        // Reset the motor encoders on the drive wheels.
        //
        resetXDriveEncoder();
        resetYDriveEncoder();
    }


    int getYAxisEncoderCount() {
        return getAxisEncoderCount(y1MotorDrive);
    }

    int getXAxisEncoderCount() {
        return getAxisEncoderCount(x1MotorDrive);
    }

    private int getAxisEncoderCount(DcMotor forMotor) {
        int encoderCount = 0;

        if (forMotor != null) {
            encoderCount = forMotor.getCurrentPosition();
        }

        return encoderCount;
    }

    boolean hasYAxisEncoderReached(double count) {
        return Math.abs(getYAxisEncoderCount()) > count;
    }

    boolean hasXAxisEncoderReached(double count) {
        return Math.abs(getXAxisEncoderCount()) > count;
    }


//    //--------------------------------------------------------------------------
//    //
//    // drive_using_encoders
//    //
//
//    /**
//     * Indicate whether the drive motors' encoders have reached a value.
//     */
//    boolean drive_using_encoders
//    (double p_left_power
//            , double p_right_power
//            , double p_left_count
//            , double p_right_count
//    )
//
//    {
//        //
//        // Assume the encoders have not reached the limit.
//        //
//        boolean l_return = false;
//
//        //
//        // Tell the system that motor encoders will be used.
//        //
//        run_using_encoders();
//
//        //
//        // Start the drive wheel motors at full power.
//        //
//        setDrivePower(p_left_power, p_right_power);
//
//        //
//        // Have the motor shafts turned the required amount?
//        //
//        // If they haven't, then the op-mode remains in this state (i.e this
//        // block will be executed the next time this method is called).
//        //
//        if (have_drive_encoders_reached(p_left_count, p_right_count)) {
//            //
//            // Reset the encoders to ensure they are at a known good value.
//            //
//            resetDriveEncoders();
//
//            //
//            // Stop the motors.
//            //
//            setDrivePower(0.0f, 0.0f);
//
//            //
//            // Transition to the next state when this method is called
//            // again.
//            //
//            l_return = true;
//        }
//
//        //
//        // Return the status.
//        //
//        return l_return;
//
//    } // drive_using_encoders
//
//    //--------------------------------------------------------------------------
//    //
//    // has_left_drive_encoder_reset
//    //
//
//    /**
//     * Indicate whether the left drive encoder has been completely reset.
//     */
//    boolean has_left_drive_encoder_reset() {
//        //
//        // Assume failure.
//        //
//        boolean l_return = false;
//
//        //
//        // Has the left encoder reached zero?
//        //
//        if (a_left_encoder_count() == 0) {
//            //
//            // Set the status to a positive indication.
//            //
//            l_return = true;
//        }
//
//        //
//        // Return the status.
//        //
//        return l_return;
//
//    } // has_left_drive_encoder_reset
//
//    //--------------------------------------------------------------------------
//    //
//    // has_right_drive_encoder_reset
//    //
//
//    /**
//     * Indicate whether the left drive encoder has been completely reset.
//     */
//    boolean has_right_drive_encoder_reset() {
//        //
//        // Assume failure.
//        //
//        boolean l_return = false;
//
//        //
//        // Has the right encoder reached zero?
//        //
//        if (a_right_encoder_count() == 0) {
//            //
//            // Set the status to a positive indication.
//            //
//            l_return = true;
//        }
//
//        //
//        // Return the status.
//        //
//        return l_return;
//
//    } // has_right_drive_encoder_reset
//
//    //--------------------------------------------------------------------------
//    //
//    // have_drive_encoders_reset
//    //
//
//    /**
//     * Indicate whether the encoders have been completely reset.
//     */
//    boolean have_drive_encoders_reset() {
//        //
//        // Assume failure.
//        //
//        boolean l_return = false;
//
//        //
//        // Have the encoders reached zero?
//        //
//        if (has_left_drive_encoder_reset() && has_right_drive_encoder_reset()) {
//            //
//            // Set the status to a positive indication.
//            //
//            l_return = true;
//        }
//
//        //
//        // Return the status.
//        //
//        return l_return;
//
//    } // have_drive_encoders_reset

    private boolean colorSensorLedEnabled = false;

    protected void enableColorSensorLed(boolean on) {
        if (sensorRGB != null) {
            colorSensorLedEnabled = on;
            sensorRGB.enableLed(on);
        }
    }

    protected boolean getColorSensorLedEnabled() {
        return colorSensorLedEnabled;
    }

    protected ColorSensorValues getColorSensorValues() {
        if (sensorRGB != null) {
            return new ColorSensorValues(sensorRGB);
        } else {
            return new ColorSensorValues();
        }
    }

    public static class ColorSensorValues {
        public final int red;
        public final int blue;
        public final int green;
        public final int alpha;

        ColorSensorValues(ColorSensor sensor) {
            red = sensor.red();
            blue = sensor.blue();
            green = sensor.green();
            alpha = sensor.alpha();

        }

        ColorSensorValues() {
            red = 0;
            blue = 0;
            green = 0;
            alpha = 0;
        }

        @Override
        public String toString() {
            return "(RGBA) " + red + ", " + green + ", " + blue + ", " + alpha;
        }
    }

    /**
     * Indicate whether a message is a available to the class user.
     */
    private boolean warningGenerated = false;

    /**
     * Store a message to the user if one has been generated.
     */
    private String warningMessage;

    private DcMotor y1MotorDrive;

    private DcMotor y2MotorDrive;

    private DcMotor x1MotorDrive;

    private DcMotor x2MotorDrive;

    protected ColorSensor sensorRGB;
}
