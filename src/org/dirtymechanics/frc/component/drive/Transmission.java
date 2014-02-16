package org.dirtymechanics.frc.component.drive;

import org.dirtymechanics.frc.actuator.DoubleSolenoid;

/**
 * Controls all of the logic for the transmission.
 *
 * @author Daniel Ruess
 */
public class Transmission {

    /**
     * The solenoid controlling the transmission.
     */
    private final DoubleSolenoid solenoid;

    public Transmission(DoubleSolenoid solenoid) {
        this.solenoid = solenoid;
    }

    public void set(boolean state) {
        solenoid.setState(state);
    }

}
