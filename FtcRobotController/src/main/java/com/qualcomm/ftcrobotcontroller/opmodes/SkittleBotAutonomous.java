package com.qualcomm.ftcrobotcontroller.opmodes;


abstract class SkittleBotAutonomous extends SkittleBotTelemetry
{
    private RobotState currentRobotState;

    public SkittleBotAutonomous(boolean blueAlliance) {
        // FIXME: These values aren't right
        ColorMatch matchBlue = new ColorMatch().blueMin(0).greenMin(0).redMin(0);
        ColorMatch matchRed = new ColorMatch().blueMin(0).greenMin(0).redMin(0);

        final ColorMatch matchMiddleAndRescue;

        if (blueAlliance) {
            matchMiddleAndRescue = matchBlue;
        } else {
            matchMiddleAndRescue = matchRed;
        }

        RobotState doneState = new DoneState(); // what the robot does when done (i.e nothing)

        RobotState startState = new StartState();
        RobotState driveToMiddleLine = new DriveAlongXAxisUntilColor("Drive to middle", .1, matchMiddleAndRescue);
        startState.setNextState(driveToMiddleLine);
        RobotState driveOffMiddleLine = new DriveAlongYAxisTimed("Drive off middle", .1, 500);
        driveToMiddleLine.setNextState(driveOffMiddleLine);
        RobotState driveUntilRescueRepairZone = new DriveAlongYAxisUntilColor("Drive to ResQ", .1, matchMiddleAndRescue);
        driveOffMiddleLine.setNextState(driveUntilRescueRepairZone);
        RobotState driveIntoRescueRepairABit = new DriveAlongYAxisTimed("Drive into ResQ", .05, 250);
        driveUntilRescueRepairZone.setNextState(driveIntoRescueRepairABit);
        RobotState seekWhiteLine = new SeekWhiteLine("Find White Line");
        driveIntoRescueRepairABit.setNextState(seekWhiteLine);
        seekWhiteLine.setNextState(doneState);
        // Robot starts the state machine at the start state
        currentRobotState = startState;
    }

    @Override
    public void init() {
        super.init();
        runWithoutDriveEncoders();
    }

    /**
     * Implements a state machine that controls the robot during
     * autonomous operation.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override
    public void loop() {
        RobotState previousState = currentRobotState;
        currentRobotState = currentRobotState.doStuffAndGetNextState();
        // send the previous and current state names/date to the driver's station
        setFirstMessage("prev: " + previousState + " cur: " + currentRobotState);
        updateTelemetry();
    }

    /**
     * All robot "states" have this code to run
     */
    abstract class RobotState {
        protected RobotState nextState;
        protected String stateName;

        RobotState(String stateName) {
            this.stateName = stateName;
        }

        void setNextState(RobotState nextState) {
            this.nextState = nextState;
        }

        /**
         * Define what this state does, and what the next
         * state when the state is finished...
         */
        abstract RobotState doStuffAndGetNextState();

        @Override
        public String toString() {
            return stateName;
        }
    }

    class DoneState extends RobotState {
        DoneState() {
            super("Done");
        }

        RobotState doStuffAndGetNextState() {
            return this;
        }
    }

    class StartState extends RobotState {
        int colorSensorLedToggleCount = 0;

        StartState() {
            super("Start");
        }

        RobotState doStuffAndGetNextState() {
            // There seems to be some race/flakiness with the LED
            // on the color sensor, toggling it multiple times
            // seems to make it work more reliably...
            if (colorSensorLedToggleCount < 100) {
                colorSensorLedToggleCount++;
                if (colorSensorLedToggleCount % 2 == 0) {
                    sensorRGB.enableLed(false);
                    return this;
                } else {
                    sensorRGB.enableLed(true);
                    return this;
                }
            } else if (colorSensorLedToggleCount < 125) {
                colorSensorLedToggleCount++;
                sensorRGB.enableLed(true);
                return this;
            }

            return nextState;
        }
    }

    class ColorMatch {
        private int redMin;
        private int redMax = Integer.MAX_VALUE;
        private int greenMin;
        private int greenMax = Integer.MAX_VALUE;
        private int blueMin;
        private int blueMax = Integer.MAX_VALUE;
        private int alphaMin;
        private int alphaMax = Integer.MAX_VALUE;

        ColorMatch redMin(int redMin) {
            this.redMin = redMin;

            return this;
        }

        ColorMatch redMax(int redMax) {
            this.redMax = redMax;

            return this;
        }

        ColorMatch greenMin(int greenMin) {
            this.greenMin = greenMin;

            return this;
        }

        ColorMatch greenMax(int greenMax) {
            this.greenMax = greenMax;

            return this;
        }

        ColorMatch blueMin(int blueMin) {
            this.blueMin = blueMin;

            return this;
        }

        ColorMatch blueMax(int blueMax) {
            this.blueMax = blueMax;

            return this;
        }

        ColorMatch alphaMin(int alphaMin) {
            this.alphaMin = alphaMin;

            return this;
        }

