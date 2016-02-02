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

package com.hfrobots.ftc.core.motion;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * HF Robotic's convenience interface around DcMotor that allows testing via mocks and simulations,
 * and cleans up usage, with options of either failing fast upon construction for motors that
 * are critical to robot operation by calling failFast() on the builder, or when built normally
 * (not mock or simulated), will replace a missing piece of hardware with a mock implementation,
 * allowing code to not have to null-check the DcMotor instance before calling any methods.
 */
public interface HfDcMotor {

    String getDeviceName();

    String getConnectionInfo();

    int getVersion();

    void close();

    DcMotorController getController();

    void setDirection(DcMotor.Direction direction);

    DcMotor.Direction getDirection();

    int getPortNumber();

    void setPower(double power);

    double getPower();

    boolean isBusy();

    void setPowerFloat();

    boolean getPowerFloat();

    void setTargetPosition(int position);

    int getTargetPosition();

    int getCurrentPosition();

    void setMode(DcMotorController.RunMode mode);

    DcMotorController.RunMode getMode();

    @Deprecated
    void setChannelMode(DcMotorController.RunMode mode);

    @Deprecated
    DcMotorController.RunMode getChannelMode();

    class Builder extends com.hfrobots.ftc.core.Builder<HfDcMotor> {
        public static Builder builder(HardwareMap hardwareMap) {
            return new Builder(hardwareMap);
        }

        protected Builder(HardwareMap hardwareMap) {
            super(hardwareMap);
        }

        @Override
        protected HfDcMotor buildMock() {
            return new MockDcMotor();
        }

        @Override
        protected HfDcMotor buildFailFast() {
            return new RealDcMotor(hardwareMap.dcMotor.get(mappedName));
        }

        @Override
        protected HfDcMotor buildSimulated() {
            return new MockDcMotor();
        }

    }

    class RealDcMotor implements HfDcMotor {
        private DcMotor dcMotor;

        protected RealDcMotor(DcMotor dcMotor) {
            this.dcMotor = dcMotor;
        }

        @Override
        public String getDeviceName() {
            return dcMotor.getDeviceName();
        }

        @Override
        public String getConnectionInfo() {
            return dcMotor.getConnectionInfo();
        }

        @Override
        public int getVersion() {
            return dcMotor.getVersion();
        }

        @Override
        public void close() {
            dcMotor.close();
        }

        @Override
        public DcMotorController getController() {
            return dcMotor.getController();
        }

        @Override
        public void setDirection(DcMotor.Direction direction) {
            dcMotor.setDirection(direction);
        }

        @Override
        public DcMotor.Direction getDirection() {
            return dcMotor.getDirection();
        }

        @Override
        public int getPortNumber() {
            return dcMotor.getPortNumber();
        }

        @Override
        public void setPower(double power) {
            dcMotor.setPower(power);
        }

        @Override
        public double getPower() {
            return dcMotor.getPower();
        }

        @Override
        public boolean isBusy() {
            return dcMotor.isBusy();
        }

        @Override
        public void setPowerFloat() {
            dcMotor.setPowerFloat();
        }

        @Override
        public boolean getPowerFloat() {
            return dcMotor.getPowerFloat();
        }

        @Override
        public void setTargetPosition(int position) {
            dcMotor.setTargetPosition(position);
        }

        @Override
        public int getTargetPosition() {
            return dcMotor.getTargetPosition();
        }

        @Override
        public int getCurrentPosition() {
            return dcMotor.getCurrentPosition();
        }

        @Override
        public void setMode(DcMotorController.RunMode mode) {
            dcMotor.setMode(mode);
        }

        @Override
        public DcMotorController.RunMode getMode() {
            return dcMotor.getMode();
        }

        @Override
        @Deprecated
        public void setChannelMode(DcMotorController.RunMode mode) {
            dcMotor.setMode(mode);
        }

        @Override
        @Deprecated
        public DcMotorController.RunMode getChannelMode() {
            return dcMotor.getMode();
        }
    }

    class MockDcMotor implements HfDcMotor {
        private DcMotor.Direction direction;
        private int portNumber;
        private DcMotorController.RunMode mode;
        private DcMotorController.DeviceMode devMode;
        private double power;
        private int targetPosition;
        private int position;
        private boolean motorPowerFloat;

        protected MockDcMotor() {
            this.direction = DcMotor.Direction.FORWARD;
            this.portNumber = -1;
            this.mode = DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
            this.devMode = DcMotorController.DeviceMode.WRITE_ONLY;
        }

        public String getDeviceName() {
            return "DC Motor";
        }

        public String getConnectionInfo() {
            return "mocked - not real";
        }

        public int getVersion() {
            return 1;
        }

        public void close() {
            this.setPowerFloat();
        }

        public DcMotorController getController() {
            return null; // FIXME: Fake controllers too?
        }

        public void setDirection(DcMotor.Direction direction) {
            this.direction = direction;
        }

        public DcMotor.Direction getDirection() {
            return this.direction;
        }

        public void setPortNumber(int portNumber) {
            this.portNumber = portNumber;
        }

        public int getPortNumber() {
            return this.portNumber;
        }

        public void setPower(double power) {
            if(direction == DcMotor.Direction.REVERSE) {
                this.power = -power;
            }

            if (mode == DcMotorController.RunMode.RUN_TO_POSITION) {
                this.power = Math.abs(power);
            }
        }

        public double getPower() {
            if(this.direction == DcMotor.Direction.REVERSE && power != 0.0D) {
                return -power;
            }

            return power;
        }

        public boolean isBusy() {
            return false;
        }

        public void setPowerFloat() {
            motorPowerFloat = true;
        }

        public boolean getPowerFloat() {
            return motorPowerFloat;
        }

        public void setTargetPosition(int position) {
            if(this.direction == DcMotor.Direction.REVERSE) {
                this.targetPosition = -position;
            } else {
                this.targetPosition = position;
            }
        }

        public int getTargetPosition() {
            return targetPosition;
        }

        public int getCurrentPosition() {
            if(this.direction == DcMotor.Direction.REVERSE) {
                return -position;
            }

            return position;
        }

        public void setMode(DcMotorController.RunMode mode) {
            this.mode = mode;
        }

        public DcMotorController.RunMode getMode() {
            return mode;
        }

        @Deprecated
        public void setChannelMode(DcMotorController.RunMode mode) {
            setMode(mode);
        }

        @Deprecated
        public DcMotorController.RunMode getChannelMode() {
            return getMode();
        }
    }
}
