// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MechanismConstants.Conveyor;
import frc.robot.Constants.MechanismConstants.Flywheel;

public class ConveyorSubsystem extends SubsystemBase {
  private final CANSparkMax conveyorMotorLeft =
      new CANSparkMax(Conveyor.CONVEYOR_MOTOR_LEFT, MotorType.kBrushless);
  private final CANSparkMax conveyorMotorRight =
      new CANSparkMax(Conveyor.CONVEYOR_MOTOR_RIGHT, MotorType.kBrushless);
  private final CANSparkMax flywheelMotorLeft =
      new CANSparkMax(Flywheel.FLYWHEEL_MOTOR_LEFT, MotorType.kBrushless);
  private final CANSparkMax flywheelMotorRight =
      new CANSparkMax(Flywheel.FLYWHEEL_MOTOR_RIGHT, MotorType.kBrushless);

  public ConveyorSubsystem() {
    System.out.println("[CONVEYOR] ConveyorSubsystem initialized.");
    conveyorMotorLeft.setInverted(Conveyor.CONVEYOR_MOTOR_LEFT_INVERTED);
    conveyorMotorRight.setInverted(Conveyor.CONVEYOR_MOTOR_RIGHT_INVERTED);
    flywheelMotorLeft.setInverted(Flywheel.FLYWHEEL_MOTOR_LEFT_INVERTED);
    flywheelMotorRight.setInverted(Flywheel.FLYWHEEL_MOTOR_RIGHT_INVERTED);

    conveyorMotorLeft.setIdleMode(IdleMode.kBrake);
    conveyorMotorRight.setIdleMode(IdleMode.kBrake);
    flywheelMotorLeft.setIdleMode(IdleMode.kBrake);
    flywheelMotorRight.setIdleMode(IdleMode.kBrake);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * Run the conveyor at a set speed in the {@link Conveyor} class.
   *
   * @param isReversed Whether or not to reverse the conveyor.
   */
  public void runConveyor(boolean isReversed) {
    double conveyorMotorSpeed = Conveyor.CONVEYOR_MOTOR_SPEED;
    if (isReversed) {
      conveyorMotorSpeed *= -1;
    }
    conveyorMotorLeft.set(conveyorMotorSpeed);
    conveyorMotorRight.set(conveyorMotorSpeed);
  }

  /** Stop the conveyor. */
  public void stopConveyor() {
    conveyorMotorLeft.set(0);
    conveyorMotorRight.set(0);
  }

  /**
   * Run the flywheel at a set speed in the {@link Flywheel} class.
   *
   * @param speed The speed to run the flywheel at.
   * @param isReversed Whether or not to reverse the flywheel.
   */
  public void runFlywheel(FlywheelSpeed speed, boolean isReversed) {
    var flywheelMotorSpeed = 0.0;
    switch (speed) {
      case LOW:
        flywheelMotorSpeed = Flywheel.FLYWHEEL_MOTOR_SPEED_LOW;
        break;
      case NORMAL:
        flywheelMotorSpeed = Flywheel.FLYWHEEL_MOTOR_SPEED_NORMAL;
        break;
      case HIGH:
        flywheelMotorSpeed = Flywheel.FLYWHEEL_MOTOR_SPEED_HIGH;
        break;
    }
    if (isReversed) {
      flywheelMotorSpeed *= -1;
    }
    flywheelMotorLeft.set(flywheelMotorSpeed);
    flywheelMotorRight.set(flywheelMotorSpeed);
  }

  /** Stop the flywheel. */
  public void stopFlywheel() {
    flywheelMotorLeft.set(0);
    flywheelMotorRight.set(0);
  }

  /**
   * Run the conveyor and flywheel at a set speed in the {@link Conveyor} and {@link Flywheel}
   * class.
   */
  public void runConveyorAndFlywheel() {
    runConveyor(false);
    runFlywheel(FlywheelSpeed.NORMAL, false);
  }

  /** Stop the conveyor and flywheel. */
  public void stopConveyorAndFlywheel() {
    stopConveyor();
    stopFlywheel();
  }

  /**
   * Reverse the conveyor and flywheel at a set speed in the {@link Conveyor} and {@link Flywheel}
   * class.
   */
  public void reverseConveyorAndFlywheel() {
    runConveyor(false);
    runFlywheel(FlywheelSpeed.NORMAL, true);
  }

  public enum FlywheelSpeed {
    LOW,
    NORMAL,
    HIGH
  }
}
