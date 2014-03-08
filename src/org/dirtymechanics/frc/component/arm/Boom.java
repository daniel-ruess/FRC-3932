package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Talon;
import org.dirtymechanics.frc.sensor.RotationalEncoder;
import org.dirtymechanics.frc.util.Updatable;

/**
 *
 * @author Daniel Ruess
 */
public class Boom implements Updatable {

    public static final Location RESTING = new Location(50);
    public static final Location HIGH_GOAL = new Location(50);
    public static final Location LOW_GOAL = new Location(50);
    public static final Location GATHERING = new Location(18);
    private static final double SPEED = .8D;

    public static class Location {

        private final int loc;

        private Location(int loc) {
            this.loc = loc;
        }
    }

    private final Talon motor;
    private final RotationalEncoder rot;
    private Location dest;

    public Boom(Talon motor, RotationalEncoder rot) {
        this.motor = motor;
        this.rot = rot;
    }

    public void set(Location dest) {
        this.dest = dest;
    }

    public void update() {
        /*
        if (Math.abs(rot.getDegrees() - dest.loc) > 3) {
            if (rot.getDegrees() > dest.loc) {
                motor.set(-1 * SPEED);
            } else if (rot.getDegrees() < dest.loc) {
                motor.set(SPEED);
            } else {
                motor.set(0);
            }
        } else {
            motor.set(0);
        }*/
    }
}
