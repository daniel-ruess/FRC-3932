package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.dirtymechanics.frc.sensor.RotationalEncoder;

/**
 *
 * @author Daniel Ruess
 */
public class PIDBoom extends Boom {
    //All the locations need retouching since originals were based on voltage and news are based on encoder reading
    public static final Location PID_PASS = new Location(500.0);  

    
    public static final String SERVER_D_IN = "Boom.D.in";
    public static final String SERVER_I_IN = "Boom.I.in";
    public static final String SERVER_P_IN = "Boom.P.in";
    
    
    /*
    
Ziegler–Nichols method
Control Type	K_p	        K_i             K_d
P	        0.50{K_u}	-               -
PI	        0.45{K_u}	1.2{K_p}/P_u	-
PID	        0.60{K_u}	2{K_p}/P_u	{K_p}{P_u}/8
    
    

    */
    //Gentle 2 second period
//    public double P = .01d;
//    public double I = .00075d; //.001 close
//    public double D = .0d; //.009 would be to correct for speed
    //More aggressive oscillation
//    public double p = .011d;
//    public double i = .0022d; //.001 close
//    public double d = .001388d; //.009 would be to correct for speed
    //No oscillation but also not very self correcting / precise
    public double p = .017d;
    public double i = .0d;
    public double d = .0d;
    
    private double pidInput=0;
    private double pidOutput=0;

   PIDSubsystem pid;


    public PIDBoom(Talon motor, RotationalEncoder rot) {
    //public PIDBoom(Talon motor, AnalogChannel rot) {
        super(motor, rot);
        pid = new BoomPIDController();
        //Boom does this in it's constructor, but without the pid 
        //  enabled that won't do anything.
        if (BOOM_ENABLED) {
            set(PID_PASS);
        }
    }

    public void init(NetworkTable server) {
        server.putNumber(SERVER_P_IN, p);
        server.putNumber(SERVER_I_IN, i);
        server.putNumber(SERVER_D_IN, d);
    }

    class BoomPIDController extends PIDSubsystem {
        
        public BoomPIDController() {
            super("Boom", p, i, d);
            enable();
        }

        protected double returnPIDInput() {
           pidInput = rot.pidGet();
           return pidInput;
       }

       protected void usePIDOutput(double output) {
           pidOutput = -1* output;
           motor.set(pidOutput);

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
     * updates the pid display fields, reads any pid values that might
     * have been entered from the dashboard and updates the pid values,
     * writes pid logging info to System.out in CSV format 
     */
    public void update(NetworkTable server) {
        updateDashboard(server);
        setPIDFromDashboardInputs(server);
    }

    void setPIDFromDashboardInputs(NetworkTable server) {
        //...update from the dashboard...
        p = server.getNumber(SERVER_P_IN);
        i = server.getNumber(SERVER_I_IN);
        d = server.getNumber(SERVER_D_IN);
    }

    void updateDashboard(NetworkTable server) {
        //send the current values back to the server...
        server.putNumber("Boom.P", p);
        server.putNumber("Boom.I", i);
        server.putNumber("Boom.D", d);
        server.putNumber("Boom.pidInput", pidInput);
        server.putNumber("Boom.pidOutput", pidOutput);
        server.putNumber("Boom.rot.pid", rot.pidGet());
        server.putNumber("Boom.rot.vlt", rot.getAverageVoltage());
    }
    
}