// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.DumpSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.swerve.SwerveSubsystem;
import java.io.File;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  // The robot's subsystems and commands are defined here...
  private final SwerveSubsystem drivebase =
      new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve"));
  private final IntakeSubsystem intake = new IntakeSubsystem();
  private final ConveyorSubsystem conveyor = new ConveyorSubsystem();
  private final DumpSubsystem dump = new DumpSubsystem();

  // Controllers and joysticks are defined here
  final CommandXboxController driverXbox = new CommandXboxController(Constants.OperatorConstants.Joysticks.Port.DRIVER_CONTROLLER);
  final CommandJoystick copilotJoystick = new CommandJoystick(Constants.OperatorConstants.Joysticks.Port.COPILOT_CONTROLLER);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    // Applies deadbands and inverts controls because joysticks
    // are back-right positive while robot
    // controls are front-left positive
    // left stick controls translation
    // right stick controls the desired angle NOT angular rotation
    Command driveFieldOrientedDirectAngle =
        drivebase.driveCommand(
            () -> MathUtil.applyDeadband(driverXbox.getLeftY(), OperatorConstants.Deadbands.LEFT_Y),
            () -> MathUtil.applyDeadband(driverXbox.getLeftX(), OperatorConstants.Deadbands.LEFT_X),
            () -> driverXbox.getRightX(),
            () -> driverXbox.getRightY());

    // Applies deadbands and inverts controls because joysticks
    // are back-right positive while robot
    // controls are front-left positive
    // left stick controls translation
    // right stick controls the angular velocity of the robot
    Command driveFieldOrientedAnglularVelocity =
        drivebase.driveCommand(
            () -> MathUtil.applyDeadband(driverXbox.getLeftY(), OperatorConstants.Deadbands.LEFT_Y),
            () -> MathUtil.applyDeadband(driverXbox.getLeftX(), OperatorConstants.Deadbands.LEFT_X),
            () -> driverXbox.getRightX() * 0.5);

    drivebase.setMotorBrake(true);
    drivebase.setDefaultCommand(driveFieldOrientedDirectAngle);
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    driverXbox.a().onTrue(Commands.runOnce(drivebase::zeroGyro));
    driverXbox.x().onTrue(Commands.runOnce(drivebase::addFakeVisionReading));
    driverXbox.b().whileTrue(Commands.deferredProxy(() -> drivebase.driveToPose(new Pose2d(new Translation2d(4, 4), Rotation2d.fromDegrees(0)))));
    driverXbox.y().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());

    copilotJoystick.trigger().onTrue(Commands.runOnce(dump::dump));
    copilotJoystick.trigger().onFalse(Commands.runOnce(dump::retract));
    copilotJoystick.button(3).whileTrue(Commands.runOnce(intake::runIntake));
    copilotJoystick.button(4).whileTrue(Commands.runOnce(intake::reverseIntake));
    copilotJoystick.button(5).whileTrue(Commands.runOnce(conveyor::runConveyor));
    copilotJoystick.button(6).whileTrue(Commands.runOnce(conveyor::reverseConveyor));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return drivebase.getAutonomousCommand("New Auto");
  }
}
