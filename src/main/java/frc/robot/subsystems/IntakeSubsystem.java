// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MechanismConstants.Intake;

public class IntakeSubsystem extends SubsystemBase {
  private final CANSparkMax intakeSushi =
      new CANSparkMax(Intake.INTAKE_SUSHI, MotorType.kBrushless);
  private final CANSparkMax intakeFront =
      new CANSparkMax(Intake.INTAKE_FRONT, MotorType.kBrushless);

  /**
   * Creates the intake subsystem.
   *
   * <p>When the robot is initialized, the intake sushi and front motors are set to brake mode.
   *
   * <p>When the robot is initialized, the intake sushi and front motors are set to their respective
   * inverted values.
   */
  public IntakeSubsystem() {
    System.out.println("[INTAKE] IntakeSubsystem initialized.");
    intakeSushi.setInverted(Intake.INTAKE_SUSHI_INVERTED);
    intakeFront.setInverted(Intake.INTAKE_FRONT_INVERTED);

    intakeFront.setIdleMode(IdleMode.kBrake);
    intakeSushi.setIdleMode(IdleMode.kBrake);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * Run the intake at a set speed in the {@link Intake} class.
   *
   * @param isReversed Whether or not to reverse the intake.
   */
  public void runIntake(boolean isReversed) {
    double intakeSushiSpeed = Intake.INTAKE_SUSHI_SPEED;
    double intakeFrontSpeed = Intake.INTAKE_FRONT_SPEED;
    if (isReversed) {
      intakeSushiSpeed *= -1;
      intakeFrontSpeed *= -1;
    }
    intakeSushi.set(intakeSushiSpeed);
    intakeFront.set(intakeFrontSpeed);
  }

  /** Stop the intake. */
  public void stopIntake() {
    intakeSushi.set(0);
    intakeFront.set(0);
  }

  /**
   * Run the intake sushi at a set speed in the {@link Intake} class.
   *
   * @param isReversed Whether or not to reverse the intake sushi.
   */
  public void runIntakeSushi(boolean isReversed) {
    double intakeSushiSpeed = Intake.INTAKE_SUSHI_SPEED;
    if (isReversed) {
      intakeSushiSpeed *= -1;
    }
    intakeSushi.set(intakeSushiSpeed);
  }

  /** Stop the intake sushi. */
  public void stopIntakeSushi() {
    intakeSushi.set(0);
  }

  /**
   * Run the intake front at a set speed in the {@link Intake} class.
   *
   * @param isReversed Whether or not to reverse the intake front.
   */
  public void runIntakeFront(boolean isReversed) {
    double intakeFrontSpeed = Intake.INTAKE_FRONT_SPEED;
    if (isReversed) {
      intakeFrontSpeed *= -1;
    }
    intakeFront.set(intakeFrontSpeed);
  }

  /** Stop the intake front. */
  public void stopIntakeFront() {
    intakeFront.set(0);
  }
}
