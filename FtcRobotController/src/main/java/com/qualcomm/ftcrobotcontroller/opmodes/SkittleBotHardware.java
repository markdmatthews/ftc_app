package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Lauren on 11/1/15.
 */
public class SkittleBotHardware extends OpMode {
    //--------------------------------------------------------------------------
    //
    // PushBotHardware
    //

    /**
     * Construct the class.
     * <p/>
     * The system calls this member when the class is instantiated.
     */
    public SkittleBotHardware()

    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.

    } // PushBotHardware

    //--------------------------------------------------------------------------
    //
    // init
    //

    /**
     * Perform any actions that are necessary when the OpMode is enabled.
     * <p/>
     * The system calls this member once when the OpMode is enabled.
     */
    @Override
    public void init()

    {
        //
        // Use the hardwareMap to associate class members to hardware ports.
        //
        // Note that the names of the devices (i.e. arguments to the get method)
        // must match the names specified in the configuration file created by
        // the FTC Robot Controller (Settings-->Configure Robot).
        //
        // The variable below is used to provide telemetry data to a class user.
        //
        v_warning_generated = false;
        v_warning_message = "Can't map; ";

        //
        // Connect the drive wheel motors.
        //
        // The direction of the right motor is reversed, so joystick inputs can
        // be more generically applied.
        //
        try {
            y1MotorDrive = hardwareMap.dcMotor.get("y1MotorDrive");
        } catch (Exception p_exeception) {
            m_warning_message("y1MotorDrive");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            y1MotorDrive = null;
        }

        try {
            y2MotorDrive = hardwareMap.dcMotor.get("y2MotorDrive");
        } catch (Exception p_exeception) {
            m_warning_message("y2MotorDrive");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            y2MotorDrive = null;
        }

        try {
            x1MotorDrive = hardwareMap.dcMotor.get("x1MotorDrive");
        } catch (Exception p_exeception) {
            m_warning_message("x1MotorDrive");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            x1MotorDrive = null;
        }

        try {
            x2MotorDrive = hardwareMap.dcMotor.get("x2MotorDrive");
        } catch (Exception p_exeception) {
            m_warning_message("x2MotorDrive");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            x2MotorDrive = null;
        }
    } // init

    //--------------------------------------------------------------------------
    //
    // a_warning_generated
    //

    /**
     * Access whether a warning has been generated.
     */
    boolean a_warning_generated()

    {
        return v_warning_generated;

    } // a_warning_generated

    //--------------------------------------------------------------------------
    //
    // a_warning_message
    //

    /**
     * Access the warning message.
     */
    String a_warning_message()

    {
        return v_warning_message;

    } // a_warning_message

    //--------------------------------------------------------------------------
    //
    // m_warning_message
    //

    /**
     * Mutate the warning message by ADDING the specified message to the current
     * message; set the warning indicator to true.
     * <p/>
     * A comma will be added before the specified message if the message isn't
     * empty.
     */
    void m_warning_message(String p_exception_message)

    {
        if (v_warning_generated) {
            v_warning_message += ", ";
        }
        v_warning_generated = true;
        v_warning_message += p_exception_message;

    } // m_warning_message

    //--------------------------------------------------------------------------
    //
    // start
    //

    /**
     * Perform any actions that are necessary when the OpMode is enabled.
     * <p/>
     * The system calls this member once when the OpMode is enabled.
     */
    @Override
    public void start()

    {
        //
        // Only actions that are common to all Op-Modes (i.e. both automatic and
        // manual) should be implemented here.
        //
        // This method is designed to be overridden.
        //

    } // start

    //--------------------------------------------------------------------------
    //
    // loop
    //

    /**
     * Perform any actions that are necessary while the OpMode is running.
     * <p/>
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override
    public void loop()

    {
        //
        // Only actions that are common to all OpModes (i.e. both auto and\
        // manual) should be implemented here.
        //
        // This method is designed to be overridden.
        //

    } // loop

    //--------------------------------------------------------------------------
    //
    // stop
    //

    /**
     * Perform any actions that are necessary when the OpMode is disabled.
     * <p/>
     * The system calls this member once when the OpMode is disabled.
     */
    @Override
    public void stop() {
        //
        // Nothing needs to be done for this method.
        //

    } // stop

    //--------------------------------------------------------------------------
    //
    // scale_motor_power
    //

    /**
     * Scale the joystick input using a nonlinear algorithm.
     */
    float scale_motor_power(float p_power) {
        //
        // Assume no scaling.
        //
        float l_scale = 0.0f;

        //
        // Ensure the values are legal.
        //
        float l_power = Range.clip(p_power, -1, 1);

        float[] l_array =
                {0.00f, 0.05f, 0.09f, 0.10f, 0.12f
                        , 0.15f, 0.18f, 0.24f, 0.30f, 0.36f
                        , 0.43f, 0.50f, 0.60f, 0.72f, 0.85f
                        , 1.00f, 1.00f
                };

        //
        // Get the corresponding index for the specified argument/parameter.
        //
        int l_index = (int) (l_power * 16.0);
        if (l_index < 0) {
            l_index = -l_index;
        } else if (l_index > 16) {
            l_index = 16;
        }

        if (l_power < 0) {
            l_scale = -l_array[l_index];
        } else {
            l_scale = l_array[l_index];
        }

        return l_scale;

    } // scale_motor_power

    double accessY1MotorDrivePower() {

        if (y1MotorDrive != null) {
            return y1MotorDrive.getPower();
        }

        return 0.0;
    }

    double accessY2MotorDrivePower() {

        if (y2MotorDrive != null) {
            return y2MotorDrive.getPower();
        }

        return 0.0;
    }

    double accessX1MotorDrivePower() {

        if (x1MotorDrive != null) {
            return x1MotorDrive.getPower();
        }

        return 0.0;
    }

    double accessX2MotorDrivePower() {

        if (x2MotorDrive != null) {
            return x2MotorDrive.getPower();
        }

        return 0.0;
    }

    //--------------------------------------------------------------------------
    //
    // set_drive_power
    //

    /**
     * Scale the joystick input using a nonlinear algorithm.
     */
    void set_drive_power(double x1DrivePower, double x2DrivePower,
                         double y1DrivePower, double y2DrivePower)

    {
        if (y1MotorDrive != null) {
            if (y1DrivePower < 0) {
                y1MotorDrive.setDirection(DcMotor.Direction.REVERSE);
            }

            y1MotorDrive.setPower(y1DrivePower);
        }
        if (y2MotorDrive != null) {
            if (y2DrivePower < 0) {
                y2MotorDrive.setDirection(DcMotor.Direction.REVERSE);
            }

            y2MotorDrive.setPower(y2DrivePower);
        }

        if (x1MotorDrive != null) {
            if (x1DrivePower < 0) {
                x1MotorDrive.setDirection(DcMotor.Direction.REVERSE);
            }

            x1MotorDrive.setPower(x1DrivePower);
        }

        if (x2MotorDrive != null) {
            if (x2DrivePower < 0) {
                x2MotorDrive.setDirection(DcMotor.Direction.REVERSE);
            }

            x2MotorDrive.setPower(x2DrivePower);
        }

    } // set_drive_power


    //--------------------------------------------------------------------------
    //
    // run_using_encoders
    //

    /**
     * Set both drive wheel encoders to run, if the mode is appropriate.
     */
    public void run_using_encoders()

    {
        DcMotor[] allDriveMotors = new DcMotor[] { x1MotorDrive, x2MotorDrive,
                y1MotorDrive, y2MotorDrive};

        for (DcMotor aMotor : allDriveMotors) {
            if (aMotor != null) {
                aMotor.setChannelMode
                        (DcMotorController.RunMode.RUN_USING_ENCODERS
                        );
            }
        }

    }

    /**
     * Set both drive wheel encoders to run, if the mode is appropriate.
     */
    public void run_without_drive_encoders()

    {
        DcMotor[] allDriveMotors = new DcMotor[] { x1MotorDrive, x2MotorDrive,
                y1MotorDrive, y2MotorDrive};

        for (DcMotor aMotor : allDriveMotors) {
            if (aMotor != null) {
                aMotor.setChannelMode
                        (DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
                        );
            }
        }
    }

    public void resetYDriveEncoder()

    {
        if (y1MotorDrive != null) {
            y1MotorDrive.setChannelMode
                    (DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    }

    public void resetXDriveEncoder()

    {
        if (x1MotorDrive != null) {
            x1MotorDrive.setChannelMode
                    (DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    } // reset_right_drive_encoder

    //--------------------------------------------------------------------------
    //
    // reset_drive_encoders
    //

    /**
     * Reset both drive wheel encoders.
     */
    public void reset_drive_encoders()

    {
        //
        // Reset the motor encoders on the drive wheels.
        //
        resetXDriveEncoder();
        resetYDriveEncoder();

    }

//    //--------------------------------------------------------------------------
//    //
//    // a_left_encoder_count
//    //
//
//    /**
//     * Access the left encoder's count.
//     */
//    int a_left_encoder_count() {
//        int l_return = 0;
//
//        if (v_motor_left_drive != null) {
//            l_return = v_motor_left_drive.getCurrentPosition();
//        }
//
//        return l_return;
//
//    } // a_left_encoder_count
//
//    //--------------------------------------------------------------------------
//    //
//    // a_right_encoder_count
//    //
//
//    /**
//     * Access the right encoder's count.
//     */
//    int a_right_encoder_count()
//
//    {
//        int l_return = 0;
//
//        if (v_motor_right_drive != null) {
//            l_return = v_motor_right_drive.getCurrentPosition();
//        }
//
//        return l_return;
//
//    } // a_right_encoder_count
//
//    //--------------------------------------------------------------------------
//    //
//    // has_left_drive_encoder_reached
//    //
//
//    /**
//     * Indicate whether the left drive motor's encoder has reached a value.
//     */
//    boolean has_left_drive_encoder_reached(double p_count)
//
//    {
//        //
//        // Assume failure.
//        //
//        boolean l_return = false;
//
//        if (v_motor_left_drive != null) {
//            //
//            // Has the encoder reached the specified values?
//            //
//            // TODO Implement stall code using these variables.
//            //
//            if (Math.abs(v_motor_left_drive.getCurrentPosition()) > p_count) {
//                //
//                // Set the status to a positive indication.
//                //
//                l_return = true;
//            }
//        }
//
//        //
//        // Return the status.
//        //
//        return l_return;
//
//    } // has_left_drive_encoder_reached
//
//    //--------------------------------------------------------------------------
//    //
//    // has_right_drive_encoder_reached
//    //
//
//    /**
//     * Indicate whether the right drive motor's encoder has reached a value.
//     */
//    boolean has_right_drive_encoder_reached(double p_count)
//
//    {
//        //
//        // Assume failure.
//        //
//        boolean l_return = false;
//
//        if (v_motor_right_drive != null) {
//            //
//            // Have the encoders reached the specified values?
//            //
//            // TODO Implement stall code using these variables.
//            //
//            if (Math.abs(v_motor_right_drive.getCurrentPosition()) > p_count) {
//                //
//                // Set the status to a positive indication.
//                //
//                l_return = true;
//            }
//        }
//
//        //
//        // Return the status.
//        //
//        return l_return;
//
//    } // has_right_drive_encoder_reached
//
//    //--------------------------------------------------------------------------
//    //
//    // have_drive_encoders_reached
//    //
//
//    /**
//     * Indicate whether the drive motors' encoders have reached a value.
//     */
//    boolean have_drive_encoders_reached
//    (double p_left_count
//            , double p_right_count
//    )
//
//    {
//        //
//        // Assume failure.
//        //
//        boolean l_return = false;
//
//        //
//        // Have the encoders reached the specified values?
//        //
//        if (has_left_drive_encoder_reached(p_left_count) &&
//                has_right_drive_encoder_reached(p_right_count)) {
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
//    } // have_encoders_reached
//
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
//        set_drive_power(p_left_power, p_right_power);
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
//            reset_drive_encoders();
//
//            //
//            // Stop the motors.
//            //
//            set_drive_power(0.0f, 0.0f);
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

    //--------------------------------------------------------------------------
    //
    // v_warning_generated
    //
    /**
     * Indicate whether a message is a available to the class user.
     */
    private boolean v_warning_generated = false;

    //--------------------------------------------------------------------------
    //
    // v_warning_message
    //
    /**
     * Store a message to the user if one has been generated.
     */
    private String v_warning_message;

    private DcMotor y1MotorDrive;

    private DcMotor y2MotorDrive;

    private DcMotor x1MotorDrive;

    private DcMotor x2MotorDrive;
}
