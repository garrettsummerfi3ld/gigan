// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.ConveyorSubsystem.FlywheelSpeed;

public class ShootAuto extends Command {
  private final ConveyorSubsystem m_conveyorSubsystem;
  private final Timer m_timer = new Timer();
  /**
   * Creates a new ShootAuto command.
   * 
   * <p>When the command is initially scheduled, the timer is reset and started.
   * 
   * <p>When the command is executed, the conveyor subsystem is run. If the timer sees that 1 second has passed, the conveyor subsystem is run. If the timer sees that 1 second has passed, the conveyor subsystem is run.
   * 
   * @param conveyorSubsystem The conveyor subsystem used by this command.
   */
  public ShootAuto(ConveyorSubsystem conveyorSubsystem) {
    m_conveyorSubsystem = conveyorSubsystem;
    addRequirements(m_conveyorSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("[AUTO] ShootAuto command initialized.");
    m_timer.reset();
    m_timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_conveyorSubsystem.runConveyor(false);
    if (m_timer.get() > 1) {
      m_conveyorSubsystem.runFlywheel(FlywheelSpeed.HIGH, false);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_conveyorSubsystem.stopConveyor();
    m_conveyorSubsystem.stopFlywheel();
    System.out.println("[AUTO] ShootAuto command ended.");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
