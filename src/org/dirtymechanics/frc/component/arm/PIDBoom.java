package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import org.dirtymechanics.frc.sensor.RotationalEncoder;

/**
 *
 * @author Daniel Ruess
 */
public class PIDBoom extends Boom {
    static final double P = .018d;
   PIDSubsystem pid;
   public static Location PID_PASS = new Boom.Location(500);//GROUND;//new Location(3.26);
   public static final Location PID_ARM_UP_LIMIT = new Boom.Location(150);//GROUND;//new Location(3.26);
   public static final Location PID_ARM_DOWN_LIMIT = new Boom.Location(800);//GROUND;//new Location(3.26);


    public PIDBoom(Talon motor, RotationalEncoder rot) {
    //public PIDBoom(Talon motor, AnalogChannel rot) {
        super(motor, rot);
        pid = new BoomPIDController();
        //Boom does this in it's constructor, but without the pid 
        //  enabled that won't do anything.
//        if (BOOM_ENABLED) {
//            set(PASS);
//        }
        
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
           System.out.println("motor.set(" + output +") + "  + "rot.pidGet()=" + rot.pidGet() + " getSetpoint()=" + getSetpoint() + " getPostion()=" + getPosition());

       }

       protected void initDefaultCommand() {
       }
    }

    public final void set(Location dest) {
       super.set(dest);
       pid.setSetpoint(dest.loc);
//        pid.setSetpoint(PID_PASS.loc);
    }

//    public void increaseOffset() {
//        dest -= .05;
//        pid.setSetpoint(dest);
//    }
//
//    public void decreaseOffset() {
//        dest += .05;
//        pid.setSetpoint(dest);
//    }

    /**
     * overrides parent class method to do nothing.
     */
    public void update() {
        //leave updateing the position to the pid
    }
}