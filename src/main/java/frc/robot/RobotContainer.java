// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants.Deadbands;
import frc.robot.commands.swerve.AbsoluteDriveAdv;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.ConveyorSubsystem.FlywheelSpeed;
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
  // Controllers and joysticks are defined here
  final CommandXboxController pilotXbox =
      new CommandXboxController(Constants.OperatorConstants.Joysticks.Port.PILOT_CONTROLLER);
  final CommandJoystick copilotJoystick =
      new CommandJoystick(Constants.OperatorConstants.Joysticks.Port.COPILOT_CONTROLLER);
  final XboxController driverXboxHID = pilotXbox.getHID();

  // The robot's subsystems and commands are defined here...
  private final SwerveSubsystem drivebase =
      new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve"));
  private final IntakeSubsystem intake = new IntakeSubsystem();
  private final ConveyorSubsystem conveyor = new ConveyorSubsystem();
  private final DumpSubsystem dump = new DumpSubsystem();
  private final ClimbSubsystem climb = new ClimbSubsystem();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    System.out.println("[ROBOT] RobotContainer initialized.");
    // Configure the trigger bindings
    configureBindings();

    AbsoluteDriveAdv absoluteDrive =
        new AbsoluteDriveAdv(
            drivebase,
            () -> MathUtil.applyDeadband(-pilotXbox.getLeftX(), Deadbands.LEFT_X),
            () -> MathUtil.applyDeadband(-pilotXbox.getLeftY(), Deadbands.LEFT_Y),
            () -> MathUtil.applyDeadband(-pilotXbox.getRightX(), Deadbands.RIGHT_X),
            () -> pilotXbox.pov(0).getAsBoolean(),
            () -> pilotXbox.pov(180).getAsBoolean(),
            () -> pilotXbox.pov(270).getAsBoolean(),
            () -> pilotXbox.pov(90).getAsBoolean());

    drivebase.setMotorBrake(true);
    drivebase.setDefaultCommand(absoluteDrive);
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
    System.out.println("[BINDS] Configuring bindings");
    configurePilotController();
    configureCopilotController();
    System.out.println("[BINDS] Bindings configured");
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

  /**
   * Configure the button bindings for the pilot controller.
   *
   * <p>This controller is used to drive the robot and control the swerve drive.
   *
   * <p>The A button is used to zero the gyro. The X button is used to add a fake vision reading.
   * The B button is used to drive to a specific pose. The Y button is used to lock the swerve
   * drive.
   *
   * <p>This is using an Xbox controller.
   */
  public void configurePilotController() {
    System.out.println("[BINDS] Configuring pilot controller");
    pilotXbox.a().onTrue(Commands.runOnce(drivebase::zeroGyro));
    pilotXbox.x().onTrue(Commands.runOnce(drivebase::addFakeVisionReading));
    pilotXbox
        .b()
        .whileTrue(
            Commands.deferredProxy(
                () ->
                    drivebase.driveToPose(
                        new Pose2d(new Translation2d(4, 4), Rotation2d.fromDegrees(0)))));
    pilotXbox.y().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
    System.out.println("[BINDS] Pilot controller configured");
  }

  /**
   * Configure the button bindings for the copilot controller.
   *
   * <p>This controller is used to control the intake, conveyor, dump, and climber.
   *
   * <p>The trigger is used to extend the dump. The thumb trigger is used to retract the dump.
   *
   * <p>Button 3 is used to run the intake and conveyor in reverse. Button 4 is used to run the
   * intake and conveyor forward. Button 5 is used to run the intake sushi in reverse. Button 6 is
   * used to run the intake sushi forward. Button 7 is used to run the conveyor and flywheel at low
   * speed. Button 9 is used to run the conveyor forward. Button 10 is used to stop the conveyor.
   * Button 11 is used to run the conveyor and flywheel at normal speed. Button 12 is used to run
   * the conveyor and flywheel at high speed.
   *
   * <p>The POV is used to control the flywheel. POV at values 315, 0, and 45 are used to run the
   * flywheels at full speed. POV at values 135, 180, and 225 are used to run the flywheels at slow
   * speed.
   *
   * <p>Throttle is used to control the climber. The values are inverted because of the way the
   * controller values are laid out. Throttle at 1 is used to run the climber up. Throttle at -1 is
   * used to run the climber down.
   */
  public void configureCopilotController() {
    System.out.println("[BINDS] Configuring copilot controller");
    copilotJoystick.trigger().onTrue(Commands.runOnce(dump::extend).repeatedly());
    copilotJoystick.trigger().onFalse(Commands.runOnce(dump::retract).repeatedly());
    copilotJoystick
        .button(3)
        .whileTrue(
            Commands.runOnce(
                    () -> {
                      intake.runIntakeFront(false);
                      conveyor.runConveyor(false);
                    })
                .repeatedly());
    copilotJoystick
        .button(4)
        .whileTrue(
            Commands.runOnce(
                    () -> {
                      intake.runIntakeFront(true);
                      conveyor.runConveyor(true);
                    })
                .repeatedly());
    copilotJoystick
        .button(5)
        .whileTrue(
            Commands.runOnce(
                    () -> {
                      intake.runIntakeSushi(false);
                      conveyor.runConveyor(false);
                    })
                .repeatedly());
    copilotJoystick
        .button(6)
        .whileTrue(
            Commands.runOnce(
                    () -> {
                      intake.runIntakeSushi(true);
                      conveyor.runConveyor(true);
                    })
                .repeatedly());
    copilotJoystick
        .button(7)
        .whileTrue(
            Commands.runOnce(
                    () -> {
                      conveyor.runFlywheel(FlywheelSpeed.LOW, false);
                    })
                .repeatedly());
    copilotJoystick
        .button(9)
        .whileTrue(
            Commands.runOnce(
                    () -> {
                      conveyor.runConveyor(true);
                    })
                .repeatedly());
    copilotJoystick
        .button(10)
        .whileTrue(
            Commands.runOnce(
                    () -> {
                      conveyor.runConveyor(false);
                    })
                .repeatedly());
    copilotJoystick
        .button(11)
        .whileTrue(
            Commands.runOnce(
                    () -> {
                      conveyor.runFlywheel(FlywheelSpeed.LOW, false);
                    })
                .repeatedly());
    copilotJoystick
        .button(12)
        .whileTrue(
            Commands.runOnce(
                    () -> {
                      conveyor.runFlywheel(FlywheelSpeed.NORMAL, false);
                    })
                .repeatedly());

    copilotJoystick
        .pov(0)
        .whileTrue(
            Commands.runOnce(() -> conveyor.runFlywheel(FlywheelSpeed.HIGH, false)).repeatedly());
    copilotJoystick
        .pov(45)
        .whileTrue(
            Commands.runOnce(() -> conveyor.runFlywheel(FlywheelSpeed.HIGH, false)).repeatedly());
    copilotJoystick
        .pov(315)
        .whileTrue(
            Commands.runOnce(() -> conveyor.runFlywheel(FlywheelSpeed.HIGH, false)).repeatedly());
    copilotJoystick
        .pov(135)
        .whileTrue(
            Commands.runOnce(() -> conveyor.runFlywheel(FlywheelSpeed.LOW, false)).repeatedly());
    copilotJoystick
        .pov(180)
        .whileTrue(
            Commands.runOnce(() -> conveyor.runFlywheel(FlywheelSpeed.LOW, false)).repeatedly());
    copilotJoystick
        .pov(225)
        .whileTrue(
            Commands.runOnce(() -> conveyor.runFlywheel(FlywheelSpeed.LOW, false)).repeatedly());

    if (copilotJoystick.getThrottle() > 0) {
      Commands.runOnce(climb::extend).repeatedly();
    } else if (copilotJoystick.getThrottle() < 0) {
      Commands.runOnce(climb::retract).repeatedly();
    }
    System.out.println("[BINDS] Copilot controller configured");
  }
}
