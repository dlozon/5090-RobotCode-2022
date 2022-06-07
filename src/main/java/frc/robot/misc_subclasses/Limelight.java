
package frc.robot.misc_subclasses;

import edu.wpi.first.networktables.NetworkTableInstance;
import static frc.robot.Constants.*;

public class Limelight {
    // Declare variables
    private double distance;

    private double a2; // Angle difference between limelight and target (height-wise)
    private double tv;
    private double tx; // Rotational angle from limelight to target
    private double ta;

    public boolean hasValidTarget = false;
    public double driveCommand;
    public double steerCommand;

    // Constructor
    public Limelight() {}

    // Accessor methods
    public boolean hasTarget() { return this.hasValidTarget; }
    public double getDistance() { return this.distance; }
    public double getSteer() { return this.steerCommand; }
    public double getDrive() { return this.driveCommand; }
    public double getRotationAngle() { return this.tx; }

    // Calculates each of limelight's values and updates
    // their corresponding variables
    public void updateLimelightTracking()
    {
        // Update Limelight tracking values
        tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
        tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
        a2 = Math.toRadians(NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0));

        // Stops the function here if there is no valid target
        if ( tv < 1 ) {
            hasValidTarget = false;
            driveCommand = 0;
            steerCommand = 0;
            return;
        }
        hasValidTarget = true;

        // Update steer and drive commands
        steerCommand = tx * STEER_K;
        driveCommand = (DESIRED_TARGET_AREA - ta) * DRIVE_K;

        // Enforces speed limit
        if( driveCommand > MAX_DRIVE )
            driveCommand = MAX_DRIVE;

        // Calculates horizontal distance to the target
        distance = Math.round((
            TARGET_HEIGHT - LIME_HEIGHT) / 
            Math.tan(LIME_ANGLE + a2));

            /*Hello, welcome to java quest v1
            to begin, you must answer the questions correctly, and if you do,
             the answer will direct you to the next page until you win.
             your first question is:

             Who was the origional banana man?
             A) Eli Goretta-GoTo Drivetrain.Java L41
             B)Caleb Lawrence-GoTo Turret.Java  L22
             C)Luke Valar-GoTo Intake.Java L47
             D)Jonathon Houston-GoTo RobotContainer.java L35
             */
    }
}