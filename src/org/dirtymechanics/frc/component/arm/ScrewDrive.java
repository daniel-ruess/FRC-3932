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

    /**
     * The default speed to run at.
     */
    private static final int SPEED = 1; //TODO: calculate this.

    private final Jaguar motor;
    private final StringEncoder string;
    private int destination;

    public ScrewDrive(Jaguar motor, StringEncoder string) {
        this.motor = motor;
        this.string = string;
    }

    /**
     * 
     * @param destination 
     */
    public void setDestination(int destination) {
        this.destination = destination;
    }
    
    public int getPosition() {
        return string.getDistance();
    }

    public void update() {
        int distance = string.getDistance();
        if (destination > distance) {
            motor.set(SPEED);
        } else if (distance > destination) {
            if (distance - destination <= 4) {
                double scale = (distance - destination) / 4;
                motor.set(-1 * SPEED * scale);
            } else {
                motor.set(-1 * SPEED);
            }
        }
    }
}
