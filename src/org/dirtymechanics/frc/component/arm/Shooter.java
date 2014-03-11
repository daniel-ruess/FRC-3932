package org.dirtymechanics.frc.component.arm;

import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.util.Updatable;

/**
 * Represents the mechanism used to fire the ball of the robot.
 *
 * @author Daniel Ruess
 */
public class Shooter implements Updatable {

    private static final int FIRE_WAIT = 1000;
    public static final Location LOW = new Location(5);
    public static final Location MID_LOW = new Location(10);
    public static final Location MID_HIGH = new Location(15);
    public static final Location HIGH = new Location(20);

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
    public Shooter(ScrewDrive screw, DoubleSolenoid firingPin) {
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
        firingPin.set(true);
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
                firingPin.set(false);
                fired = false;
                //set(MID_HIGH);
            }
        }
    }
}
