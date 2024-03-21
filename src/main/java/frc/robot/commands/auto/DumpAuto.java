// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DumpSubsystem;

public class DumpAuto extends Command {
  private final DumpSubsystem m_dumpSubsystem;
  private final Timer m_timer = new Timer();

  /**
   * Creates a new DumpAuto command.
   *
   * <p>When the command is initially scheduled, the timer is reset and started.
   *
   * <p>When the command is executed, the dump subsystem is extended. If the timer sees that 1
   * second has passed, the dump subsystem is retracted. If the timer sees that 1.5 seconds have
   * passed, the dump subsystem is extended. If the timer sees that 2 seconds have passed, the dump
   * subsystem is retracted.
   *
   * @param dumpSubsystem The dump subsystem used by this command.
   */
  public DumpAuto(DumpSubsystem dumpSubsystem) {
    m_dumpSubsystem = dumpSubsystem;
    addRequirements(m_dumpSubsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_timer.reset();
    m_timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_dumpSubsystem.extend();
    if (m_timer.get() > 1.0) {
      m_dumpSubsystem.retract();
    }
    if (m_timer.get() > 1.5) {
      m_dumpSubsystem.extend();
    }
    if (m_timer.get() > 2.0) {
      m_dumpSubsystem.retract();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_dumpSubsystem.retract();
    System.out.println("[AUTO] DumpAuto command ended.");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
