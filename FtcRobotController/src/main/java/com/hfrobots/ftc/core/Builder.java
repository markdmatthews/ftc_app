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

package com.hfrobots.ftc.core;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.hardware.HardwareMap;

public abstract class Builder<T> {

    protected final HardwareMap hardwareMap;

    protected String mappedName;

    private enum BuildType { MOCK, SIMULATED, FAIL_FAST, NORMAL}

    BuildType buildType = BuildType.NORMAL;

    protected Builder(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    /**
     * Sets the name of the device being built in the FTC robot controller hardware map.
     * @param name registered name in the hardware map of the FTC robot controller
     * @return the Builder for further customization
     */
    public Builder<T> withName(String name) {
        mappedName = name;
        return this;
    }

    /**
     * After calling this, build() will return a mock implementation of the interface being built.
     */
    public Builder<T> mock() {
        buildType = BuildType.MOCK;
        return this;
    }

    /**
     * After calling this, build() will return a simulated implementation of the interface
     * being built, if one exists, otherwise it will return a mock instance.
     */
    public Builder<T> simulated() {
        buildType = BuildType.SIMULATED;
        return this;
    }

    /**
     * After calling this, build() will return a live implementation of the interface being built,
     * unless it is not available, in which case it will throw the appropriate exception.
     *
     * Consider using this method when the interface being built is absolutely required for correct
     * operation of the robot (a drive motor, for example).
     */
    public Builder<T> failFast() {
        buildType = BuildType.FAIL_FAST;
        return this;
    }

    /**
     * After calling this, build() will return a live implementation being built, unless it is
     * not available, in which case it will return a mock implementation that will respond correctly
     * to methods being called, but will not use the actual device.
     *
     * Consider using this implementation for non-critical robot functions.
     */
    public Builder<T> normal() {
        buildType = BuildType.NORMAL;
        return this;
    }

    public T build() {
        switch (buildType) {
            case NORMAL:
                return buildNormal();
            case MOCK:
                return buildMock();
            case SIMULATED:
                return buildSimulated();
            case FAIL_FAST:
                return buildFailFast();
            default:
                return buildNormal();
        }
    }

    protected T buildNormal() {
        try {
            return buildFailFast();
        } catch (Exception ex) {
            DbgLog.error("Unable to map device \"" + mappedName + "\", substituting with mock implementation");
            return buildMock();
        }
    }

    protected abstract T buildMock();

    protected abstract T buildFailFast();

    protected abstract T buildSimulated();
}
