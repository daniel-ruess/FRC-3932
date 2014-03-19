package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Talon;
import org.dirtymechanics.frc.sensor.RotationalEncoder;
import org.dirtymechanics.frc.util.Updatable;

/**
 *
 * @author Daniel Ruess
 */
public class Boom implements Updatable {

    public static final Location MAX = new Location(1.43);
    public static final Location TRUSS_SHOT = new Location(2.25);
    public static final Location PASS = new Location(2.6);
    public static final Location HIGH_9 = new Location(2.1);
    public static final Location AUTO = new Location(2.35);
    public static final Location GATHERING = new Location(4.25);
    private static final double SPEED = .7D;
    private static final double ERROR = .01D;

    public static class Location {

        private final double loc;

        private Location(double loc) {
            this.loc = loc;
        }
    }

    private final Talon motor;
    private final RotationalEncoder rot;
    private double dest;

    public Boom(Talon motor, RotationalEncoder rot) {
        this.motor = motor;
        this.rot = rot;
        set(PASS);
    }

    public void set(Location dest) {
        this.dest = dest.loc;
    }

    public void increaseOffset() {
        dest -= .05;
    }

    public void decreaseOffset() {
        dest += .05;
    }

    public void update() {
        double dif = Math.abs(dest - rot.getAverageVoltage());
        if (dest < MAX.loc) {
            dest = MAX.loc;
        } else if (dest > GATHERING.loc) {
            dest = GATHERING.loc;
        }
        if (dif > ERROR) {
            double scale = 1;
            if (dif <= .5) {
                scale = 2 * dif;
            }
            if (dest > rot.getAverageVoltage()) {
                motor.set(SPEED * scale);
            } else {
                motor.set(-SPEED * scale);
            }
        } else {
            if (rot.getAverageVoltage() > 2 && rot.getAverageVoltage() < 3.7) {
                motor.set(-.05);
            } else {
                motor.set(0);
            }
        }
    }
}
