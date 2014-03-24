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
    private final Jaguar driveLeftA;
    private final Jaguar driveLeftB;
    /**
     * The PWM controller handling the speed of the right motors.
     */
    private final Jaguar driveRightA;
    private final Jaguar driveRightB;

    public DriveTrain(Jaguar driveLeftA, Jaguar driveLeftB, Jaguar driveRightA, Jaguar driveRightB) {
        this.driveLeftA = driveLeftA;
        this.driveLeftB = driveLeftB;
        this.driveRightA = driveRightA;
        this.driveRightB = driveRightB;
    }

    /**
     * Sets the speed of the left motors.
     *
     * @param speed The speed.
     */
    public void setLeftSpeed(double speed) {
        driveLeftA.set(-speed);
        driveLeftB.set(-speed);
    }

    /**
     * Sets the speed of the right motors.
     *
     * @param speed The speed.
     */
    public void setRightSpeed(double speed) {
        driveRightA.set(speed);
        driveRightB.set(speed);
    }

    /**
     * Sets both of the speeds.
     *
     * @param left The left speed.
     * @param right The right speed.
     */
    public void setSpeed(double left, double right) {
        setLeftSpeed(left);
        setRightSpeed(right);
    }
}
