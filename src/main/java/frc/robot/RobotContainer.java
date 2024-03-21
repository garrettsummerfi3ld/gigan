// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants.Deadbands;
import frc.robot.commands.DoNothing;
import frc.robot.commands.swerve.AbsoluteDrive;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.ConveyorSubsystem.FlywheelSpeed;
import frc.robot.subsystems.DumpSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.swerve.SwerveSubsystem;
import java.io.File;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;
import org.photonvision.PhotonCamera;

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

  // Cameras are defined here
  final PhotonCamera limelight = new PhotonCamera("limelight");
  final PhotonCamera rasperryPi = new PhotonCamera("raspberry-pi");

  // AprilTag fields are defined here
  AprilTagFieldLayout aprilTagFieldLayout = AprilTagFields.k2024Crescendo.loadAprilTagLayoutField();

  // The robot's subsystems and commands are defined here...
  private final SwerveSubsystem drivebase =
      new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve"));
  private final IntakeSubsystem intake = new IntakeSubsystem();
  private final ConveyorSubsystem conveyor = new ConveyorSubsystem();
  private final DumpSubsystem dump = new DumpSubsystem();
  private final ClimbSubsystem climb = new ClimbSubsystem();
  private final DoNothing nothing = new DoNothing();

  // Autonomous chooser is defined here
  private final LoggedDashboardChooser<Command> autoChooser;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    System.out.println("[ROBOT] RobotContainer initialized.");
    // Configure the trigger bindings
    configureBindings();

    AbsoluteDrive absoluteDrive =
        new AbsoluteDrive(
            drivebase,
            () -> MathUtil.applyDeadband(-pilotXbox.getLeftY(), Deadbands.LEFT_X),
            () -> MathUtil.applyDeadband(-pilotXbox.getLeftX(), Deadbands.LEFT_Y),
            () -> MathUtil.applyDeadband(-pilotXbox.getRightX(), Deadbands.RIGHT_X),
            () -> MathUtil.applyDeadband(-pilotXbox.getRightY(), Deadbands.RIGHT_Y));

    drivebase.setMotorBrake(true);
    drivebase.setDefaultCommand(absoluteDrive);

    // Create a LoggedDashboardChooser to select the autonomous command
    autoChooser =
        new LoggedDashboardChooser<>("Autonomous Chooser", AutoBuilder.buildAutoChooser());
    autoChooser.addDefaultOption("Nothing", nothing);
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
    if (autoChooser.get() == null) {
      System.out.println("[AUTO] Running " + autoChooser.get().getName());
      return nothing;
    }
    return drivebase.getAutonomousCommand(autoChooser.get().getName());
  }

  /**
   * Configure the button bindings for the pilot controller from the {@link configureBindings}
   * method.
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
    pilotXbox.x().onTrue(Commands.runOnce(() -> drivebase.aimAtTarget(limelight)));
    pilotXbox
        .b()
        .whileTrue(
            Commands.deferredProxy(
                () ->
                    drivebase.driveToPose(
                        new Pose2d(new Translation2d(2, 7.5), Rotation2d.fromDegrees(0)))));
    pilotXbox.y().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
    pilotXbox
        .leftBumper()
        .whileTrue(Commands.runOnce(() -> drivebase.aimAtTarget(rasperryPi)).repeatedly());
    System.out.println("[BINDS] Pilot controller configured");
  }

  /**
   * Configure the button bindings for the copilot controller from the {@link configureBindings}
   * method.
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
    configureButton(
        3,
        () -> {
          intake.runIntakeFront(false);
          conveyor.runConveyor(false);
        });
    configureButton(
        4,
        () -> {
          intake.runIntakeFront(true);
          conveyor.runConveyor(true);
        });
    configureButton(
        5,
        () -> {
          intake.runIntakeSushi(false);
          conveyor.runConveyor(false);
        });
    configureButton(
        6,
        () -> {
          intake.runIntakeSushi(true);
          conveyor.runConveyor(true);
        });
    configureButton(7, () -> conveyor.runFlywheel(FlywheelSpeed.LOW, false));
    configureButton(9, () -> conveyor.runConveyor(true));
    configureButton(10, () -> conveyor.runConveyor(false));
    configureButton(11, () -> conveyor.runFlywheel(FlywheelSpeed.LOW, false));
    configureButton(12, () -> conveyor.runFlywheel(FlywheelSpeed.NORMAL, false));
    int pov = copilotJoystick.getHID().getPOV();
    switch (pov) {
      case 0:
      case 45:
      case 315:
        Commands.runOnce(() -> conveyor.runFlywheel(FlywheelSpeed.HIGH, false)).repeatedly();
        break;
      case 135:
      case 180:
      case 225:
        Commands.runOnce(() -> conveyor.runFlywheel(FlywheelSpeed.LOW, false)).repeatedly();
        break;
      default:
        // Handle other cases or do nothing
        break;
    }

    if (copilotJoystick.getThrottle() > 0) {
      Commands.runOnce(climb::extend);
    } else if (copilotJoystick.getThrottle() < 0) {
      Commands.runOnce(climb::retract);
    }
    System.out.println("[BINDS] Copilot controller configured");
  }

  /**
   * Configure a button to run a specific action when pressed.
   *
   * @param buttonNumber The button index to configure, starting at 1.
   * @param action The action to run when the button is pressed.
   */
  private void configureButton(int buttonNumber, Runnable action) {
    copilotJoystick.button(buttonNumber).whileTrue(Commands.runOnce(action).repeatedly());
  }
}
