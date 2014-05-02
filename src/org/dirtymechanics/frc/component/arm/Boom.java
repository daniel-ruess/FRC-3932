package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Talon;
import org.dirtymechanics.frc.sensor.RotationalEncoder;
import org.dirtymechanics.frc.util.Updatable;

/**
 *
 * @author Daniel Ruess
 */
public class Boom implements Updatable {

    public static final Location MAX = new Location(1.00);
    public static final Location MIN = new Location(4.10);

    public static final Location REST = new Location(1.25);
    public static final Location AUTONOMOUS_SHOT = new Location(1.74);
    public static final Location HIGH_GOAL = new Location(1.74);
    public static final Location GROUND = MIN;
    public static final Location PASS = new Location(2.75);//GROUND;//new Location(3.26);

    private static final double SPEED = .7D;
    private static final double ERROR = .05;
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
            set(HIGH_GOAL);
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
        if (dest < MAX.loc) {
            dest = MAX.loc;
        } else if (dest > MIN.loc) {
            dest = MIN.loc;
        }
        if (dif > ERROR) {
            double scale = 1;
            if (dif <= .4) {
                scale = .22;
            }
            if (dest > rot.getVoltage()) {
                motor.set(SPEED * scale);
            } else {
                motor.set(-SPEED * scale);
            }
        } else {
            if (rot.getVoltage() > 1.4 && rot.getVoltage() < 2.3) {
                motor.set(-.05);
            } else {
                motor.set(0);
            }
        }
    }
}
