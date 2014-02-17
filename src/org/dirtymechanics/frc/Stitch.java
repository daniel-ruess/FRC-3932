package org.dirtymechanics.frc;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.component.drive.DriveTrain;
import org.dirtymechanics.frc.component.drive.Transmission;
import org.dirtymechanics.frc.util.List;
import org.dirtymechanics.frc.util.Updatable;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Stitch extends IterativeRobot {

    /**
     * The Joystick used to control the speed of the leftDriveMotor
     */
    private final Joystick joystickLeft;
    /**
     * The Joystick used to control the speed of the rightDriveMotor
     */
    private final Joystick joystickRight;

    /** 
     * The main compressor
     */
    private final Compressor compressor;

    /**
     * Motor to move the 3 left wheels
     */
    private final Jaguar leftDriveMotor;
    /** 
     * Motor to move the 3 right wheels
     */
    private final Jaguar rightDriveMotor;

    /**
     * The relay that ...
     */
    private final Relay transOpenSpike;
    /** 
     * The relay that ...
     */
    private final Relay transCloseSpike;
    /** 
     * The solenoid to switch the transmission
     */
    private final DoubleSolenoid transmissionSolenoid;
    
    /**
     * The actual transmission
     */
    private final Transmission transmission;
    private final DriveTrain driveTrain;

    /** 
     * List of all the updatable objects
     */
    private final List updatables;

    public Stitch() {
        joystickLeft = new Joystick(1);
        joystickRight = new Joystick(2);

        compressor = new Compressor(1, 6);

        leftDriveMotor = new Jaguar(1);
        rightDriveMotor = new Jaguar(2);

        transOpenSpike = new Relay(1);
        transCloseSpike = new Relay(2);
        transmissionSolenoid = new DoubleSolenoid(transOpenSpike, transCloseSpike);

        transmission = new Transmission(transmissionSolenoid);
        driveTrain = new DriveTrain(leftDriveMotor, leftDriveMotor, transmission);
        
        
        updatables = new List();
        updatables.put(transmissionSolenoid);
    }

    /**
     * Called per first initialization of the robot.
     */
    public void robotInit() {
        // Initiat the compressor for the pneumatic systems
        compressor.start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        // Set the motor speeds acording to the joyStick positions
        driveTrain.setLeftSpeed(joystickLeft.getY() * -1);
        driveTrain.setRightSpeed(joystickRight.getY() * -1);
        update();
    }

    /**
     * This function is used to update all the updatable objects
     */
    private void update() {
        Object[] o = updatables.getObjects();
        for (int i = 0; i < o.length; ++i) {
            Updatable ud = (Updatable) o[i];
            ud.update();
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }
}