        ColorMatch alphaMax(int alphaMax) {
            this.alphaMax = alphaMax;

            return this;
        }

        boolean colorMatches(ColorSensorValues colorReading) {
            boolean match = false;

            if ((colorReading.red >= redMin && colorReading.red <= redMax)
                    && (colorReading.green >= greenMin && colorReading.green <= greenMax)
                    && (colorReading.blue >= blueMin && colorReading.blue <= blueMax)
                    && (colorReading.alpha >= alphaMin && colorReading.alpha <= alphaMax)) {
                match = true;
            }

            return match;
        }
    }

    abstract class ColorSensingState extends RobotState {
        private final ColorMatch colorMatch;
        private ColorSensorValues lastColorReading;

        ColorSensingState(String stateName, ColorMatch colorMatch) {
            super(stateName);
            this.colorMatch = colorMatch;
        }

        protected boolean colorMatches() {
            ColorSensorValues colorReading = getColorSensorValues();
            lastColorReading = colorReading;

            return colorMatch.colorMatches(colorReading);
        }

        @Override
        public String toString() {
            if (lastColorReading != null) {
                return stateName + " cr: " + lastColorReading;
            }

            return stateName;
        }
    }

    class DriveAlongXAxisUntilColor extends ColorSensingState {
        private final double motorPower;

        DriveAlongXAxisUntilColor(String stateName, double motorPower, ColorMatch colorMatch) {
            super(stateName, colorMatch);
            this.motorPower = motorPower;
        }

        RobotState doStuffAndGetNextState() {
            if (!colorMatches()) {
                driveAlongXAxis(motorPower);

                return this; // keep doing what we were doing
            } else {
                stopAllDriveMotors();
                return nextState;
            }
        }
    }

    class DriveAlongYAxisUntilColor extends ColorSensingState {
        private final double motorPower;

        DriveAlongYAxisUntilColor(String stateName, double motorPower, ColorMatch colorMatch) {
            super(stateName, colorMatch);
            this.motorPower = motorPower;
        }

        RobotState doStuffAndGetNextState() {
            if (!colorMatches()) {
                driveAlongYAxis(motorPower);

                return this; // keep doing what we were doing
            } else {
                stopAllDriveMotors();
                return nextState;
            }
        }
    }

    abstract class TimedDrive extends RobotState {
        private final long stopAfterMs;
        private long beginTimeMs = 0;
        private long lastElapsedTime = 0;

        public TimedDrive(String stateName, long stopAfterMs) {
            super(stateName);
            this.stopAfterMs = stopAfterMs;
        }

        RobotState doStuffAndGetNextState() {
            if (beginTimeMs == 0) {
                beginTimeMs = System.currentTimeMillis();
            } else {
                long now = System.currentTimeMillis();
                long elapsedTime = now - beginTimeMs;
                lastElapsedTime = elapsedTime;

                if (elapsedTime >= stopAfterMs) {
                    stopAllDriveMotors();

                    return nextState;
                }
            }

            doTheDriving();

            return this;
        }

        abstract void doTheDriving();

        @Override
        public String toString() {
            return stateName + " for: " + stopAfterMs + ", elapsed: " + lastElapsedTime;
        }
    }

    class DriveAlongYAxisTimed extends TimedDrive {
        private double motorPower;

        public DriveAlongYAxisTimed(String stateName, double motorPower, long stopAfterMs) {
            super(stateName, stopAfterMs);
            this.motorPower = motorPower;
        }

        @Override
        void doTheDriving() {
            driveAlongYAxis(motorPower);
        }
    }

    class DriveAlongXAxisTimed extends TimedDrive {
        private double motorPower;

        public DriveAlongXAxisTimed(String stateName, double motorPower, long stopAfterMs) {
            super(stateName, stopAfterMs);
            this.motorPower = motorPower;
        }

        @Override
        void doTheDriving() {
            driveAlongXAxis(motorPower);
        }
    }

    class SeekWhiteLine extends RobotState {

        long beginTime = 0;
        long reverseDirectionTimeMs = 3000;
        int seekDirectionMultiplier = 1;

        ColorMatch whiteLine = new ColorMatch().alphaMax(0).alphaMax(5); /* FIXME: Not Right Values */

        public SeekWhiteLine(String stateName) {
            super(stateName);
        }

        @Override
        RobotState doStuffAndGetNextState() {
            if (beginTime == 0) {
                beginTime = System.currentTimeMillis();
            } else {
                long now = System.currentTimeMillis();
                long elapsedTime = now - beginTime;

                if (elapsedTime >= reverseDirectionTimeMs) {
                    seekDirectionMultiplier = -seekDirectionMultiplier;
                }
            }

            ColorSensorValues colorReading = getColorSensorValues();

            if (whiteLine.colorMatches(colorReading)) {
                stopAllDriveMotors();
                return nextState;
            }

            double drivePower = .05 * seekDirectionMultiplier;
            driveAlongXAxis(drivePower);

            return this;
        }
    }
}

