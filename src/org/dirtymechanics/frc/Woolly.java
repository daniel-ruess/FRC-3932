package org.dirtymechanics.frc;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
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
    private final Joystick driverLeftJoy;
    /**
     * The physical right joystick.
     */
    private final Joystick driverRightJoy;
    /**
     * The operator's controller.
     */
    private final Joystick operatorController;
    /**
     * The operator's joystick.
     */
    private final Joystick operatorJoy;
    /**
     * The compressor's controller.
     */
    private final Compressor compressor;
    /**
     * Jaguar that's driving the first left motor.
     */
    private final Jaguar leftDriveMotorA;
    /**
     * Jaguar that's driving the second left motor.
     */
    private final Jaguar leftDriveMotorB;
    /**
     * Jaguar that's driving the first right motor.
     */
    private final Jaguar rightDriveMotorA;
    /**
     * Jaguar that's driving the second right motor.
     */
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
    private final AnalogChannel ultrasonicSensor;

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
    private final Solenoid cameraLEDA;
    private final Solenoid cameraLEDB;
    private final Solenoid signalLEDA;
    private final Solenoid signalLEDB;
    private final DoubleSolenoid rollerSolenoid;
    private final Transmission transmission;
    private boolean firing;
    private long fireTime;
    private long octoTime;
    private boolean octoSwitchOpen; 
    NetworkTable server = NetworkTable.getTable("SmartDashboard");
    double imageMatchConfidence = 0.0;
    private int mode = 0, lastMode = -1, firingMode = 1, lastFiringMode = -1;

    private final int[] toggle = new int[30];
    private final boolean[] released = new boolean[30];

    int counter = 0;
    private long autoStart;
    

    public Woolly() {
        driverLeftJoy = new Joystick(1);
        driverRightJoy = new Joystick(2);
        operatorController = new Joystick(3);
        operatorJoy = new Joystick(4);

        buttonMap = new ButtonMap(driverLeftJoy, driverRightJoy, operatorController);

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

        cameraLEDA = new Solenoid(2, 7);
        cameraLEDB = new Solenoid(2, 8);
        signalLEDA = new Solenoid(2, 5);
        signalLEDB = new Solenoid(2, 6);

        stringEncoder = new StringEncoder(1);
        rotEncoder = new RotationalEncoder(2);
        ultrasonicSensor = new AnalogChannel(3);
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


    public void autonomousInit() {
        autoStart = System.currentTimeMillis();
        cameraLEDA.set(true);
        cameraLEDB.set(true);
    }
    
    /**
     * This function is called periodically during autonomous.
     */
    public void autonomousPeriodic() {
        driveForwardUntil3rdSecondOfAutonomous();
        //autonomousExperimintalShooter(time);
        //autonomousExperimentalHotGoalShot();
    }

    void driveForwardUntil3rdSecondOfAutonomous() {
        long time = System.currentTimeMillis() - autoStart;
        if (time < 3000) {
            driveTrain.setSpeed(-.73, .75);
        } else {
            driveTrain.setSpeed(0, 0);
        }
        
    }

    //Experimental, not currently called.
    void autonomousExperimentalHotGoalShot() {
        //HOT_CONFIDENCE = server.getNumber("HOT_Confidence", 0.0);
        // Algorithm one: One ball, hot vision
        /*
        if (HOT_CONFIDENCE > 75.0) {
        //shoot
        }
        else {
        //wait a few seconds
        }
        */
        //Algorithm two
        long dif = System.currentTimeMillis() - autoStart;
        if (dif > 0 && dif < 2000) {
            driveTrain.setSpeed(-1, 1);
        } else {
            //if (dif > 2000 && dif < 2300) {
            driveTrain.setSpeed(0, 0);
        }/*
        if (dif > 2300) {
        roller.openArm();
        }
        if (dif > 2500) {
        shooter.fire();
        }
        update();*/
    }
    
    //TODO extract class PeriodicAutonomousShooter, this method would be "update"
    //  members would be appropriate controls passed in constructor.
    //Experimental, not currently called.
    void autonomousExperimintalShooter(long time) {
        if (time < 750) {
            boom.set(Boom.AUTO);
            screwDrive.set(ScrewDrive.AUTO);
            roller.forward();
        } else if (octo.get() && time < 1000) {
            roller.forward();
        } else {
            roller.stop();
        }

        if (time < 3500) {
            driveTrain.setSpeed(-.73, .75);
        } else if (time > 3700) {
            grabLargeSolenoid.set(false);
            grabSmallSolenoid.set(false);
            roller.openArm();
            roller.stop();
            if (time > 4200) {
                resetFireControls();
            } else if (time > 4050) {
                shooter.fire();
            }
        } else {

            driveTrain.setSpeed(0, 0);
        }
        update();

        //System.out.println(server.getNumber("HOT_CONFIDENCE", 0.0));
        System.out.println(ultrasonicSensor.getAverageVoltage());
        imageMatchConfidence = server.getNumber("HOT_CONFIDENCE", 0.0);
        if (System.currentTimeMillis() - autoStart < 3000) {
            driveTrain.setSpeed(-.43, .5);
        } else {
            driveTrain.setSpeed(0, 0);
            if (imageMatchConfidence > 50 || time > 6000) {
                roller.openArm();
                if (!firing) {
                    firing = true;
                    fireTime = System.currentTimeMillis();
                }
                if (time > 6300 || System.currentTimeMillis() - fireTime > 300) {
                    shooter.fire();
                    firing = false;
                }
            }
        }
        transmissionSolenoid.set(false);
        screwDrive.set(ScrewDrive.PASS);

        update();
    }

    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {
        if (counter++ % 20 == 0) {
            debugSensors();
            turnOffLEDs();
        }

        driveTrain.setSpeed(buttonMap.getDriveLeft(), buttonMap.getDriveRight());
        setTransmission(buttonMap.isTransmissionHigh());
        updateOcto();

        if (firing) {
            prepareToFire();
            if (firingCompleted()) {
                resetFireControls();
            } else if (System.currentTimeMillis() - fireTime > 350) {
                shooter.fire();
            }
        }
        updateScrewDrive();
        updateBoom();

        if (holdingTheBallAndNotFiring()) {
            if (operatorController.getRawButton(8)) {
                if (released[8]) {
                    if (toggle[8]++ % 2 == 0) {
                        grabLargeSolenoid.set(true);
                    } else {
                        grabLargeSolenoid.set(false);
                    }
                    released[8] = false;
                }
            } else {
                released[8] = true;
            }

            if (operatorController.getRawButton(5)) {
                if (released[5]) {
                    if (toggle[5]++ % 2 == 0) {
                        roller.openArm();
                    } else {
                        roller.closeArm();
                    }
                    released[5] = false;
                }
            } else {
                released[5] = true;
            }

            if (operatorController.getRawButton(10)) {
                if (released[10]) {
                    if (toggle[10]++ % 2 == 0) {
                        if (toggle[9] % 2 != 0) {
                            toggle[9]++;
                        }
                        roller.forward();
                    } else {
                        roller.stop();
                    }
                    released[10] = false;
                }
            } else {
                released[10] = true;
            }

            if (operatorController.getRawButton(9)) {
                if (released[9]) {
                    if (toggle[9]++ % 2 == 0) {
                        if (toggle[10] % 2 != 0) {
                            toggle[10]++;
                        }
                        roller.reverse();
                    } else {
                        roller.stop();
                    }
                    released[9] = false;
                }
            } else {
                released[9] = true;
            }

            if (operatorController.getRawButton(6)) {
                startFiringSequence();
            } else {
                released[6] = true;
            }
        }

        update();
    }

    void startFiringSequence() {
        if (released[6]) {
            if (operatorController.getRawButton(11) || !octo.get()) {
                released[6] = false;
                firing = true;
                fireTime = System.currentTimeMillis();
            }
        }
    }

    boolean holdingTheBallAndNotFiring() {
        return !octoSwitchOpen && !firing;
    }

    void updateBoom() {
        if (operatorJoy.getRawButton(6)) {
            if (released[21]) {
                boom.increaseOffset();
                released[21] = false;
            }
        } else if (operatorJoy.getRawButton(4)) {
            if (released[21]) {
                boom.decreaseOffset();
                released[21] = false;
            }
        } else {
            released[21] = true;
        }
        
        if (operatorController.getRawButton(4)) {
            boom.set(Boom.TRUSS_SHOT);
        } else if (operatorController.getRawButton(2)) {
            boom.set(Boom.PASS);
        }
    }

    void updateScrewDrive() {
        if (operatorController.getRawAxis(5) < -.5) {
            screwDrive.set(ScrewDrive.RESET);
        } else if (operatorController.getRawAxis(6) > .5) {
            screwDrive.set(ScrewDrive.PASS);
        } else if (operatorController.getRawAxis(6) < -.5) {
            screwDrive.set(ScrewDrive.TRUSS_SHOT);
        }
        
        if (operatorJoy.getRawAxis(6) < -.5) {
            if (released[20]) {
                screwDrive.increaseOffset();
                released[20] = false;
            }
            released[20] = false;
        } else if (operatorJoy.getRawAxis(6) > .5) {
            if (released[20]) {
                screwDrive.decreaseOffset();
                released[20] = false;
            }
        } else {
            released[20] = true;
        }
    }

    void prepareToFire() {
        disableToggles();
        grabLargeSolenoid.set(false);
        grabSmallSolenoid.set(false);
        roller.openArm();
        roller.stop();
    }

    void resetFireControls() {
        grabLargeSolenoid.set(false);
        grabSmallSolenoid.set(false);
        roller.closeArm();
        roller.stop();
        firing = false;
    }

    boolean firingCompleted() {
        return System.currentTimeMillis() - fireTime > 500;
    }

    void updateOcto() {
        if (!octo.get()) {
            if (!octoSwitchOpen) {
                octoSwitchOpen = true;
                octoTime = System.currentTimeMillis();
            }
        }
        
        if (octoSwitchOpen) {
            if (System.currentTimeMillis() - octoTime > 200) {
                if (System.currentTimeMillis() - octoTime > 500) {
                    roller.closeArm();
                    octoSwitchOpen = false;
                }
                grabLargeSolenoid.set(false);
                grabSmallSolenoid.set(false);
                roller.stop();
                disableToggles();
            }
        }
    }

    void setTransmission(boolean transmissionHigh) {
        if (transmissionHigh) {
            transmission.setHigh();
        } else {
            transmission.setLow();
        }
    }

    void turnOffLEDs() {
        cameraLEDA.set(false);
        cameraLEDB.set(false);
    }

    void debugSensors() {
        System.out.println("ROT: " + rotEncoder.getAverageVoltage());
        System.out.println("LIN: " + stringEncoder.getAverageVoltage());
        System.out.println("ULT: " + ultrasonicSensor.getAverageVoltage());
        System.out.println("OCT: " + octo.get());
    }

    private void disableToggles() {

        if (toggle[5] % 2 == 0) { //disable all roller/grabber toggles
            toggle[5]++;
            released[5] = true;
        }
        if (toggle[7] % 2 == 0) {
            toggle[7]++;
            released[7] = true;
        }
        if (toggle[8] % 2 == 0) {
            toggle[8]++;
            released[8] = true;
        }
        if (toggle[9] % 2 == 0) {
            toggle[9]++;
            released[9] = true;
        }
        if (toggle[10] % 2 == 0) {
            toggle[10]++;
            released[10] = true;
        }
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

    public void teleopInit() {
        cameraLEDA.set(true);
        cameraLEDB.set(true);
    }

    



    

    
}
