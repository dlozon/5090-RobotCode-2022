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
        this.FLMotor = new CANSparkMax(FLMotorID, MotorType.kBrushless);
        FLMotor.restoreFactoryDefaults();
        this.FRMotor = new CANSparkMax( FRMotorID, MotorType.kBrushless);
        FRMotor.restoreFactoryDefaults();
        this.RLMotor = new CANSparkMax( RLMotorID, MotorType.kBrushless);
        RLMotor.restoreFactoryDefaults();
        this.RRMotor = new CANSparkMax( RRMotorID, MotorType.kBrushless);
        RRMotor.restoreFactoryDefaults(); 

        // Create groups
        leftMotors = new MotorControllerGroup(FLMotor, RLMotor);
        rightMotors = new MotorControllerGroup(FRMotor, RRMotor);
    }

    // Accessor methods (getters)
    public CANSparkMax getFLMotor() { return this.FLMotor; }
    public CANSparkMax getFRMotor() { return this.FRMotor; }
    public CANSparkMax getRLMotor() { return this.RLMotor; }
    //You Lost! Haha Loser
    public CANSparkMax getRRMotor() { return this.RRMotor; }
    public MotorControllerGroup getRightMotorGroup() { return this.rightMotors; }
    public MotorControllerGroup getLeftMotorGroup() { return this.leftMotors; }

}

