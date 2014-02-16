package org.dirtymechanics.frc.component.drive;

import edu.wpi.first.wpilibj.Jaguar;

/**
 * Controls the components of the drive train on the robot.
 *
 * @author Daniel Ruess
 */
public class DriveTrain {

    /**
     * The PWM controller handling the speed of the left motors.
     */
    private final Jaguar driveLeft;
    /**
     * The PWM controller handling the speed of the right motors.
     */
    private final Jaguar driveRight;
    /**
     * The object controlling the transmission state.
     */
    private final Transmission trans;

    /**
     * @param driveLeft The PWM controller handling the speed of the left
     * motors.
     * @param driveRight The PWM controller handling the speed of the right
     * motors.
     * @param trans The <CODE>Transmission</CODE> object controlling the
     * transmission state.
     */
    public DriveTrain(Jaguar driveLeft, Jaguar driveRight, Transmission trans) {
        this.driveLeft = driveLeft;
        this.driveRight = driveRight;
        this.trans = trans;
    }

    /**
     * Sets the state of the transmission.
     *
     * @param highGear Whether or not to use high gear.
     */
    public void setTransmissionGear(boolean highGear) {
        trans.set(highGear);
    }

    /**
     * Sets the speed of the left motors.
     *
     * @param speed The speed.
     */
    public void setLeftSpeed(double speed) {
        driveRight.set(speed);
    }

    /**
     * Sets the speed of the right motors.
     *
     * @param speed The speed.
     */
    public void setRightSpeed(double speed) {
        driveLeft.set(speed);
    }
}
