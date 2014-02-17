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

    /**
     * 
     * @param solenoid The DoubleSolenoid that controls the Transmission
     */
    public Transmission(DoubleSolenoid solenoid) {
        this.solenoid = solenoid;
    }

    /**
     * 
     * @param state True is the high gear, false is the low gear.
     */
    public void set(boolean state) {
        solenoid.setState(state);
    }

}
