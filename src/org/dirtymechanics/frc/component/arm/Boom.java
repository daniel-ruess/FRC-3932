package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Talon;
import org.dirtymechanics.frc.sensor.RotationalEncoder;
import org.dirtymechanics.frc.util.Updatable;

/**
 *
 * @author Daniel Ruess
 */
public class Boom implements Updatable {

    public static final Location RESTING = new Location(2.5);
    public static final Location START = new Location(1.35);
    public static final Location HIGH_GOAL = new Location(2);
    public static final Location LOW_GOAL = new Location(3);
    public static final Location GATHERING = new Location(4.25);
    private static final double SPEED = .8D;

    public static class Location {

        private final double loc;

        private Location(double loc) {
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
        double error = .08;
        double dif = Math.abs(dest.loc - rot.getVoltage());
        if (dif > error) {
            if (dest.loc > rot.getVoltage()) {
                motor.set(.7D);
            } else {
                motor.set(-.7D);
            }
        } else {
            if (rot.getVoltage() > 1.7 && rot.getVoltage() < 4) {
                motor.set(-.05);
            } else {
                motor.set(0);
            }
        }
    }
}
