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

package com.hfrobots.ftc.core.hmi;

/**
 * Takes two BooleanControls, which when one is pressed, represent the minimum and maximum values
 * of the variable control.
 */
public class VariableControlAdapter implements VariableControl {
    private final BooleanControl minBooleanControl;
    private final BooleanControl maxBooleanControl;
    private final float minValue;
    private final float maxValue;
    private final float defaultValue;

    public VariableControlAdapter(float defaultValue, BooleanControl minBooleanControl, float minValue,
                                  BooleanControl maxBooleanControl, float maxValue) {
        this.defaultValue = defaultValue;
        this.minBooleanControl = minBooleanControl;
        this.minValue = minValue;
        this.maxBooleanControl = maxBooleanControl;
        this.maxValue = maxValue;
    }

    @Override
    public float getValue() {
        return 0; // FIXME: What do we do here, how do we convert boolean inputs to variable inputs?
    }
}
