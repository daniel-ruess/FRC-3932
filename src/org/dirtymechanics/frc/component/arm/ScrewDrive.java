package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Jaguar;
import org.dirtymechanics.frc.sensor.StringEncoder;
import org.dirtymechanics.frc.util.Updatable;

/**
 * A controller for a screw drive.
 *
 * @author Daniel Ru
 */
public class ScrewDrive implements Updatable {

    public static final Location TRUSS_SHOT = new Location(3);
    public static final Location PASS = new Location(1.7);
    public static final Location HIGH_9 = new Location(2.25);
    public static final Location AUTO = new Location(2.55);
    public static final Location RESET = new Location(.7); //595

    /**
     * Represents a location to move the screw drive to.
     */
    public static class Location {

        private final double loc;

        private Location(double loc) {
            this.loc = loc;
        }
    }
    /**
     * The default speed to run at.
     */
    private static final double SPEED = 1D; //TODO: calculate this.

    private final Jaguar motor;
    private final StringEncoder string;
    private double dest;

    public ScrewDrive(Jaguar motor, StringEncoder string) {
        this.motor = motor;
        this.string = string;
        set(TRUSS_SHOT);
    }

    /**
     *
     * @param destination
     */
    public final void set(Location destination) {
        // if (resetting) {
        //    nextDestination = destination;
        //} else {
        this.dest = destination.loc;
        //}
    }

    public void increaseOffset() {
        dest += .1;
    }

    public void decreaseOffset() {
        dest -= .1;
    }

    public int getPosition() {
        return string.getDistance();
    }

    public void update() {
        double loc = string.getAverageVoltage();
        double dif = Math.abs(dest - loc);
        double error = .01;

        if (dif > error) {
            if (dest < loc) {
                if (dif < .2) {
                    motor.set(.2);
                } else {
                    motor.set(SPEED);
                }
            } else {
                if (dif < .2) {
                    motor.set(0);
                } else {
                    motor.set(-1 * SPEED);
                }
            }
        } else {
            motor.set(0);
            if (dest == RESET.loc) {
                set(TRUSS_SHOT);
            }
        }
    }
}
