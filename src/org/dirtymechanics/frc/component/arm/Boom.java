package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Talon;
import org.dirtymechanics.frc.sensor.RotationalEncoder;
import org.dirtymechanics.frc.util.Updatable;

/**
 *
 * @author Daniel Ruess
 */
public class Boom implements Updatable {

    public static final Location MAX = new Location(3.67);
    public static final Location GROUND = new Location(1.15);

    public static final Location TRUSS_SHOT = new Location(3.44);
    public static final Location PASS = new Location(2.0);
    public static final Location HIGH_9 = new Location(2.9);
    //public static final Location HIGH_GOAL_ANGLE = new Location(3.38);
    public static final Location HIGH_GOAL_ANGLE = new Location(3.1);
    public static final Location AUTO = new Location(2.35);

    private static final double SPEED = .7D;
    private static final double ERROR = .04;
    public boolean BOOM_ENABLED = true;

    public static class Location {

        final double loc;

        private Location(double loc) {
            this.loc = loc;
        }
    }

    final Talon motor;
    final RotationalEncoder rot;
    double dest;

    public Boom(Talon motor, RotationalEncoder rot) {
        this.motor = motor;
        this.rot = rot;
        if (BOOM_ENABLED) {
            set(PASS);
        }
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
        double dif = Math.abs(dest - rot.getVoltage());
        if (dest > MAX.loc) {
            dest = MAX.loc;
        } else if (dest < GROUND.loc) {
            dest = GROUND.loc;
        }
        if (dif > ERROR) {
            double scale = 1;
            if (dif <= .4) {
                scale = .4;
            }
            if (dest > rot.getVoltage()) {
                motor.set(-SPEED * scale);
            } else {
                motor.set(SPEED * scale);
            }
        } else {
            if (rot.getVoltage() > 1.6 && rot.getVoltage() < 3.2) {
                motor.set(.05);
            } else {
                motor.set(0);
            }
        }
    }
}
