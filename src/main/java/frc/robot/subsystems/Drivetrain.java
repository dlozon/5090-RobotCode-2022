package frc.robot.subsystems;

// Imports
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Drivetrain {
    // Declare motors
    // Motors are named based on their position
    // eg. Rear Left Motor --> RLMotor
    // eg. Front Right Motor --> FRMotor
    private CANSparkMax FLMotor;
    private CANSparkMax FRMotor;
    private CANSparkMax RLMotor;
    private CANSparkMax RRMotor; 
    MotorControllerGroup leftMotors;
    MotorControllerGroup rightMotors;

    // Constructor method
    public Drivetrain( int FLMotorID, int FRMotorID, int RLMotorID, int RRMotorID ) {
        // Initialize motors
        this.FLMotor = new CANSparkMax( FLMotorID, MotorType.kBrushless);
        this.FLMotor.setSmartCurrentLimit(60);
        this.FRMotor = new CANSparkMax( FRMotorID, MotorType.kBrushless);
        this.FRMotor.setSmartCurrentLimit(60);
        this.RLMotor = new CANSparkMax( RLMotorID, MotorType.kBrushless);
        this.RLMotor.setSmartCurrentLimit(60);
        this.RRMotor = new CANSparkMax( RRMotorID, MotorType.kBrushless);
        this.RRMotor.setSmartCurrentLimit(60);


        // Create groups
        leftMotors = new MotorControllerGroup(FLMotor, RLMotor);
        rightMotors = new MotorControllerGroup(FRMotor, RRMotor);
    }

    // Accessor methods (getters)
    public CANSparkMax getFLMotor() { return this.FLMotor; }
    public CANSparkMax getFRMotor() { return this.FRMotor; }
    public CANSparkMax getRLMotor() { return this.RLMotor; }
    public CANSparkMax getRRMotor() { return this.RRMotor; }
    public MotorControllerGroup getRightMotorGroup() { return this.rightMotors; }
    public MotorControllerGroup getLeftMotorGroup() { return this.leftMotors; }

}

