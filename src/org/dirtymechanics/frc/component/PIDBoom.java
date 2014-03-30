package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import org.dirtymechanics.frc.sensor.RotationalEncoder;
import org.dirtymechanics.frc.util.Updatable;

/**
 *
 * @author Daniel Ruess
 */
public class PIDBoom extends PIDSubsystem implements Updatable {
    private static final double P = 3d;

    public static final Location MAX = new Location(3.67);
    public static final Location GROUND = new Location(1.15);

    public static final Location TRUSS_SHOT = new Location(3.44);
    public static final Location PASS = new Location(2.0);
    public static final Location HIGH_9 = new Location(2.9);
    //public static final Location HIGH_GOAL_ANGLE = new Location(3.38);
    public static final Location HIGH_GOAL_ANGLE = new Location(3.1);
    public static final Location AUTO = new Location(2.35);

    private static final double SPEED = .7D;
    private static final double ERROR = .04;
    public boolean BOOM_ENABLED = true;
    
    private final Talon motor;
    private final RotationalEncoder rot;
    private double dest;

   

    public static class Location {

        private final double loc;

        private Location(double loc) {
            this.loc = loc;
        }
    }



    public PIDBoom(Talon motor, RotationalEncoder rot) {
        super("Boom", P, 0, 0);
        this.motor = motor;
        this.rot = rot;
        getPIDController().setContinuous(false);
        if (BOOM_ENABLED) {
            set(PASS);
        }
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

    public final void set(Location dest) {
       setSetpoint(dest.loc);
    }

    public void increaseOffset() {
        dest -= .05;
    }

    public void decreaseOffset() {
        dest += .05;
    }

    public void update() {
        //leave updateing the position to the pid
    }
}
