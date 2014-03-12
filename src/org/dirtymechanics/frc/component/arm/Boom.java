package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Talon;
import org.dirtymechanics.frc.sensor.RotationalEncoder;
import org.dirtymechanics.frc.util.Updatable;

/**
 *
 * @author Daniel Ruess
 */
public class Boom implements Updatable {

    public static final Location START = new Location(1.35);
    public static final Location HIGH_5 = new Location(1.55);
    public static final Location HIGH_9 = new Location(2);
    public static final Location GATHERING = new Location(4.35);
    private static final double SPEED = .7D;

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
    
    public void set(double dest) {
        this.dest = new Location(dest);
    }

    public void update() {
        double error = .08;
        double dif = Math.abs(dest.loc - rot.getAverageVoltage());
        if (dif > error) {
            double scale = 1;
            if (dif <= .5) {
                scale = 2 * dif;
            }
            if (dest.loc > rot.getAverageVoltage()) {
                motor.set(SPEED * scale);
            } else {
                motor.set(-1 * SPEED * scale);
            }
        } else {
            if (rot.getAverageVoltage() > 1.8 && rot.getAverageVoltage() < 3.7) {
                motor.set(-.05);
            } else {
                motor.set(0);
            }
        }
    }
}
