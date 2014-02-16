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

    private final Joystick joystickLeft;
    private final Joystick joystickRight;

    private final Compressor compressor;

    private final Jaguar leftDriveMotor;
    private final Jaguar rightDriveMotor;

    private final Relay transOpenSpike;
    private final Relay transCloseSpike;
    private final DoubleSolenoid transmissionSolenoid;
    
    private final Transmission transmission;
    private final DriveTrain driveTrain;

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
        compressor.start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        driveTrain.setLeftSpeed(joystickLeft.getY() * -1);
        driveTrain.setRightSpeed(joystickRight.getY() * -1);
        update();
    }

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
