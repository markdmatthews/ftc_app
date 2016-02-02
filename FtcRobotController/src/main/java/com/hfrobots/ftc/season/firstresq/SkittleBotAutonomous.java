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


import com.hfrobots.ftc.core.sensors.ColorMatch;
import com.hfrobots.ftc.core.sensors.ColorSensorValues;
import com.hfrobots.ftc.core.statemachine.ColorSensingState;
import com.hfrobots.ftc.core.statemachine.DoneState;
import com.hfrobots.ftc.core.statemachine.RobotState;
import com.hfrobots.ftc.core.statemachine.DoUntilTime;

abstract class SkittleBotAutonomous extends SkittleBotTelemetry
{
    private RobotState currentRobotState;

    private boolean useEncoders;

    public SkittleBotAutonomous(boolean blueAlliance, boolean useEncoders) {
        this.useEncoders = useEncoders;
        ColorMatch matchBlue = new ColorMatch().redMin(0).redMax(0).
                blueMin(10).blueMax(20).greenMin(2).greenMax(4).alphaMin(0).alphaMax(40);
        ColorMatch matchRed = new ColorMatch().blueMin(0).blueMax(2).greenMin(0).greenMax(40).
                redMin(17).redMax(26).alphaMin(8).alphaMax(11);

        double powerWhenDetectingTape = .3;
        double powerWhenSeeking = .3;

        final ColorMatch matchMiddleAndRescue;

        if (blueAlliance) {
            matchMiddleAndRescue = matchBlue;
        } else {
            matchMiddleAndRescue = matchRed;
            // X-axis direction is reversed when we are in the red alliance
            powerWhenDetectingTape = -powerWhenDetectingTape;
            powerWhenSeeking = -powerWhenSeeking;
        }

        RobotState doneState = new DoneState(); // what the robot does when done (i.e nothing)

        RobotState startState = new StartState();
        RobotState driveToMiddleLine = new DriveAlongXAxisUntilColor("Drive to middle", powerWhenDetectingTape * 0.57, matchMiddleAndRescue);
        startState.setNextState(driveToMiddleLine);
        // Remember, y-axis driving is *always* positive with our program
        RobotState driveOffMiddleLine = new DriveAlongYAxisTimed("Drive off middle", Math.abs(powerWhenDetectingTape), 2500);
        driveToMiddleLine.setNextState(driveOffMiddleLine);
        RobotState driveUntilRescueRepairZone = new DriveAlongYAxisUntilColor("Drive to ResQ", Math.abs(powerWhenDetectingTape), matchMiddleAndRescue);
        driveOffMiddleLine.setNextState(driveUntilRescueRepairZone);
        RobotState driveIntoRescueRepairABit = new DriveAlongYAxisTimed("Drive into ResQ", .1, 250);
        driveUntilRescueRepairZone.setNextState(driveIntoRescueRepairABit);
        RobotState seekWhiteLine = new SeekWhiteLine("Find White Line", powerWhenSeeking);
        driveIntoRescueRepairABit.setNextState(seekWhiteLine);
        RobotState alignToBeacon = new DriveAlongXAxisTimed("Aligning to beacon", -0.1, 850);
        seekWhiteLine.setNextState(alignToBeacon);
        RobotState driveUntilWallTouch = new DriveAlongYAxisUntilTouchSensorPushed("To wall", 0.2);
        alignToBeacon.setNextState(driveUntilWallTouch);
        RobotState dumpClimbers = new DumpClimbers("Dump Climbers");
        driveUntilWallTouch.setNextState(dumpClimbers);
        dumpClimbers.setNextState(doneState);

        // Robot starts the state machine at the start state
        currentRobotState = startState;
    }

