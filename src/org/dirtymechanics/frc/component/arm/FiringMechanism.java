package org.dirtymechanics.frc.component.arm;

import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.util.Updatable;

/**
 * Represents the mechanism used to fire the ball of the robot.
 *
 * @author Daniel Ruess
 */
public class FiringMechanism implements Updatable {

    private static final int FIRE_WAIT = 500;
    private static final int RETRACT_WAIT = 1000;
    public static final Location RESET = new Location(10);
    public static final Location FIRING = new Location(15);

    /**
     * Represents a location to move the screw drive to.
     */
    public static class Location {

        private final int loc;

        private Location(int loc) {
            this.loc = loc;
        }
    }
    /**
     * The screw drive.
     */
    private final ScrewDrive screw;
    /**
     * The solenoid used to release the button.
     */
    private final DoubleSolenoid firingPin;
    /**
     * Whether or not it fired.
     */
    private boolean fired = false;
    /**
     * The time the mechanism was last fired.
     */
    private long lastFired;

    /**
     * @param screw The screw drive.
     * @param firingPin The solenoid for releasing the buckle.
     */
    public FiringMechanism(ScrewDrive screw, DoubleSolenoid firingPin) {
        this.screw = screw;
        this.firingPin = firingPin;
    }

    /**
     * Sets the location to move the screw drive to.
     *
     * @param dest The destination.
     */
    public void set(Location dest) {
        screw.setDestination(dest.loc);
    }

    /**
     * Fires the firing pin.
     */
    public void fire() {
        firingPin.flip();
        fired = true;
        lastFired = System.currentTimeMillis();
    }

    /**
     * Updates the state of the firing pin.
     *
     * Automatically retracts the screw drive after firing.
     */
    public void update() {
        if (System.currentTimeMillis() - lastFired > FIRE_WAIT) {
            if (fired) {
                firingPin.flip();
                fired = false;
            }
            if (System.currentTimeMillis() - lastFired > RETRACT_WAIT) {
                set(RESET);
            }
        }
        if (screw.getPosition() == RESET.loc) {
            screw.setDestination(FIRING.loc);
        }
    }
}
