package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Lauren on 11/1/15.
 */
public class SkittleBotTelemetry extends SkittleBotHardware {
    //--------------------------------------------------------------------------
    //
    // update_telemetry
    //
    /**
     * Update the telemetry with current values from the base class.
     */
    public void update_telemetry ()

    {
        if (a_warning_generated ())
        {
            set_first_message (a_warning_message ());
        }
        //
        // Send telemetry data to the driver station.
        //
//        telemetry.addData
//                ( "01"
//                        , "Left Drive: "
//                                + a_left_drive_power ()
//                                + ", "
//                                + a_left_encoder_count ()
//                );
//        telemetry.addData
//                ( "02"
//                        , "Right Drive: "
//                                + a_right_drive_power ()
//                                + ", "
//                                + a_right_encoder_count ()
//                );
//        telemetry.addData
//                ( "03"
//                        , "Left Arm: " + a_left_arm_power ()
//                );
//        telemetry.addData
//                ( "04"
//                        , "Hand Position: " + a_hand_position ()
//                );

    } // update_telemetry

    //--------------------------------------------------------------------------
    //
    // update_gamepad_telemetry
    //
    /**
     * Update the telemetry with current gamepad readings.
     */
    public void update_gamepad_telemetry ()

    {
        //
        // Send telemetry data concerning gamepads to the driver station.
        //
        telemetry.addData ("05", "GP1 Left x: " + -gamepad1.left_stick_x);
        telemetry.addData ("06", "GP1 Left y: " + -gamepad1.left_stick_y);
        telemetry.addData ("07", "GP1 Right x: " + -gamepad1.right_stick_x);
        telemetry.addData ("08", "GP1 Right y: " + -gamepad1.right_stick_y);

//        telemetry.addData ("07", "GP1 Left: " + -gamepad2.left_stick_y);
//        telemetry.addData ("08", "GP2 X: " + gamepad2.x);
//        telemetry.addData ("09", "GP2 Y: " + gamepad2.y);
//        telemetry.addData ("10", "GP1 LT: " + gamepad1.left_trigger);
//        telemetry.addData ("11", "GP1 RT: " + gamepad1.right_trigger);

    } // update_gamepad_telemetry

    //--------------------------------------------------------------------------
    //
    // set_first_message
    //
    /**
     * Update the telemetry's first message with the specified message.
     */
    public void set_first_message (String p_message)

    {
        telemetry.addData ( "00", p_message);

    } // set_first_message

    //--------------------------------------------------------------------------
    //
    // set_error_message
    //
    /**
     * Update the telemetry's first message to indicate an error.
     */
    public void set_error_message (String p_message)

    {
        set_first_message ("ERROR: " + p_message);

    } // set_error_message
}