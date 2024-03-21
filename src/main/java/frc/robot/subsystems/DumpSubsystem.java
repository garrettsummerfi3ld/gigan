// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareConstants;

public class DumpSubsystem extends SubsystemBase {
  private final DoubleSolenoid dumpSolenoid =
      new DoubleSolenoid(
          HardwareConstants.REV_PCM_ID,
          PneumaticsModuleType.REVPH,
          HardwareConstants.PneumaticsChannels.DUMP_OUT,
          HardwareConstants.PneumaticsChannels.DUMP_IN);
  private boolean isDumped = false;

  /**
   * Creates a dump subsystem.
   *
   * <p>When the robot is initialized, the dump is retracted to avoid damage and the state of the
   * dump is set to false.
   *
   * <p>When the robot is dumping, the dump is extended which activates the dump bed when dumping a
   * Note into the AMP.
   *
   * <p>When the robot is not dumping, the dump is retracted to avoid damage.
   */
  public DumpSubsystem() {
    System.out.println("[DUMP] DumpSubsystem initialized.");
    retract();
    SmartDashboard.putBoolean(getName(), isDumped);
  }

  @Override
  public void periodic() {}

  /** Dumps the dump bed. */
  public void extend() {
    dumpSolenoid.set(DoubleSolenoid.Value.kForward);
    isDumped = true;
    SmartDashboard.putBoolean(getName(), isDumped);
  }

  /** Retracts the dump bed. */
  public void retract() {
    dumpSolenoid.set(DoubleSolenoid.Value.kReverse);
    isDumped = false;
    SmartDashboard.putBoolean(getName(), isDumped);
  }
}
