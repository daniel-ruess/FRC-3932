package org.dirtymechanics.frc;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.component.arm.Boom;
import org.dirtymechanics.frc.component.arm.Shooter;
import org.dirtymechanics.frc.component.arm.Grabber;
import org.dirtymechanics.frc.component.arm.Roller;
import org.dirtymechanics.frc.component.arm.ScrewDrive;
import org.dirtymechanics.frc.component.drive.DriveTrain;
import org.dirtymechanics.frc.component.drive.Transmission;
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
public class Woolly extends IterativeRobot {

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
    private final Joystick joystickController;
    /**
     * The compressor's controller.
     */
    private final Compressor compressor;
    /**
     * Jaguar that's driving the left two motors.
     */
    private final Jaguar leftDriveMotorA;
    private final Jaguar leftDriveMotorB;
    /**
     * Jaguar that's driving the right two motors.
     */
    private final Jaguar rightDriveMotorA;
    private final Jaguar rightDriveMotorB;
    /**
     * Jaguar controlling the screw drive.
     */
    private final Jaguar screwMotor;
    /**
     * Jaguar controlling the boom.
     */
    private final Talon boomMotor;
    /**
     * Jaguar controller the grabber's roller.
     */
    private final Relay rollerMotor;
    /**
     * The spike controlling the transmission open valve.
     */
    private final Solenoid transOpen;
    /**
     * The spike controlling the transmission close valve.
     */
    private final Solenoid transClose;
    /**
     * The spike controlling the open valve for the grabber.
     */
    private final Solenoid grabSmallOpen;
    /**
     * The spike controlling the close valve for the grabber.
     */
    private final Solenoid grabSmallClose;
    /**
     * The solenoid to switch the transmission.
     */
    private final DoubleSolenoid transmissionSolenoid;
    /**
     * The solenoid controlling the grabber.
     */
    private final DoubleSolenoid grabSmallSolenoid;
    /**
     * The string encoder used for the screw drive.
     */
    private final StringEncoder stringEncoder;
    /**
     * The rotational encoder used for the boom.
     */
    private final RotationalEncoder rotEncoder;

    private final DigitalInput octo;
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
    private final Solenoid firingOpen;
    private final Solenoid firingClose;
    private final DoubleSolenoid firingSolenoid;
    private final Grabber grabber;
    private final ScrewDrive screwDrive;
    private final Shooter shooter;
    private final Boom boom;
    private final Solenoid grabLargeOpen;
    private final Solenoid grabLargeClose;
    private final DoubleSolenoid grabLargeSolenoid;
    private final Solenoid rollerOpen;
    private final Solenoid rollerClose;
    private final Solenoid lightSolenoid;
    private final DoubleSolenoid rollerSolenoid;
    private final Transmission transmission;
    private long catchTime;

    public Woolly() {
        joystickLeft = new Joystick(1);
        joystickRight = new Joystick(2);
        joystickController = new Joystick(3);

        buttonMap = new ButtonMap(joystickLeft, joystickRight, joystickController);

        compressor = new Compressor(1, 1);

        leftDriveMotorA = new Jaguar(1);
        leftDriveMotorB = new Jaguar(2);
        rightDriveMotorB = new Jaguar(3);
        rightDriveMotorA = new Jaguar(4);
        boomMotor = new Talon(5);
        screwMotor = new Jaguar(6);
        rollerMotor = new Relay(2);

        transOpen = new Solenoid(1, 7);
        transClose = new Solenoid(1, 8);
        transmissionSolenoid = new DoubleSolenoid(transOpen, transClose);

        grabSmallOpen = new Solenoid(1, 1);
        grabSmallClose = new Solenoid(1, 2);
        grabSmallSolenoid = new DoubleSolenoid(grabSmallOpen, grabSmallClose);

        grabLargeOpen = new Solenoid(1, 5);
        grabLargeClose = new Solenoid(1, 6);
        grabLargeSolenoid = new DoubleSolenoid(grabLargeOpen, grabLargeClose);

        firingOpen = new Solenoid(2, 1);
        firingClose = new Solenoid(2, 2);
        firingSolenoid = new DoubleSolenoid(firingOpen, firingClose);

        rollerOpen = new Solenoid(1, 3);
        rollerClose = new Solenoid(1, 4);
        rollerSolenoid = new DoubleSolenoid(rollerOpen, rollerClose);

        lightSolenoid = new Solenoid(2, 8);

        stringEncoder = new StringEncoder(1);
        rotEncoder = new RotationalEncoder(2);
        octo = new DigitalInput(2);

        driveTrain = new DriveTrain(leftDriveMotorA, leftDriveMotorB, rightDriveMotorA, rightDriveMotorB);
        transmission = new Transmission(transmissionSolenoid);
        roller = new Roller(rollerMotor, rollerSolenoid);
        grabber = new Grabber(grabSmallSolenoid);
        screwDrive = new ScrewDrive(screwMotor, stringEncoder);
        shooter = new Shooter(screwDrive, firingSolenoid);
        boom = new Boom(boomMotor, rotEncoder);

        updatables = new List();
        updatables.put(transmissionSolenoid);
        updatables.put(grabSmallSolenoid);
        updatables.put(grabLargeSolenoid);
        updatables.put(firingSolenoid);
        updatables.put(rollerSolenoid);
        updatables.put(shooter);
        updatables.put(boom);
        updatables.put(screwDrive);
    }

