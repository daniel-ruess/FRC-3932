package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Jaguar;
import org.dirtymechanics.frc.sensor.StringEncoder;
import org.dirtymechanics.frc.util.Updatable;

/**
 * A controller for a screw drive.
 *
 * @author Daniel Ruess
 */
public class ScrewDrive implements Updatable {

    public static final Location HIGH_5 = new Location(2.1);
    public static final Location HIGH_9 = new Location(2.25);
    public static final Location RESET = new Location(1);
    private Location nextDestination;

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
    private static final double SPEED = .5D; //TODO: calculate this.

    private final Jaguar motor;
    private final StringEncoder string;
    private Location destination = HIGH_5;
    private boolean resetting = true;

    public ScrewDrive(Jaguar motor, StringEncoder string) {
        this.motor = motor;
        this.string = string;
    }

    /**
     *
     * @param destination
     */
    public void set(Location destination) {
        if (resetting) {
            nextDestination = destination;
        } else {
            this.destination = destination;
        }
    }

    public int getPosition() {
        return string.getDistance();
    }

    public void reset() {
        resetting = true;
    }

    public void update() {
        double dest = this.destination.loc;
        double loc = string.getAverageVoltage();
        double error = .08;
        double dif = Math.abs(dest - loc);

        if (dif > error) {
            if (dest < loc) {
                motor.set(SPEED);
            } else {
                motor.set(-1 * SPEED);
            }
        } else {
            motor.set(0);
            if (resetting) {
                resetting = false;
                if (nextDestination.loc == RESET.loc) {
                    set(HIGH_5);
                } else {
                    set(nextDestination);
                }
            }
        }
    }
}
