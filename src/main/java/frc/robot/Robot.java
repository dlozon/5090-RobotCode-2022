package frc.robot;

// Controller Imports
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Joystick;

// Actuation imports (Motors, Compressors, etc.)
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.wrappers.GenericPID;
import com.revrobotics.CANSparkMax.ControlType;

// Camera imports
import edu.wpi.first.cameraserver.CameraServer;

// Subsystem imports
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Climber;

// Misc imports
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.misc_subclasses.Dashboard;
import frc.robot.misc_subclasses.Limelight;
import static frc.robot.Constants.*;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // Controller ojects
  private Joystick joystick; 
  private XboxController xbox;

  // Subsystem objects
  private Shooter shooter;
  private Drivetrain drivetrain;
  private Dashboard dashboard;
  private Limelight limelight;
  private Elevator elevator; 
  private Intake intake;
  private Climber climber;
  private Turret turret;
  private Hood hood;

  // Misc variables/objects
  private DifferentialDrive robotDrive;
  private Compressor comp;
  private GenericPID turretPID;
  private GenericPID shooterPID;
  private GenericPID hoodPID;
  private GenericPID leftclimberPID;
  private GenericPID rightclimberPID;
  private double autonStartTime;
  
  // This function is run when the robot is first started up and should be used
  // for any initialization code.
  @Override
  public void robotInit() {
    // Initialize variables
    joystick = new Joystick(0);
    xbox  = new XboxController(1);
  
    drivetrain = new Drivetrain(7, 3, 6, 2);
    robotDrive = new DifferentialDrive(
      drivetrain.getLeftMotorGroup(), drivetrain.getRightMotorGroup());

    CameraServer.startAutomaticCapture();
    limelight = new Limelight();

    turret = new Turret(14);
    turretPID = new GenericPID(turret.getMotor(), ControlType.kPosition, .25);
    turretPID.setInputRange(-75 * TURRET_RATIO, 75 * TURRET_RATIO);


    shooter = new Shooter(9, 5);    
    shooterPID = new GenericPID(shooter.getLeaderMotor(), ControlType.kVelocity, .00022, .0000005, 0);
    shooterPID.setOutputRange(-1,1);

    hood = new Hood(15);
    hoodPID = new GenericPID(hood.getMotor(), ControlType.kPosition, .25);
    hoodPID.setInputRange(0, 65);

    elevator = new Elevator(13, 0, 1);

    intake = new Intake(10);
    comp = new Compressor(0, PneumaticsModuleType.CTREPCM);

    climber = new Climber(11, 12, 2, 3);
    leftclimberPID = new GenericPID(climber.getleftMotor(), ControlType.kPosition, .25);
    leftclimberPID.setInputRange(-240, 0);
    rightclimberPID = new GenericPID(climber.getrightMotor(), ControlType.kPosition, .25);
    leftclimberPID.setInputRange(-240, 0);

    dashboard = new Dashboard();
  }

  // This function is called once at the start of auton
  @Override
  public void autonomousInit() {
    autonStartTime = Timer.getFPGATimestamp();
  }

  // This function is called every 20ms during auton
  @Override
  public void autonomousPeriodic() { 
    double currentTime = Timer.getFPGATimestamp() - autonStartTime;

    if (currentTime > 0 && currentTime < 1.5) {
      drivetrain.getLeftMotorGroup().set(0);
      drivetrain.getRightMotorGroup().set(0);
    }

    if (currentTime > .45 && currentTime < 1.5) {
      intake.down();
    }

    if (currentTime > 1.5 && currentTime < 5.20) {
      intake.set(1);
    }

    if (currentTime > 1.5 && currentTime < 3) {
      drivetrain.getLeftMotorGroup().set(-0.4);
      drivetrain.getRightMotorGroup().set(0.4);
    }

    if (currentTime > 3 && currentTime < 5.20) {
      drivetrain.getLeftMotorGroup().set(0);
      drivetrain.getRightMotorGroup().set(0);
      intake.set(0);
      intake.up();
    }

    if (currentTime > 5.20 && currentTime < 6) {
      drivetrain.getLeftMotorGroup().set(.4);
      drivetrain.getRightMotorGroup().set(.4);
    }

    if (currentTime == 6) {
      drivetrain.getLeftMotorGroup().set(0);
      drivetrain.getRightMotorGroup().set(0);
    }

    if (currentTime > 6 && currentTime < 7) {
      turretPID.activate(
        ((turret.getPosition() / TURRET_RATIO) - limelight.getRotationAngle()) * TURRET_RATIO );
    }

    if (currentTime > 7 && currentTime < 10) {
      hoodPID.activate((.000002262119 * Math.pow(limelight.getDistance(), 4)) - (.000654706898 * Math.pow(limelight.getDistance(), 3)) + (.060942569498 * Math.pow(limelight.getDistance(), 2)) - (1.23311704654 * limelight.getDistance()) - .962075155165);
      shooterPID.activate(.056650444657 * Math.pow(limelight.getDistance(), 2) + 8.50119265165 * limelight.getDistance() + 2383.56516106);
    }

    if (currentTime > 8 && currentTime < 10) {
      elevator.shoot();
    }

    if (currentTime == 11) {
      shooter.off();
      elevator.off(); 
    }
  }
  
  // This function is called once at the start of teleop
  @Override
  public void teleopInit() {
    turretPID.setSetpoint(0);
    
    hoodPID.setSetpoint(0);

    comp.enableDigital();
  }

  // This function is called every 20ms during teleop
  @Override
  public void teleopPeriodic() {
    // Puts the robot in arcade drive
    robotDrive.arcadeDrive(-joystick.getRawAxis(0), joystick.getRawAxis(1));

    // Joystick trigger activates motor
    if(joystick.getTrigger())
      intake.set(1);
    else if(joystick.getRawButton(4))
      intake.set(-1);
    else
      intake.motorOff();

    // Manually control the turret with bumpers
    if(xbox.getLeftBumper()) {
      turretPID.pause();
      turret.setPower(.8);
    }
    else if(xbox.getRightBumper()) {
      turretPID.pause();
      turret.setPower(-.8);
    }
    else if(turretPID.getP() == 0)
      turret.off();

    // Dpad controls
    switch(xbox.getPOV()){
      case 0: // UP
        leftclimberPID.activate(-236);
        rightclimberPID.activate(-238);
        break;
      case 180: // DOWN
        leftclimberPID.activate(0);
        rightclimberPID.activate(0);
        break;
      case 90: // RIGHT
        shooter.increasePowerBy(.004);
        break;
      case 270: // LEFT
        shooter.decreasePowerBy(.004);
        break;
      default: // NOT PRESSED
        // Right trigger pushes a ball into the shooter
        if(xbox.getRightTriggerAxis() > 0)
          elevator.shoot();
        else if(xbox.getAButton())
          elevator.lift();
        else
          elevator.auto();
    }
    
    
    if(xbox.getLeftTriggerAxis() > 0) {
      
      hoodPID.activate();
      
      turretPID.activate();

      shooterPID.activate();
    }
      
    // Left stick Y-axis controls left climber arm
    if(Math.abs(xbox.getLeftY()) > CLIMBER_DEADZONE ) {
      climber.setLeft(xbox.getLeftY());
      leftclimberPID.pause();
    }
    else if(leftclimberPID.getP() == 0)
      climber.leftOff();
      

    // Right stick Y-axis controls left climber arm
    if(Math.abs(xbox.getRightY()) > CLIMBER_DEADZONE ) {
      climber.setRight(xbox.getRightY());
      rightclimberPID.pause();
    }
    else if (leftclimberPID.getP() == 0)
      climber.rightOff();

    // X button lowers intake
    if(xbox.getXButton())
      intake.down();
    // Y button raises intake
    else if(xbox.getYButton())
      intake.up();

    // joystick controls intake state
    if(joystick.getRawButton(2))
      intake.down();
    else if (joystick.getRawButton(3))
      intake.up();

    // Get ready to climb!
    if(xbox.getStartButton()) {
      hoodPID.activate(0);
      turretPID.activate(0);
      shooterPID.activate(0);
      intake.up();
    }
    
    // preset motor value to shoot ball
    if(xbox.getLeftStickButton()) {
      shooterPID.activate(3100);
    }

    // preset motor value to shoot ball at low speed (reject ball)
    if(xbox.getRightStickButton()) {
      shooterPID.activate(1700);
    }


    // B is essentially an e-stop
    if(xbox.getBButton()){
      shooter.off();
      elevator.off();
      climber.off();
      turret.off();
      hood.off();
      turretPID.pause();
    }
  }

  // This function is called every 20ms while the robot is enabled
  @Override
  public void robotPeriodic() {
    // Update subclass internal values
    shooter.updateCurrentPower();
    elevator.update();
    limelight.updateLimelightTracking();

    // Update dashboard
    dashboard.PIDtoDashboard(shooterPID, "Shooter");
    dashboard.PIDtoDashboard(turretPID, "Turret");
    dashboard.PIDtoDashboard(hoodPID, "Hood");
    dashboard.printElevatorStorage(elevator);
    dashboard.printTurretDegrees(turret);
    dashboard.printHoodDegrees(hood);
    dashboard.printLeftClimberPosition(climber);
    dashboard.printRightClimberPosition(climber);
    dashboard.printLimelightData(limelight);
  }
}