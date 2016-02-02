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

package com.hfrobots.ftc.core.sensors;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * HF Robotic's convenience interface around ColorSensor that allows testing via mocks and simulations,
 * and cleans up usage, with options of either failing fast upon construction for sensors that
 * are critical to robot operation by calling failFast() on the builder, or when built normally
 * (not mock or simulated), will replace a missing piece of hardware with a mock implementation,
 * allowing code to not have to null-check the color sensor instance before calling any methods.
 */
public interface HfColorSensor {
    String getConnectionInfo();

    void setI2cAddress(int i);

    void enableLed(boolean b);

    int getI2cAddress();

    int getVersion();

    void close();

    String getDeviceName();

    /**
     * Our implementation doesn't give direct access to RGBA individually, it returns a snapshot
     * of all 4 values as a ColorSensorValues instance. ColorSensorValues also work with the multi-
     * axis ColorMatch class to make matching ranges of color gamut more straightforward.
     */
    ColorSensorValues getColorSensorValues();

    class Builder extends com.hfrobots.ftc.core.Builder<HfColorSensor>{

        public static Builder builder(HardwareMap hardwareMap) {
            return new Builder(hardwareMap);
        }

        protected Builder(HardwareMap hardwareMap) {
            super(hardwareMap);
        }

        @Override
        protected HfColorSensor buildMock() {
            return new MockColorSensor(mappedName);
        }

        @Override
        protected HfColorSensor buildFailFast() {
            return new RealColorSensor(hardwareMap.colorSensor.get(mappedName));
        }

        @Override
        protected HfColorSensor buildSimulated() {
            return new MockColorSensor(mappedName);
        }
    }

    class MockColorSensor implements HfColorSensor {
        private String name;

        private ColorSensorValues colorSensorValues = new ColorSensorValues();

        private int i2CAddress;

        private boolean ledEnabled;

        protected MockColorSensor(String name) {
            this.name = name;
        }

        @Override
        public String getConnectionInfo() {
            return null;
        }

        @Override
        public void setI2cAddress(int i) {
            i2CAddress = i;
        }

        @Override
        public void enableLed(boolean b) {
            ledEnabled = b;
        }

        @Override
        public int getI2cAddress() {
            return i2CAddress;
        }

        @Override
        public int getVersion() {
            return 1;
        }

        @Override
        public void close() {

        }

        @Override
        public String getDeviceName() {
            return "Modern Robotics I2C Color Sensor";
        }

        @Override
        public ColorSensorValues getColorSensorValues() {
            return colorSensorValues;
        }

        public void setColorSensorValues(ColorSensorValues csv) {
            colorSensorValues = csv;
        }
    }

    class RealColorSensor implements HfColorSensor {
        private final ColorSensor colorSensor;

        @Override
        public String getConnectionInfo() {
            return colorSensor.getConnectionInfo();
        }

        @Override
        public void setI2cAddress(int i) {
            colorSensor.setI2cAddress(i);
        }

        @Override
        public void enableLed(boolean b) {
            colorSensor.enableLed(b);
        }

        @Override
        public int getI2cAddress() {
            return colorSensor.getI2cAddress();
        }

        @Override
        public int getVersion() {
            return colorSensor.getVersion();
        }

        @Override
        public void close() {
            colorSensor.close();
        }

        @Override
        public String getDeviceName() {
            return colorSensor.getDeviceName();
        }

        protected RealColorSensor(ColorSensor colorSensor) {
            this.colorSensor = colorSensor;
        }

        @Override
        public ColorSensorValues getColorSensorValues() {
            if (colorSensor != null) {
                return new ColorSensorValues(colorSensor);
            } else {
                return new ColorSensorValues();
            }
        }
    }
}
