// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareConstants;

public class ClimbSubsystem extends SubsystemBase {
  private final DoubleSolenoid climbSolenoid =
      new DoubleSolenoid(
          HardwareConstants.REV_PCM_ID,
          PneumaticsModuleType.REVPH,
          HardwareConstants.PneumaticsChannels.CLIMB_OUT,
          HardwareConstants.PneumaticsChannels.CLIMB_IN);
  private boolean isClimbing = false;

  /**
   * Creates a climber subsystem.
   * 
   * <p>When the robot is initialized, the climb is retracted to avoid damage and the state of the climb is set to false.
   * 
   * <p>When the robot is climbing, the climb is extended to support the robot.
   * 
   * <p>When the robot is not climbing, the climb is retracted to avoid damage.
   */
  public ClimbSubsystem() {
    System.out.println("[CLIMB] ClimbSubsystem initialized.");
    retract();
    SmartDashboard.putBoolean(getName(), isClimbing);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /** Extends the climb. */
  public void extend() {
    climbSolenoid.set(DoubleSolenoid.Value.kForward);
    isClimbing = true;
    SmartDashboard.putBoolean(getName(), isClimbing);
  }

  /** Retracts the climb. */
  public void retract() {
    climbSolenoid.set(DoubleSolenoid.Value.kReverse);
    isClimbing = false;
    SmartDashboard.putBoolean(getName(), isClimbing);
  }
}
