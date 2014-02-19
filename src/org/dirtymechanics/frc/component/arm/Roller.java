package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Jaguar;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;

/**
 *
 * @author Daniel Ruess
 */
public class Roller {

    private final Jaguar motor;
    private final DoubleSolenoid solenoid;

    public Roller(Jaguar motor, DoubleSolenoid solenoid) {
        this.motor = motor;
        this.solenoid = solenoid;
    }

    public void forward() {
        motor.set(1);
    }

    public void stop() {
        motor.set(0);

    }

    public void reverse() {

        motor.set(-1);
    }

    public void openArm() {
        solenoid.set(true);
    }

    public void closeArm() {
        solenoid.set(false);
    }
}
