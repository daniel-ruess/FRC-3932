package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Jaguar;
import org.dirtymechanics.frc.sensor.StringEncoder;
import org.dirtymechanics.frc.util.Updatable;

/**
 * Represents the mechanism used to fire the robot.
 *
 * @author Daniel Ruess
 */
public class FiringMechanism implements Updatable {

    private static final int SPEED = 1;

    private final Jaguar motor;
    private final StringEncoder string;
    private int dest;

    public FiringMechanism(Jaguar motor, StringEncoder string) {
        this.motor = motor;
        this.string = string;
    }

    public void set(int dest) {
        this.dest = dest;
    }

    public void update() {
        int dist = string.getDistance();
        if (dest > dist) {
            motor.set(SPEED);
        } else if (dist > dest) {
            if (dist - dest <= 4) {
                double scale = (dist - dest) / 4;
                motor.set(-1 * SPEED * scale);
            } else {
                motor.set(-1 * SPEED);
            }
        }
    }
}
