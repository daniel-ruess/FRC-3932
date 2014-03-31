
package org.dirtymechanics.event;

import edu.wpi.first.wpilibj.Joystick;
import org.dirtymechanics.frc.Woolly;

/**
 *
 * @author Daniel Ruess
 */
public class FireButtonEventHandler implements ButtonEventHandler {
    private Joystick controller;
    private Woolly robot;
    
    //For now pull in the whole robot.  Later tease apart what should be in 
    //  common into some middle ground.  Need the operatorController to
    //  check on the "safety" button.  Need the robot to call all the things
    //  to do to shoot.
    public FireButtonEventHandler(Joystick operatorController, Woolly robot) {
        this.controller = operatorController;
        this.robot = robot;
    }

    public void onEvent(int buttonEvent) {
        switch (buttonEvent) {
            case ButtonListener.SINGLE_CLICK:
                shoot();
                break;
            case ButtonListener.HOLD:
                lockonShoot();
                break;
            case ButtonListener.NEUTRAL:
                resetFireControls();
                break;
        }
    }

    private void shoot() {
        System.out.println("shoot");
    }
    
    private void lockonShoot() {
        System.out.println("lockonshoot");
    }

    private void resetFireControls() {
        System.out.println("reset");
    }
    
}
