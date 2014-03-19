package org.dirtymechanics.frc.control;

/**
 *
 * @author Daniel Ruess
 */
public abstract class ToggleSwitch {

    private boolean released = true;
    private int toggle = 0;
    private boolean disabled;

    public final void update(boolean val) {
        if (val) {
            if (released && !disabled) {
                if (toggle++ % 2 == 0) {
                    putOn();
                } else {
                    putOff();
                }
                released = false;
            }
        } else {
            released = true;
        }
    }

    public void setOn() {
        if (toggle % 2 != 0) {
            toggle++;
        }
    }

    public void setOff() {
        if (toggle % 2 != 0) {
            toggle++;
        }
    }

    public void set(boolean state) {
        if (state) {
            setOn();
        } else {
            setOff();
        }
    }

    public void disable() {
        disabled = true;
    }

    public void enable() {
        disabled = false;
    }

    protected abstract void putOn();

    protected abstract void putOff();
}
