// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeAuto extends Command {
  private final IntakeSubsystem m_intakeSubsystem;
  private final Timer m_timer = new Timer();

  /**
   * Creates a new IntakeAuto command.
   *
   * <p>When the command is initially scheduled, the timer is reset and started.
   *
   * <p>When the command is executed, the intake subsystem is run. If the timer sees that 3 seconds
   * have passed, the intake subsystem is stopped.
   *
   * @param intakeSubsystem The intake subsystem used by this command.
   */
  public IntakeAuto(IntakeSubsystem intakeSubsystem) {
    m_intakeSubsystem = intakeSubsystem;
    addRequirements(m_intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("[AUTO] IntakeAuto command initialized.");
    m_timer.reset();
    m_timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_intakeSubsystem.runIntake(false);
    if (m_timer.get() > 3) {
      m_intakeSubsystem.stopIntake();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intakeSubsystem.stopIntake();
    System.out.println("[AUTO] IntakeAuto command ended.");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
