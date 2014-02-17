package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Jaguar;

/**
 *
 * @author Daniel Ruess
 */
public class Roller {

    private final Jaguar motor;

    public Roller(Jaguar motor) {
        this.motor = motor;
    }

    public void set(boolean on) {
        if (on) {
            motor.set(1);
        } else {
            motor.set(0);
        }
    }
}
