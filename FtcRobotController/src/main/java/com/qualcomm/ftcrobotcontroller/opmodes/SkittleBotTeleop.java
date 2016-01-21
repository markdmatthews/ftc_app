package com.qualcomm.ftcrobotcontroller.opmodes;


import com.qualcomm.robotcore.util.Range;

/**
 * Provide a basic manual operational mode that controls the holonomic drive.
 */
public class SkittleBotTeleop extends SkittleBotTelemetry

{
    protected boolean useEncoders = false;

    /*
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public SkittleBotTeleop() {
    }

    /**
     * Places the robot into the initial pre-running state.
     * Robot must fit within sizing guidelines (18x18)
     * before this state completes.
     */
    @Override
    public void init() {
        super.init();

        if (!useEncoders) {
            runWithoutDriveEncoders();
        } else {
            runUsingEncoders();
        }


        // set servos to initial positions
        setClimberDumpServoPosition(.5); // sets servo to stop position
    }


    /**
     * Implement a state machine that controls the robot during
     * manual-operation.  The state machine uses gamepad input to transition
     * between states.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override public void loop ()

    {


        steeringPriorityDrive();

        // The climber dump servo is continuous rotation, 0.5 is stop,
        // 0 is one direction, 1 the other
        if (gamepad2.dpad_down) {
            setClimberDumpServoPosition(1.0);
        } else if (gamepad2.dpad_up) {
            setClimberDumpServoPosition(0);
        } else {
            setClimberDumpServoPosition(0.5);
        }

        handleWinchControls();

        //
        // Send telemetry data to the driver station.
        //
        updateTelemetry(); // Update common telemetry
        updateGamepadTelemetry();

    }

    private void steeringPriorityDrive() {
        //----------------------------------------------------------------------
        //
        // DC Motors
        //
        // Obtain the current values of the joystick controllers.
        //
        // Note that x and y equal -1 when the joystick is pushed all of the way
        // forward (i.e. away from the human holder's body).
        //
        // The clip method guarantees the value never exceeds the range +-1.
        //
        // The DC motors are scaled to make it easier to control them at slower
        // speeds.
        //
        // The setPower methods write the motor power values to the DcMotor
        // class, but the power levels aren't applied until the loop() method ends.
        //

        float spinControl = -gamepad1.right_stick_x;

        final float yDrivePower;
        final float xDrivePower;

        if (Math.abs(spinControl) > 0) {
            yDrivePower = xDrivePower = scaleMotorPower(spinControl);
            setDrivePower(xDrivePower, xDrivePower, yDrivePower, yDrivePower);
        } else {
            float leftStickY = -gamepad1.left_stick_y;
            float leftStickX = -gamepad1.left_stick_x;

            double orientationShiftDegrees = getOrientationShiftDegrees();

            if (gamepad1.right_trigger < .65) {
                double fortyFiveDegRad = Math.toRadians(-45.0D + orientationShiftDegrees);
                double leftStickXPrime = leftStickX * Math.cos(fortyFiveDegRad) - leftStickY * Math.sin(fortyFiveDegRad);
                double leftStickYPrime = leftStickX * Math.sin(fortyFiveDegRad) + leftStickY * Math.cos(fortyFiveDegRad);

                leftStickX = (float)leftStickXPrime;
                leftStickY = (float)leftStickYPrime;
            }

            yDrivePower = scaleMotorPower(leftStickY);
            xDrivePower = scaleMotorPower(leftStickX);

            setDrivePower(xDrivePower, -xDrivePower, yDrivePower, -yDrivePower);
        }
    }

    private void simultaneousSteerDrive() {
        float spinControl = -gamepad1.right_stick_x;

        final float yDriveOnlyPower;
        final float xDriveOnlyPower;
        float spinPower = 0;

        if (Math.abs(spinControl) > 0) {
            spinPower = scaleMotorPower(spinControl);
        }

        spinPower = 0;
        float leftStickY = -gamepad1.left_stick_y;
        float leftStickX = -gamepad1.left_stick_x;

        double orientationShiftDegrees = getOrientationShiftDegrees();

        if (gamepad1.right_trigger < .65) {
            double fortyFiveDegRad = Math.toRadians(-45.0D + orientationShiftDegrees);
            double leftStickXPrime = leftStickX * Math.cos(fortyFiveDegRad) - leftStickY * Math.sin(fortyFiveDegRad);
            double leftStickYPrime = leftStickX * Math.sin(fortyFiveDegRad) + leftStickY * Math.cos(fortyFiveDegRad);

            leftStickX = (float)leftStickXPrime;
            leftStickY = (float)leftStickYPrime;
        }

        yDriveOnlyPower = scaleMotorPower(leftStickY);
        xDriveOnlyPower = scaleMotorPower(leftStickX);

        float x1DrivePower = Range.clip(xDriveOnlyPower + spinPower, -1, 1);
        float x2DrivePower = Range.clip(-xDriveOnlyPower + spinPower,-1,1);
        float y1DrivePower = Range.clip(yDriveOnlyPower + spinPower, -1,1);
        float y2DrivePower = Range.clip(-yDriveOnlyPower + spinPower,-1,1);

        setDrivePower(x1DrivePower, x2DrivePower, y1DrivePower, y2DrivePower);
    }

    private void handleWinchControls() {
        float winchPower = gamepad2.left_stick_y;
        float aimValue = gamepad2.right_stick_y;

        setWinchDrivePower(scaleMotorPower(winchPower));

        if (aimValue != 0) {
            // Remember servo range is 0.0 - 1.0
            // the loop() method gets called every 30ms or so, which is 30 times/second or so
            //
            // The scale factor is then, how quickly do we want to turn the servo from stop-to-stop
            // in seconds / 30

            double scaledAimValue = aimValue * .06667;
            setWinchAimServoPosition(getWinchAimServoPosition() + scaledAimValue);
        }
    }

    /**
     * Shifts the "front" of the robot to one of the sides, mapped to
     * the button layout on the driver's controller. The shift is only
     * in effect while the button is depressed, and returns to the normal
     * front side when the button is released.
     *
     *       Y
     *  X         B
     *       A
     */
    private double getOrientationShiftDegrees() {
        if (gamepad1.y) {
            return 0; /* we have it at zero because this is the starting point*/
        } else if (gamepad1.b) {
            return -90; /* negative because of right-hand rule, z-axis points up on our robot */
        } else if (gamepad1.a) {
            return -180;
        } else if (gamepad1.x) {
            return -270;
        } else {
             return 0; /* we want it to revert back to it original state if no button is pressed */
        }
    }
}
