package frc.robot.subsystems;

// Imports
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Intake {
  // Variable Declaration
  private DoubleSolenoid dubs;
  CANSparkMax motor;

  // Is true if the arms are up 
  // Motor should NOT move if this is true
  private boolean solenoidIsUp;

  // Constructor
  public Intake(int motorID) {
    motor = new CANSparkMax(motorID, MotorType.kBrushless);
  }

  // Accessor methods
  public boolean armsAreUp() { return this.solenoidIsUp; }
  public int getMotorRPM() { return (int) Math.round(this.motor.getEncoder().getVelocity()); }
  public double getRPM() { return motor.getEncoder().getVelocity(); }
  public double getPosition() { return motor.getEncoder().getPosition(); }
  public CANSparkMax getMotor() { return motor; }

  // Put arms up
  public void up() { dubs.set(DoubleSolenoid.Value.kForward); }

  // Put arms down
  public void down() { dubs.set(DoubleSolenoid.Value.kReverse); }

  // Set the motor to specified power
  public void set(double power) { if(!solenoidIsUp) motor.set(power); }

  // Turn motor off
  public void motorOff() { motor.set(0); }

  // If arms are up, they will move down
  // If arms are down they will move up
  public void toggleArms() {
    if(solenoidIsUp){
      dubs.set(DoubleSolenoid.Value.kReverse);
      solenoidIsUp = false;
    }
    else {
      dubs.set(DoubleSolenoid.Value.kForward);
      solenoidIsUp = true;
    }
  }
}
