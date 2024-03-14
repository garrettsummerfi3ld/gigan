// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.util.PIDConstants;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import swervelib.math.Matter;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

  public static final double ROBOT_MASS = (148 - 20.3) * 0.453592; // 32lbs * kg per pound
  public static final Matter CHASSIS =
      new Matter(new Translation3d(0, 0, Units.inchesToMeters(8)), ROBOT_MASS);
  public static final double LOOP_TIME = 0.13; // s, 20ms + 110ms sprk max velocity lag

  /**
   * Constants for hardware devices on the robot.
   *
   * <p>These constants should be used to identify the ports and channels of each device connected
   * to the robot. This makes it easy to change the wiring of the robot without changing the code.
   */
  public final class HardwareConstants {
    public static final int REV_PDH_ID = 60;
    public static final int REV_PCM_ID = 61;

    public static final class PneumaticsChannels {
      public static final int DUMP_OUT = 15;
      public static final int DUMP_IN = 13;
      public static final int CLIMB_OUT = 11;
      public static final int CLIMB_IN = 10;
    }
  }

  /**
   * Constants for each mechanism on the robot. These constants should be used to tune the robot's
   * behavior to match the physical properties of each mechanism.
   */
  public static final class MechanismConstants {

    /**
     * Intake subsystem constants. These constants should be used to tune the intake's behavior to
     * match the physical properties of the intake.
     */
    public static final class Intake {
      public static final int INTAKE_SUSHI = 51;
      public static final int INTAKE_FRONT = 52;
      public static final boolean INTAKE_SUSHI_INVERTED = false;
      public static final boolean INTAKE_FRONT_INVERTED = true;
      public static final double INTAKE_FRONT_SPEED = 0.2;
      public static final double INTAKE_SUSHI_SPEED = 0.8;
    }

    /**
     * Conveyor subsystem constants. These constants should be used to tune the conveyor's behavior
     * to match the physical properties of the conveyor.
     */
    public static final class Conveyor {
      public static final int CONVEYOR_MOTOR_LEFT = 1;
      public static final int CONVEYOR_MOTOR_RIGHT = 2;
      public static final boolean CONVEYOR_MOTOR_LEFT_INVERTED = true;
      public static final boolean CONVEYOR_MOTOR_RIGHT_INVERTED = false;
      public static final double CONVEYOR_MOTOR_SPEED = 0.38;
    }

    /**
     * Flywheel subsystem constants. These constants should be used to tune the flywheel's behavior
     * to match the physical properties of the flywheel. This would be used in conjunction to the
     * conveyor as having a flywheel alongside the conveyor would be beneficial.
     */
    public static final class Flywheel {
      public static final int FLYWHEEL_MOTOR_LEFT = 3;
      public static final int FLYWHEEL_MOTOR_RIGHT = 4;
      public static final boolean FLYWHEEL_MOTOR_LEFT_INVERTED = false;
      public static final boolean FLYWHEEL_MOTOR_RIGHT_INVERTED = false;
      public static final double FLYWHEEL_MOTOR_SPEED_HIGH = 1.0;
      public static final double FLYWHEEL_MOTOR_SPEED_NORMAL = 0.25;
      public static final double FLYWHEEL_MOTOR_SPEED_LOW = 0.15;
    }
  }

  /** Constants for the drivetrain. */
  public static final class DrivebaseConstants {
    // Hold time on motor brakes when disabled in seconds
    public static final double WHEEL_LOCK_TIME = 10;
  }

  /** Constants for the autonomous period of the match. */
  public static final class AutonConstants {
    public static final PIDConstants TRANSLATION_PID = new PIDConstants(0.7, 0, 0);
    public static final PIDConstants ANGLE_PID = new PIDConstants(0.4, 0, 0.01);
  }

  /** Constants for the operator interface. */
  public static class OperatorConstants {

    /** Constants for the joysticks on the operator interface. */
    public static class Joysticks {
      /**
       * These constants represent the port that each joystick is plugged into on the driver station
       * software.
       */
      public static class Port {
        public static final int PILOT_CONTROLLER = 0;
        public static final int COPILOT_CONTROLLER = 1;
      }

      /**
       * These constants should only be used on the copilot controller. They represent the raw
       * button mappings for the copilot controller.
       *
       * <p>Do not use these constants with Xbox controllers. Use the CommandXboxController class to
       * map buttons to commands.
       */
      public static class RawButtons {}
    }

    /**
     * Deadbands for the joysticks to prevent "stick drift"
     *
     * <p>Depending on the controller used, the joysticks may not return to exactly zero when
     * released. This can cause the robot to move when the joysticks are not being touched. These
     * constants should be used to filter out small joystick movements.
     */
    public static class Deadbands {
      public static final double LEFT_X = 0.1;
      public static final double LEFT_Y = 0.1;
      public static final double RIGHT_X = 0.1;
      public static final double RIGHT_Y = 0.1;
      public static final double TURN_CONSTANT = 6;
    }
  }

  /**
   * Constants for the alert system.
   *
   * <p>These constants should only be changed if the alert system is not functioning as expected.
   */
  public static final class AlertContants {
    public static final double CAN_ALERT_TIME = 0.5;
    public static final double LOW_BATTERY_VOLTAGE = 11.0;
    public static final double LOW_BATTERY_TIME = 2.0;
  }
}
