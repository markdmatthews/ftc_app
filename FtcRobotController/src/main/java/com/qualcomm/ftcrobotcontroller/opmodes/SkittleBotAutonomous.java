package com.qualcomm.ftcrobotcontroller.opmodes;


/**
 *
 */
public class SkittleBotAutonomous extends SkittleBotTelemetry

{
    private enum RobotState { START, DRIVE_TO_BLUE_MIDDLE, MOVE_DOWN_LINE_NORTHEAST,
        DRIVE_TO_RESCUE_BLUE, WHITE_LINE_FIND, GO_TO_BEACON, DUMP_THE_CLIMBERS, DONE}

    private RobotState currentRobotState = RobotState.START;

    public SkittleBotAutonomous()

    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.

    }

    //--------------------------------------------------------------------------
    //
    // loop
    //
    /**
     * Implement a state machine that controls the robot during
     * autonomous operation.  T
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    private int ledToggleCount = 0;

    @Override public void loop ()

    {
        // There seems to be some race/flakiness with the LED
        // on the color sensor, toggling it multiple times
        // seems to make it work more reliably...
        if (ledToggleCount < 100) {
            ledToggleCount++;
            if (ledToggleCount %2 == 0) {
                sensorRGB.enableLed(false);
                return;
            } else {
                sensorRGB.enableLed(true);
                return;
            }
        } else if (ledToggleCount < 125) {
            ledToggleCount++;
            sensorRGB.enableLed(true);
            return;
        }

        switch (currentRobotState) {
            case START:
                // do something
                currentRobotState = robotStart();
                break;
            case DRIVE_TO_BLUE_MIDDLE:
                currentRobotState = driveToBlueMiddle();
                break;
            case MOVE_DOWN_LINE_NORTHEAST:
                currentRobotState = moveDownLineNortheast();
                break;
            case DRIVE_TO_RESCUE_BLUE:
                currentRobotState = driveToRescueBlue();
                break;
            case WHITE_LINE_FIND:
                currentRobotState = whiteLineFind();
                break;
            case GO_TO_BEACON:
                currentRobotState = goToBeacon();
                break;
            case DUMP_THE_CLIMBERS:
                currentRobotState = dumpTheClimbers();
                break;
            case DONE:
                done();
                break;
        }

        updateTelemetry();
    }

    private void done() {
    //shut down robot; it should be safed
    }

    private RobotState dumpTheClimbers() {
        if (areClimbersDumped()) {
            return RobotState.DONE;
        } else {
            return RobotState.DUMP_THE_CLIMBERS;
        }
    }

    private boolean areClimbersDumped() {
        return false;
    }

    private RobotState goToBeacon() {
        if (robotAtBeacon()) {
            return RobotState.DUMP_THE_CLIMBERS;
        } else {
            return RobotState.GO_TO_BEACON;
        }
    }

    private boolean robotAtBeacon() {
        return false;
    }

    private RobotState whiteLineFind() {
        if (foundWhiteLine()) {
            return RobotState.GO_TO_BEACON;
        } else {
            return RobotState.WHITE_LINE_FIND;
        }
    }

    private boolean foundWhiteLine() { 
        return false;
    }

    private RobotState robotStart() {
        // setup the robot
        return RobotState.DRIVE_TO_BLUE_MIDDLE;
    }

    boolean blueMiddleDrivePowerSet = false;

    private RobotState driveToBlueMiddle() {
        if (foundBlueLine()) {
            return RobotState.MOVE_DOWN_LINE_NORTHEAST;
        } else {
            if (!blueMiddleDrivePowerSet) {
                double motorPower = .07;
                driveAlongYAxis(motorPower);
                blueMiddleDrivePowerSet = true;
            }

            return RobotState.DRIVE_TO_BLUE_MIDDLE;
        }
    }

    private RobotState moveDownLineNortheast() {
        if (movedDistanceInches(36)) {
            return RobotState.DRIVE_TO_RESCUE_BLUE;
        } else {
            return RobotState.MOVE_DOWN_LINE_NORTHEAST;
        }
    }

    private RobotState driveToRescueBlue() {
        if (foundBlueLine()) {
            return RobotState.WHITE_LINE_FIND;
        } else {
            return RobotState.DRIVE_TO_RESCUE_BLUE;
        }
    }

    private boolean movedDistanceInches(int distanceInInches) {
        return false;
    }


    private boolean foundBlueLine() {
        ColorSensorValues colorReading = getColorSensorValues();

        if (colorReading.blue > 6 && colorReading.red < 7 && colorReading.green < 7 ) {
            return true;
        }
        return false;
    }
}