    /**
     * Called per first initialization of the robot.
     */
    public void robotInit() {
        // Initiate the compressor for the pneumatic systems
        compressor.start();
    }

    private int mode = 0, lastMode = -1, firingMode = 1, lastFiringMode = -1;

    private boolean pressed = true, aRelease = false;

    boolean released = true;

    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {
        driveTrain.setSpeed(buttonMap.getDriveLeft(), buttonMap.getDriveRight());

        if (buttonMap.isTransmissionHigh()) {
            transmission.setHigh();
        } else {
            transmission.setLow();
        }

        if (joystickController.getRawButton(4)) { // high 5
            mode = 2;
        } else if (joystickController.getRawButton(2)) { // high 9
            mode = 3;
        } else if (joystickController.getRawButton(1)) { // start
            mode = 0;
        } else if (joystickController.getRawButton(3)) { // gather
            mode = 1;
        } else if (joystickController.getRawButton(5)) { // close
            mode = 4;
        }

        if (joystickController.getRawButton(8)) { // catch
            if ((mode == 5 || mode == 10) && released) {
                mode = 10;
            } else {
                mode = 5;
                released = false;
            }
        } else {
            released = true;
        }

        if (joystickController.getRawButton(7)) {
            roller.reverse();
        }

        //if (mode != lastMode) {
        switch (mode) {
            case 0: // start
                boom.set(Boom.START);
                shooter.set(ScrewDrive.HIGH_9);
                grabSmallSolenoid.set(false);
                grabLargeSolenoid.set(false);
                roller.closeArm();
                roller.stop();
                break;
            case 1: // gather
                boom.set(Boom.GATHERING);
                shooter.set(ScrewDrive.HIGH_9);
                grabSmallSolenoid.set(true);
                grabLargeSolenoid.set(false);
                roller.closeArm();
                roller.forward();
                if (!octo.get()) {
                    mode = 1;
                }
                break;
            case 4: // gather (off)
                boom.set(Boom.GATHERING);
                shooter.set(ScrewDrive.HIGH_9);
                grabSmallSolenoid.set(false);
                grabLargeSolenoid.set(false);
                roller.closeArm();
                roller.stop();
                break;
            case 2: // high 5
                boom.set(Boom.HIGH_5);
                shooter.set(ScrewDrive.HIGH_5);
                grabSmallSolenoid.set(false);
                grabLargeSolenoid.set(false);
                roller.closeArm();
                roller.stop();
                break;
            case 3: // high 9
                boom.set(Boom.HIGH_9);
                shooter.set(ScrewDrive.HIGH_9);
                grabSmallSolenoid.set(false);
                grabLargeSolenoid.set(false);
                roller.closeArm();
                roller.stop();
                break;
            case 5: // catching
                boom.set(Boom.HIGH_5);
                shooter.set(ScrewDrive.HIGH_5);
                grabSmallSolenoid.set(false);
                grabLargeSolenoid.set(true);
                roller.openArm();
                roller.stop();
                if (!octo.get()) {
                    catchTime = System.currentTimeMillis();
                    mode = 10;
                }
                break;
            case 10: // catch
                grabSmallSolenoid.set(false);
                grabLargeSolenoid.set(false);
                roller.stop();
                if (System.currentTimeMillis() - catchTime > 500) {
                    roller.closeArm();
                }
                break;
        }
        //}
        lastMode = mode;

        SmartDashboard.putNumber("Rot: ", rotEncoder.getVoltage());
        SmartDashboard.putNumber("Rot a: ", rotEncoder.getAverageVoltage());
        SmartDashboard.putNumber("Dist: ", stringEncoder.getVoltage());
        SmartDashboard.putNumber("Dist: a", stringEncoder.getVoltage());
        SmartDashboard.putNumber("Mode: ", mode);
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
        // TODO: Autonomous
    }
}
