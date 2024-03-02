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
          HardwareConstants.PneumaticsChannel.DUMP_OUT,
          HardwareConstants.PneumaticsChannel.DUMP_IN);
  private boolean isDumped = false;

  public DumpSubsystem() {
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