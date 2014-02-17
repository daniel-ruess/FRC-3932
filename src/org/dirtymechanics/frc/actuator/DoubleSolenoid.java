package org.dirtymechanics.frc.actuator;

import edu.wpi.first.wpilibj.Relay;
import org.dirtymechanics.frc.util.Updatable;

/**
 *
 * @author Daniel Ruess
 * 
 * The point of this is to...
 */
public class DoubleSolenoid implements Updatable {

    private static final int FIRE_WAIT = 500;
    private static final int DEBOUNCE = 1000;

    private final Relay relay1;
    /**
     *
     */
    private final Relay relay2;
    /**
     * The state of the solenoid.
     */
    private boolean state;

    /** 
     * Flip is the...
     */
    private boolean flip = true;
    private long flipTime;

    /** 
     * Whether or not to bounce?
     */
    private boolean bounce = true;
    private long bounceTime;

    public DoubleSolenoid(Relay a, Relay b) {
        this(a, b, false);
    }

    public DoubleSolenoid(Relay a, Relay b, boolean initialState) {
        this.relay1 = a;
        this.relay2 = b;
        this.state = initialState;
    }

    public void setState(boolean state) {
        if (System.currentTimeMillis() - bounceTime > DEBOUNCE) {
            if (this.state != state) {
                flip = true;
            }
            this.state = state;
        } else {
            bounceTime = System.currentTimeMillis();
        }
    }

    public void flip() {
        if (System.currentTimeMillis() - bounceTime > DEBOUNCE) {
            flip = true;
            state = !state;
        } else {
            bounceTime = System.currentTimeMillis();
        }
    }

    public void update() {
        if (flip) {
            flipTime = System.currentTimeMillis();
            flip = false;
        } else {
            if (System.currentTimeMillis() - flipTime < FIRE_WAIT) {
                if (state) {
                    relay1.set(Relay.Value.kForward);
                    relay2.set(Relay.Value.kOn);
                } else {
                    relay1.set(Relay.Value.kOn);
                    relay2.set(Relay.Value.kForward);
                }
            } else {
                relay1.set(Relay.Value.kOn);
                relay2.set(Relay.Value.kOn);
            }
        }

    }
}
