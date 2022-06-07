package frc.robot.subsystems;

// Imports
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// Controls the turret system
public class Turret {
    CANSparkMax motor;

    // Constructor
    public Turret(int motorID) {
        motor = new CANSparkMax(motorID, MotorType.kBrushless);
    }

    // Accessors
    public double getRPM() { return motor.getEncoder().getVelocity(); }
    public double getPosition() { return motor.getEncoder().getPosition(); }
    public CANSparkMax getMotor() { return motor; }

    // Sets motor to specified power
    //You Lost! Haha Loser
    public void setPower(double pwr) { motor.set(pwr); }

    // Stops the turret
    public void off() { motor.set(0); }
}
