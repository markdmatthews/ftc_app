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

public class ColorMatch {
    private int redMin;
    private int redMax = Integer.MAX_VALUE;
    private int greenMin;
    private int greenMax = Integer.MAX_VALUE;
    private int blueMin;
    private int blueMax = Integer.MAX_VALUE;
    private int alphaMin;
    private int alphaMax = Integer.MAX_VALUE;

    public ColorMatch redMin(int redMin) {
        this.redMin = redMin;

        return this;
    }

    public ColorMatch redMax(int redMax) {
        this.redMax = redMax;

        return this;
    }

    public ColorMatch greenMin(int greenMin) {
        this.greenMin = greenMin;

        return this;
    }

    public ColorMatch greenMax(int greenMax) {
        this.greenMax = greenMax;

        return this;
    }

    public ColorMatch blueMin(int blueMin) {
        this.blueMin = blueMin;

        return this;
    }

    public ColorMatch blueMax(int blueMax) {
        this.blueMax = blueMax;

        return this;
    }

    public ColorMatch alphaMin(int alphaMin) {
        this.alphaMin = alphaMin;

        return this;
    }

    public ColorMatch alphaMax(int alphaMax) {
        this.alphaMax = alphaMax;

        return this;
    }

    public boolean colorMatches(ColorSensorValues colorReading) {
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
