package org.dirtymechanics.frc;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.component.arm.Boom;
import org.dirtymechanics.frc.component.arm.FiringMechanism;
import org.dirtymechanics.frc.component.arm.Grabber;
import org.dirtymechanics.frc.component.arm.Roller;
import org.dirtymechanics.frc.component.arm.ScrewDrive;
import org.dirtymechanics.frc.component.drive.DriveTrain;
import org.dirtymechanics.frc.control.ButtonMap;
import org.dirtymechanics.frc.sensor.RotationalEncoder;
import org.dirtymechanics.frc.sensor.StringEncoder;
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
     * The physical left joystick.
     */
    private final Joystick joystickLeft;
    /**
     * The physical right joystick.
     */
    private final Joystick joystickRight;
    /**
     * The physical controller.
     */
    private final Joystick joystickCont;
    /**
     * The compressor's controller.
     */
    private final Compressor compressor;
    /**
     * Jaguar that's driving the left two motors.
     */
    private final Jaguar leftDriveMotor;
    /**
     * Jaguar that's driving the right two motors.
     */
    private final Jaguar rightDriveMotor;
    /**
     * Jaguar controlling the screw drive.
     */
    private final Jaguar screwMotor;
    /**
     * Jaguar controlling the boom.
     */
    private final Jaguar boomMotor;
    /**
     * Jaguar controller the grabber's roller.
     */
    private final Jaguar rollerMotor;
    /**
     * The spike controlling the transmission open valve.
     */
    private final Relay transOpenSpike;
    /**
     * The spike controlling the transmission close valve.
     */
    private final Relay transCloseSpike;
    private final Relay grabberOpenSpike;
    private final Relay grabberCloseSpike;
    /**
     * The solenoid to switch the transmission
     */
    private final DoubleSolenoid transmissionSolenoid;
    private final DoubleSolenoid grabberSolenoid;
    /**
     * The string encoder used for the screw drive.
     */
    private final StringEncoder stringEncoder;
    /**
     * The rotational encoder used for the boom.
     */
    private final RotationalEncoder rotEncoder;
    /**
     * The button map used for controllers.
     */
    private final ButtonMap buttonMap;
    /**
     * The object controlling the drive train.
     */
    private final DriveTrain driveTrain;
    /**
     * List of all the updatable objects.
     */
    private final List updatables;
    private final Roller roller;
    private final Relay firingOpenSpike;
    private final Relay firingCloseSpike;
    private final DoubleSolenoid firingSolenoid;
    private final Grabber grabber;
    private final ScrewDrive screwDrive;
    private final FiringMechanism firing;
    private final Boom boom;

    public Stitch() {
        joystickLeft = new Joystick(1);
        joystickRight = new Joystick(2);
        joystickCont = new Joystick(3);

        buttonMap = new ButtonMap(joystickLeft, joystickRight, joystickCont);

        compressor = new Compressor(1, 6);

        leftDriveMotor = new Jaguar(1);
        rightDriveMotor = new Jaguar(2);
        screwMotor = new Jaguar(3);
        boomMotor = new Jaguar(4);
        rollerMotor = new Jaguar(5);

        transOpenSpike = new Relay(1);
        transCloseSpike = new Relay(2);
        transmissionSolenoid = new DoubleSolenoid(transOpenSpike, transCloseSpike);

        grabberOpenSpike = new Relay(3);
        grabberCloseSpike = new Relay(4);
        grabberSolenoid = new DoubleSolenoid(grabberOpenSpike, grabberCloseSpike);

        firingOpenSpike = new Relay(5);
        firingCloseSpike = new Relay(6);
        firingSolenoid = new DoubleSolenoid(firingOpenSpike, firingCloseSpike);

        stringEncoder = new StringEncoder(1);
        rotEncoder = new RotationalEncoder(2);

        driveTrain = new DriveTrain(leftDriveMotor, rightDriveMotor, transmissionSolenoid);
        roller = new Roller(rollerMotor);
        grabber = new Grabber(grabberSolenoid);
        screwDrive = new ScrewDrive(screwMotor, stringEncoder);
        firing = new FiringMechanism(screwDrive, firingSolenoid);
        boom = new Boom(boomMotor, rotEncoder);

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
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {
        // Set the motor speeds acording to the joyStick positions
        driveTrain.setLeftSpeed(buttonMap.getDriveLeft());
        driveTrain.setRightSpeed(buttonMap.getDriveLeft());
        driveTrain.setTransmissionGear(buttonMap.getTransmissionGear());

        switch (buttonMap.getMode()) {
            case 0: //idle
                boom.set(Boom.RESTING);
                grabber.close();
                roller.set(false);
                break;
            case 1: //firing
                boom.set(Boom.FIRING);
                grabber.close();
                roller.set(false);
                break;
            case 2: //gathering
                boom.set(Boom.GATHERING);
                grabber.open();
                roller.set(true);
                break;
        }
        update();
    }

    /**
     * This function is used to update all the updatable objects.
     */
    private void update() {
        Object[] o = updatables.getObjects();
        for (int i = 0; i < o.length; ++i) {
            Updatable ud = (Updatable) o[i];
            ud.update();
        }
    }

    /**
     * This function is called periodically during autonomous.
     */
    public void autonomousPeriodic() {

    }
}
