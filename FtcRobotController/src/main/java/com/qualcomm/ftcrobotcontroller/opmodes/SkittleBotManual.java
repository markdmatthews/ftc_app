package com.qualcomm.ftcrobotcontroller.opmodes;


/**
 * Provide a basic manual operational mode that controls the holonomic drive.
 */
public class SkittleBotManual extends SkittleBotTelemetry

{
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public SkittleBotManual() {

    }

    /**
     * Places the robot into the initial pre-running state.
     * Robot must fit within sizing guidelines (18x18)
     * before this state completes.
     */
    @Override
    public void init() {
        super.init();
        runWithoutDriveEncoders();


        // set servos to initial positions
        setClimberDumpServoPosition(.5); // sets servo to stop position
        setLeftZiplineTriggerServoPosition(.5);
        setRightZiplineTriggerServoPosition(.5);
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
        // class, but the power levels aren't applied until this method ends.
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

            if (gamepad1.right_trigger > 0) {
                double fortyFiveDegRad = Math.toRadians(-45.0D);
                double leftStickXPrime = leftStickX * Math.cos(fortyFiveDegRad) - leftStickY * Math.sin(fortyFiveDegRad);
                double leftStickYPrime = leftStickX * Math.sin(fortyFiveDegRad) + leftStickY * Math.cos(fortyFiveDegRad);

                leftStickX = (float)leftStickXPrime;
                leftStickY = (float)leftStickYPrime;
            }

            yDrivePower = scaleMotorPower(leftStickY);
            xDrivePower = scaleMotorPower(leftStickX);

            setDrivePower(xDrivePower, -xDrivePower, yDrivePower, -yDrivePower);
        }

        // The climber dump servo is continuous rotation, 0.5 is stop,
        // 0 is one direction, 1 the other
        if (gamepad2.dpad_down) {
            setClimberDumpServoPosition(1.0);
        } else if (gamepad2.dpad_up) {
            setClimberDumpServoPosition(0);
        } else {
            setClimberDumpServoPosition(0.5);
        }

        if (gamepad2.left_stick_x > 0) {
            setLeftZiplineTriggerServoPosition(getLeftZiplineTriggerServoPosition() - 0.005);
        }

        if (gamepad2.left_stick_x < 0) {
            setLeftZiplineTriggerServoPosition(getLeftZiplineTriggerServoPosition() + 0.005);
        }

        if (gamepad2.right_stick_x > 0) {
            setRightZiplineTriggerServoPosition(getRightZiplineTriggerServoPosition() - 0.005);
        }

        if (gamepad2.right_stick_x < 0) {
            setRightZiplineTriggerServoPosition(getRightZiplineTriggerServoPosition() + 0.005);
        }

        //
        // Send telemetry data to the driver station.
        //
        updateTelemetry(); // Update common telemetry
        updateGamepadTelemetry();

    }

}
