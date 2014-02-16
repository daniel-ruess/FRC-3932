package org.dirtymechanics.frc.actuator;

import edu.wpi.first.wpilibj.Relay;
import org.dirtymechanics.frc.util.Updatable;

/**
 *
 * @author Daniel Ruess
 */
public class DoubleSolenoid implements Updatable {

    private static final int FIRE_WAIT = 500;
    private static final int DEBOUNCE = 1000;

    private final Relay a;
    /**
     *
     */
    private final Relay b;
    /**
     * The state of the solenoid.
     */
    private boolean state;

    private boolean flip = true;
    private long flipTime;

    private boolean bounce = true;
    private long bounceTime;

    public DoubleSolenoid(Relay a, Relay b) {
        this(a, b, false);
    }

    public DoubleSolenoid(Relay a, Relay b, boolean initialState) {
        this.a = a;
        this.b = b;
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
                    a.set(Relay.Value.kForward);
                    b.set(Relay.Value.kOn);
                } else {
                    a.set(Relay.Value.kOn);
                    b.set(Relay.Value.kForward);
                }
            } else {
                a.set(Relay.Value.kOn);
                b.set(Relay.Value.kOn);
            }
        }

    }
}
