// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;


import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
public class SwerveDrivetrain extends SubsystemBase {

  //these are limits you can change!!!
  public static final double kMaxSpeed = Units.feetToMeters(13.6); // 20 feet per second
  public static final double kMaxAngularSpeed = Math.PI; // 1/2 rotation per second
  public static double feildCalibration = 0;

  //this is where you put the angle offsets you got from the smart dashboard

  public static double frontLeftOffset = 0;
  public static double frontRightOffset = 0;
  public static double backLeftOffset = 0;
  public static double backRightOffset = 0;


  //put your can Id's here!
  public static final int frontLeftDriveId = 26; 
  public static final int frontLeftCANCoderId = 33; 
  public static final int frontLeftSteerId = 27;
  //put your can Id's here!
  public static final int frontRightDriveId = 20; 
  public static final int frontRightCANCoderId = 30; 
  public static final int frontRightSteerId = 21; 
  //put your can Id's here!
  public static final int backLeftDriveId = 24; 
  public static final int backLeftCANCoderId = 32; 
  public static final int backLeftSteerId = 25;
  //put your can Id's here!

  public static final int backRightDriveId = 22; 
  public static final int backRightCANCoderId = 31; 
  public static final int backRightSteerId = 23;   
  public static AHRS gyro = new AHRS(SPI.Port.kMXP);

  private SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
    new Translation2d(
      Units.inchesToMeters(10),
      Units.inchesToMeters(10)
    ),
    new Translation2d(
      Units.inchesToMeters(10),
      Units.inchesToMeters(-10)
    ),
    new Translation2d(
      Units.inchesToMeters(-10),
      Units.inchesToMeters(10)
    ),
    new Translation2d(
      Units.inchesToMeters(-10),
      Units.inchesToMeters(-10)
    )
  );

 

  private SwerveModuleMK3[] modules = new SwerveModuleMK3[] {

    new SwerveModuleMK3(new TalonFX(frontLeftDriveId), new TalonFX(frontLeftSteerId), new CANCoder(frontLeftCANCoderId), Rotation2d.fromDegrees(frontLeftOffset)), // Front Left
    new SwerveModuleMK3(new TalonFX(frontRightDriveId), new TalonFX(frontRightSteerId), new CANCoder(frontRightCANCoderId), Rotation2d.fromDegrees(frontRightOffset)), // Front Right
    new SwerveModuleMK3(new TalonFX(backLeftDriveId), new TalonFX(backLeftSteerId), new CANCoder(backLeftCANCoderId), Rotation2d.fromDegrees(backLeftOffset)), // Back Left
    new SwerveModuleMK3(new TalonFX(backRightDriveId), new TalonFX(backRightSteerId), new CANCoder(backRightCANCoderId), Rotation2d.fromDegrees(backRightOffset))  // Back Right

  };

  public SwerveDrivetrain() {
   // gyro.reset(); 
  }

  /**
   * Method to drive the robot using joystick info.
   *
   * @param xSpeed Speed of the robot in the x direction (forward).
   * @param ySpeed Speed of the robot in the y direction (sideways).
   * @param rot Angular rate of the robot.
   * @param fieldRelative Whether the provided x and y speeds are relative to the field.
   * @param calibrateGyro button to recalibrate the gyro offset
   */
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative, boolean calibrateGyro) {
    
    if(calibrateGyro){
      gyro.reset(); //recalibrates gyro offset
    }

    SwerveModuleState[] states =
      kinematics.toSwerveModuleStates(
        fieldRelative
          ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rot, Rotation2d.fromDegrees(-gyro.getAngle()))
          : new ChassisSpeeds(xSpeed, ySpeed, rot));
    SwerveDriveKinematics.normalizeWheelSpeeds(states, kMaxSpeed);
    for (int i = 0; i < states.length; i++) {
      SwerveModuleMK3 module = modules[i];
      SwerveModuleState state = states[i];
      SmartDashboard.putNumber(String.valueOf(i), module.getRawAngle());
      //below is a line to comment out from step 5
      module.setDesiredState(state);
      SmartDashboard.putNumber("gyro Angle", gyro.getAngle());
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
