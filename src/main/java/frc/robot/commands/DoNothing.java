// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

public class DoNothing extends Command {
  /**
   * This command does nothing. It is useful for testing, debugging, and as a placeholder for
   * commands when you don't want to do anything.
   *
   * <p>It is recommended to use this command as a placeholder for commands that you don't want to
   * do anything, rather than using {@link edu.wpi.first.wpilibj2.command.InstantCommand} or {@link
   * edu.wpi.first.wpilibj2.command.WaitCommand}.
   */
  public DoNothing() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Do nothing
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
