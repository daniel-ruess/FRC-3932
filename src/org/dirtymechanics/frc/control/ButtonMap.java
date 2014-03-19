package org.dirtymechanics.frc.control;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Daniel Ruess
 */
public class ButtonMap {
    private final int[] leftSpeeds = new int[15];
    private final int[] rightSpeeds = new int[15];
    

    private final Joystick left;
    private final Joystick right;
    private final Joystick cont;

    private boolean transmissionState = false;
    private boolean transmissionLast = false;
    private int transmissionFlip = 0;
    
    private int mode = 0;

    public ButtonMap(Joystick left, Joystick right, Joystick cont) {
        this.left = left;
        this.right = right;
        this.cont = cont;
    }

    public double getDriveLeft() {
        double spd = left.getY();
        double scale = 1;
        if (left.getRawButton(1)) {
            spd = right.getY();
        }
        return spd * scale;
    }

    public double getDriveRight() {
        double spd = right.getY();
        double scale = -1;
        return spd * scale;
    }

    public boolean isTransmissionHigh() {
        boolean state = right.getRawButton(1);
        if (state != transmissionLast) {
            transmissionFlip++;
            if (transmissionFlip % 2 == 0) {
                transmissionState = !transmissionState;
            }
            transmissionLast = state;
        }
        return transmissionState;
    }
    
    public boolean fire() {
        return cont.getRawButton(12);
    }
    
    /**
     * 0 = idle
     * 1 = firing
     * 2 = collecting
     * @return 
     */
    public int getMode() { //TODO: add ball sensor
        if (cont.getRawButton(2)) {
            mode = 1;
        } else if (cont.getRawButton(3)) {
            mode = 2;
        } else {
            if (cont.getRawButton(1)) {
                mode = 0;
            }
        }
        return mode;
    }
}
