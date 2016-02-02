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

public interface Gamepad {
    VariableControl getLeftStickX();

    VariableControl getLeftStickY();

    VariableControl getRightStickX();

    VariableControl getRightStickY();

    BooleanControl getAButton();

    BooleanControl getBButton();

    BooleanControl getXButton();

    BooleanControl getYButton();

    BooleanControl getDPadUp();
    BooleanControl getDPadDown();
    BooleanControl getDPadLeft();
    BooleanControl getDPadRight();

    BooleanControl getGuide();
    BooleanControl getStart();
    BooleanControl getBack();
    BooleanControl getLeftBumper();
    BooleanControl getRightBumper();
    BooleanControl getLeftStickButton();
    BooleanControl getRightStickButton();
    VariableControl getLeftTrigger();
    VariableControl getRightTrigger();

    class RealGamepad implements Gamepad {
        private final com.qualcomm.robotcore.hardware.Gamepad realGamepad;

        private final VariableControl leftStickX;

        private final VariableControl leftStickY;

        private final VariableControl rightStickX;

        private final VariableControl rightStickY;

        private final BooleanControl aButton;

        private final BooleanControl bButton;

        private final BooleanControl xButton;

        private final BooleanControl yButton;

        private final BooleanControl dPadUp;
        private final BooleanControl dPadDown;
        private final BooleanControl dPadLeft;
        private final BooleanControl dPadRight;

        private final BooleanControl guide;
        private final BooleanControl start;
        private final BooleanControl back;
        private final BooleanControl leftBumper;
        private final BooleanControl rightBumper;
        private final BooleanControl leftStickButton;
        private final BooleanControl rightStickButton;
        private final VariableControl leftTrigger;
        private final VariableControl rightTrigger;

        protected RealGamepad(com.qualcomm.robotcore.hardware.Gamepad realGamepad) {
            this.realGamepad = realGamepad;

            leftStickX = new VariableControl() {
                @Override
                public float getValue() {
                    return RealGamepad.this.realGamepad.left_stick_x;
                }
            };

            leftStickY = new VariableControl() {
                @Override
                public float getValue() {
                    return RealGamepad.this.realGamepad.left_stick_y;
                }
            };

            rightStickX = new VariableControl() {
                @Override
                public float getValue() {
                    return RealGamepad.this.realGamepad.right_stick_x;
                }
            };

            rightStickY = new VariableControl() {
                @Override
                public float getValue() {
                    return RealGamepad.this.realGamepad.right_stick_y;
                }
            };

            aButton = new BooleanControl() {
                @Override
                public boolean isPressed() {
                    return RealGamepad.this.realGamepad.a;
                }
            };

            bButton = new BooleanControl() {
                @Override
                public boolean isPressed() {
                    return RealGamepad.this.realGamepad.b;
                }
            };

            xButton = new BooleanControl() {
                @Override
                public boolean isPressed() {
                    return RealGamepad.this.realGamepad.x;
                }
            };

            yButton = new BooleanControl() {
                @Override
                public boolean isPressed() {
                    return RealGamepad.this.realGamepad.y;
                }
            };

            dPadUp = new BooleanControl() {
                @Override
                public boolean isPressed() {
                    return RealGamepad.this.realGamepad.dpad_up;
                }
            };

            dPadDown = new BooleanControl() {
                @Override
                public boolean isPressed() {
                    return RealGamepad.this.realGamepad.dpad_down;
                }
            };

            dPadLeft = new BooleanControl() {
                @Override
                public boolean isPressed() {
                    return RealGamepad.this.realGamepad.dpad_left;
                }
            };

            dPadRight = new BooleanControl() {
                @Override
                public boolean isPressed() {
                    return RealGamepad.this.realGamepad.dpad_right;
                }
            };

            guide = new BooleanControl() {
                @Override
                public boolean isPressed() {
                    return RealGamepad.this.realGamepad.guide;
                }
            };

            start = new BooleanControl() {
                @Override
                public boolean isPressed() {
                    return RealGamepad.this.realGamepad.start;
                }
            };

            back  = new BooleanControl() {
                @Override
                public boolean isPressed() {
                    return RealGamepad.this.realGamepad.back;
                }
            };

            leftBumper  = new BooleanControl() {
                @Override
                public boolean isPressed() {
                    return RealGamepad.this.realGamepad.left_bumper;
                }
            };

            rightBumper  = new BooleanControl() {
                @Override
                public boolean isPressed() {
                    return RealGamepad.this.realGamepad.right_bumper;
                }
            };

            leftStickButton  = new BooleanControl() {
                @Override
                public boolean isPressed() {
                    return RealGamepad.this.realGamepad.left_stick_button;
                }
            };

            rightStickButton  = new BooleanControl() {
                @Override
                public boolean isPressed() {
                    return RealGamepad.this.realGamepad.right_stick_button;
                }
            };

            leftTrigger = new VariableControl() {
                @Override
                public float getValue() {
                    return RealGamepad.this.realGamepad.left_trigger;
                }
            };

            rightTrigger  = new VariableControl() {
                @Override
                public float getValue() {
                    return RealGamepad.this.realGamepad.right_trigger;
                }
            };
        }

        @Override
        public VariableControl getLeftStickX() {
            return leftStickX;
        }

        @Override
        public VariableControl getLeftStickY() {
            return leftStickY;
        }

        @Override
        public VariableControl getRightStickX() {
            return rightStickX;
        }

        @Override
        public VariableControl getRightStickY() {
            return rightStickY;
        }

        @Override
        public BooleanControl getAButton() {
            return aButton;
        }

        @Override
        public BooleanControl getBButton() {
            return bButton;
        }

        @Override
        public BooleanControl getXButton() {
            return xButton;
        }

        @Override
        public BooleanControl getYButton() {
            return yButton;
        }

        @Override
        public BooleanControl getDPadUp() {
            return dPadUp;
        }

        @Override
        public BooleanControl getDPadDown() {
            return dPadDown;
        }

        @Override
        public BooleanControl getDPadLeft() {
            return dPadLeft;
        }

        @Override
        public BooleanControl getDPadRight() {
            return dPadRight;
        }

        @Override
        public BooleanControl getGuide() {
            return guide;
        }

        @Override
        public BooleanControl getStart() {
            return start;
        }

        @Override
        public BooleanControl getBack() {
            return back;
        }

        @Override
        public BooleanControl getLeftBumper() {
            return leftBumper;
        }

        @Override
        public BooleanControl getRightBumper() {
            return rightBumper;
        }

        @Override
        public BooleanControl getLeftStickButton() {
            return leftStickButton;
        }

        @Override
        public BooleanControl getRightStickButton() {
            return rightStickButton;
        }

        @Override
        public VariableControl getLeftTrigger() {
            return leftTrigger;
        }

        @Override
        public VariableControl getRightTrigger() {
            return rightTrigger;
        }
    }
}
