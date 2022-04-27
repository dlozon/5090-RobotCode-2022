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
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Candy;


// Misc imports
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.misc_subclasses.Dashboard;




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
  private Drivetrain drivetrain;
  private Dashboard dashboard;
  private Candy candy;


  // Misc variables/objects
  private DifferentialDrive robotDrive;
  private Compressor comp;
  private Candy candyMotor;
  private GenericPID candyPID;
  
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

    dashboard = new Dashboard();

    candyMotor = new Candy(17);
    candyPID = new GenericPID(candy.getMotor(), ControlType.kPosition, .04);
  }

  // This function is called once at the start of auton
  @Override
  public void autonomousInit() {
  }

  // This function is called every 20ms during auton
  @Override
  public void autonomousPeriodic() { 
  }

  // This function is called every 20ms during teleop
  @Override
  public void teleopPeriodic() {
    // Puts the robot in arcade drive
    robotDrive.arcadeDrive(-joystick.getRawAxis(0), joystick.getRawAxis(1));

    if (joystick.getTrigger() && candyMotor.getPosition() < 40) {
      candyPID.activate(12);
    }
  }

  // This function is called every 20ms while the robot is enabled
  @Override
  public void robotPeriodic() {

  }
}