    @Override
    public void init() {
        super.init();

        if (!useEncoders) {
            runWithoutDriveEncoders();
        } else {
            runUsingEncoders();
        }

        // Set servos to initial required state
        setClimberDumpServoPosition(.5); // sets servo to stop position

        enableColorSensorLed(false); // bug with FTC software, must init() with LED off, on in start
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

    class StartState extends RobotState {
        public StartState() {
            super("Start");
        }

        public RobotState doStuffAndGetNextState() {
            enableColorSensorLed(true);

            return nextState;
        }
    }

    class DriveAlongXAxisUntilColor extends ColorSensingState {
        private final double motorPower;

        DriveAlongXAxisUntilColor(String stateName, double motorPower, ColorMatch colorMatch) {
            super(stateName, sensorRGB, colorMatch);
            this.motorPower = motorPower;
        }

        public RobotState doStuffAndGetNextState() {
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
            super(stateName, sensorRGB, colorMatch);
            this.motorPower = motorPower;
        }

        public RobotState doStuffAndGetNextState() {
            if (!colorMatches()) {
                driveAlongYAxis(motorPower);

                return this; // keep doing what we were doing
            } else {
                stopAllDriveMotors();
                return nextState;
            }
        }
    }

    class DriveAlongYAxisTimed extends DoUntilTime {
        private double motorPower;

        public DriveAlongYAxisTimed(String stateName, double motorPower, long stopAfterMs) {
            super(stateName, stopAfterMs);
            this.motorPower = motorPower;
        }

        @Override
        protected void timeReached() {stopAllDriveMotors();}

        @Override
        protected void doTheDriving() {
            driveAlongYAxis(motorPower);
        }
    }

    class DriveAlongXAxisTimed extends DoUntilTime {
        private double motorPower;

        public DriveAlongXAxisTimed(String stateName, double motorPower, long stopAfterMs) {
            super(stateName, stopAfterMs);
            this.motorPower = motorPower;
        }

        @Override
        protected void timeReached() {stopAllDriveMotors();}

        @Override
        protected void doTheDriving() {
            driveAlongXAxis(motorPower);
        }
    }

    class SeekWhiteLine extends RobotState {

        long beginTime = 0;
        long reverseDirectionTimeMs = 3200;
        int seekDirectionMultiplier = 1;
        double powerWhenSeeking;
        boolean firstSeek = true;

        ColorMatch whiteLine = new ColorMatch().blueMin(25).blueMax(40).greenMin(25).greenMax(40).redMin(25).redMax(40).alphaMin(30).alphaMax(45);

        /**
         * Seeks back and forth along the X axis searching for the white line
         * in the ResQ repair zone.
         *
         * @param stateName Name of this state
         * @param powerWhenSeeking Initial power setting to use when seeking (including direction)
         */
        public SeekWhiteLine(String stateName, double powerWhenSeeking) {
            super(stateName);
            this.powerWhenSeeking = powerWhenSeeking;
        }

        @Override
        public RobotState doStuffAndGetNextState() {
            if (beginTime == 0) {
                beginTime = System.currentTimeMillis();
            } else {
                long now = System.currentTimeMillis();
                long elapsedTime = now - beginTime;

                // Seek only a portion as long first cycle, because it's only 1/2 a cycle
                if (elapsedTime >= (/* firstSeek ? reverseDirectionTimeMs / 1.7 : */ reverseDirectionTimeMs)) {
                    firstSeek = false;
                    seekDirectionMultiplier = -seekDirectionMultiplier;
                    beginTime = System.currentTimeMillis();
                    reverseDirectionTimeMs = reverseDirectionTimeMs + (reverseDirectionTimeMs / 4);
                }
            }

            ColorSensorValues colorReading = sensorRGB.getColorSensorValues();

            if (whiteLine.colorMatches(colorReading)) {
                stopAllDriveMotors();
                return nextState;
            }

            double drivePower = powerWhenSeeking * seekDirectionMultiplier;
            driveAlongXAxis(drivePower);

            return this;
        }
    }

    class DriveAlongYAxisUntilTouchSensorPushed extends RobotState {
        double motorPower;

        public DriveAlongYAxisUntilTouchSensorPushed(String stateName, double motorPower) {
            super(stateName);
            this.motorPower = motorPower;
        }

        @Override
        public RobotState doStuffAndGetNextState() {
            if (isFrontTouchSensorPressed()) {
                stopAllDriveMotors();
                return nextState;
            }

            driveAlongYAxis(motorPower);

            return this;
        }
    }

    class DumpClimbers extends RobotState {
        private final int ARM_STATE_START = 0;
        private final int ARM_STATE_RAISING = 1;
        private final int ARM_STATE_HOLDING = 2;
        private final int ARM_STATE_LOWERING = 3;

        private int currentArmState = ARM_STATE_START;

        private long raiseBeginTimeMs;
        private long holdBeginTimeMs;
        private long lowerBeginTimeMs;
        private long raiseLowerElapsedTimeMs = 1000;

        public DumpClimbers(String stateName) {
            super(stateName);
        }

        @Override
        public RobotState doStuffAndGetNextState() {
            switch (currentArmState) {
                case ARM_STATE_START:
                    raiseBeginTimeMs = System.currentTimeMillis();
                    setClimberDumpServoPosition(0); // up

                    currentArmState = ARM_STATE_RAISING;

                    return this;
                case ARM_STATE_RAISING:

                    long now = System.currentTimeMillis();
                    long elapsedTime = now - raiseBeginTimeMs;

                    if (elapsedTime >= raiseLowerElapsedTimeMs) {
                        setClimberDumpServoPosition(.5);
                        holdBeginTimeMs = System.currentTimeMillis();

                        currentArmState = ARM_STATE_HOLDING;

                        return this;
                    }

                    currentArmState = ARM_STATE_RAISING;

                    return this;
                case ARM_STATE_HOLDING:
                    now = System.currentTimeMillis();
                    elapsedTime = now - holdBeginTimeMs;

                    if (elapsedTime >= 500) {
                        setClimberDumpServoPosition(1); // down
                        lowerBeginTimeMs = System.currentTimeMillis();
                        currentArmState = ARM_STATE_LOWERING;

                        return this;
                    }

                    currentArmState = ARM_STATE_HOLDING;

                    return this;
                case ARM_STATE_LOWERING:
                    now = System.currentTimeMillis();
                    elapsedTime = now - lowerBeginTimeMs;

                    if (elapsedTime >= raiseLowerElapsedTimeMs) {
                        setClimberDumpServoPosition(0.5); // stop
                        return nextState;
                    }

                    currentArmState = ARM_STATE_LOWERING;

                    return this;
                default:
                    // error, try next state?
                    return nextState;
            }
        }
    }
}

