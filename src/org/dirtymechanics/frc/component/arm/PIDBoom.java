package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import org.dirtymechanics.frc.sensor.RotationalEncoder;

/**
 *
 * @author Daniel Ruess
 */
public class PIDBoom extends Boom {
    static final double P = 3d;
   PIDSubsystem pid;


    public PIDBoom(Talon motor, RotationalEncoder rot) {
    //public PIDBoom(Talon motor, AnalogChannel rot) {
        super(motor, rot);
        pid = new BoomPIDController();
        //Boom does this in it's constructor, but without the pid 
        //  enabled that won't do anything.
        if (BOOM_ENABLED) {
            set(PASS);
        }
        
    }
    
    class BoomPIDController extends PIDSubsystem {
        
        public BoomPIDController() {
            super("Boom", P, 0, 0);
            enable();
        }

        protected double returnPIDInput() {
           return rot.pidGet();
       }

       protected void usePIDOutput(double output) {
           motor.set(output);

       }

       protected void initDefaultCommand() {
       }
    }

    public final void set(Location dest) {
       super.set(dest);
       pid.setSetpoint(dest.loc);
    }

    public void increaseOffset() {
        dest -= .05;
        pid.setSetpoint(dest);
    }

    public void decreaseOffset() {
        dest += .05;
        pid.setSetpoint(dest);
    }

    /**
     * overrides parent class method to do nothing.
     */
    public void update() {
        //leave updateing the position to the pid
    }
}