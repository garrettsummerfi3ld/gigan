// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.SignalLogger;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.AlertContants;
import frc.robot.Constants.HardwareConstants;
import frc.robot.util.Alert;
import frc.robot.util.Alert.AlertType;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggedPowerDistribution;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;
import org.littletonrobotics.urcl.URCL;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends LoggedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  private final Timer canErrorTimer = new Timer();
  private final Timer canErrorTimerInit = new Timer();
  private final Timer disabledTimer = new Timer();

  private final Alert canErr =
      new Alert(
          "CAN bus error! Please check the CAN bus connections and power cycle the robot. Robot may be uncontrollable!",
          AlertType.ERROR);
  private final Alert lowBatt =
      new Alert(
          "Low battery! Please charge the robot or switch the battery out for a charged one. Robot may brownout and become uncontrollable!",
          AlertType.WARNING);
  private final Alert noLog =
      new Alert("No log file found! Please check the USB stick.", AlertType.WARNING);
  private final Alert gitDirty =
      new Alert("Git repository is dirty! Please commit or stash changes.", AlertType.WARNING);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Git metadata
    Logger.recordMetadata("RobotSource", "gigan");
    Logger.recordMetadata("RuntimeType", getRuntimeType().toString());
    Logger.recordMetadata("BuildDate", BuildConstants.BUILD_DATE);
    Logger.recordMetadata("GitSHA", BuildConstants.GIT_SHA);
    Logger.recordMetadata("GitDate", BuildConstants.GIT_DATE);
    Logger.recordMetadata("GitBranch", BuildConstants.GIT_BRANCH);
    switch (BuildConstants.DIRTY) {
      case 0:
        Logger.recordMetadata("GitDirty", "Clean");
        break;
      case 1:
        Logger.recordMetadata("GitDirty", "Uncommitted changes");
        gitDirty.set(true);
        break;
      default:
        Logger.recordMetadata("GitDirty", "Unknown");
        break;
    }

    Logger.addDataReceiver(new NT4Publisher());
    if (isReal()) {
      LoggedPowerDistribution.getInstance(HardwareConstants.REV_PDH_ID, ModuleType.kRev);
      Logger.registerURCL(URCL.startExternal());
      if (Paths.get("/U").getParent() != null) {
        Logger.addDataReceiver(new WPILOGWriter());
        noLog.set(true);
        SignalLogger.start();
      } else {
        setUseTiming(false);
        try {
          String logPath = LogFileUtil.findReplayLog();
          Logger.setReplaySource(new WPILOGReader(logPath));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    Logger.start();

    // Reset and start timers for error checking
    canErrorTimerInit.reset();
    canErrorTimerInit.start();
    canErrorTimer.reset();
    canErrorTimer.start();
    disabledTimer.reset();
    disabledTimer.start();
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();

    var canStats = RobotController.getCANStatus();
    if (canStats.receiveErrorCount > 0 || canStats.transmitErrorCount > 0) {
      canErrorTimer.reset();
    }
    canErr.set(
        !canErrorTimer.hasElapsed(AlertContants.CAN_ALERT_TIME)
            && canErrorTimerInit.hasElapsed(AlertContants.CAN_ALERT_TIME));

    if (DriverStation.isEnabled()) {
      disabledTimer.reset();
    }
    if (RobotController.getBatteryVoltage() < AlertContants.LOW_BATTERY_VOLTAGE
        && disabledTimer.hasElapsed(AlertContants.LOW_BATTERY_TIME)) {
      lowBatt.set(true);
    } else {
      lowBatt.set(false);
    }

    // Log list of NT clients
    List<String> clientNames = new ArrayList<>();
    List<String> clientAddresses = new ArrayList<>();
    for (var client : NetworkTableInstance.getDefault().getConnections()) {
      clientNames.add(client.remote_id);
      clientAddresses.add(client.remote_ip);
    }
    Logger.recordOutput("NTClients/Names", clientNames.toArray(new String[clientNames.size()]));
    Logger.recordOutput(
        "NTClients/Addresses", clientAddresses.toArray(new String[clientAddresses.size()]));
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
