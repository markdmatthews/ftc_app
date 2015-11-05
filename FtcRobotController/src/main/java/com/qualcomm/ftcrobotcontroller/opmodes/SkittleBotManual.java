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
    public SkittleBotManual()

    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.

    } // PushBotManual

    //--------------------------------------------------------------------------
    //
    // loop
    //
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

        //
        // Manage the drive wheel motors.
        //

        float spinControl = -gamepad1.right_stick_x;

        final float yDrivePower;
        final float xDrivePower;

        if (Math.abs(spinControl) > 0) {
            yDrivePower = xDrivePower = scale_motor_power(spinControl);

            set_drive_power(xDrivePower, xDrivePower, yDrivePower, yDrivePower);
        } else {
            yDrivePower = scale_motor_power(-gamepad1.left_stick_y);
            xDrivePower = scale_motor_power(-gamepad1.left_stick_x);

            set_drive_power(xDrivePower, -xDrivePower, yDrivePower, -yDrivePower);
        }



        //
        // Send telemetry data to the driver station.
        //
        update_telemetry (); // Update common telemetry
        update_gamepad_telemetry ();

    } // loop

} // PushBotManual
