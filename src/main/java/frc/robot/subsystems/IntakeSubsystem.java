// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MechanismConstants.Intake;

public class IntakeSubsystem extends SubsystemBase {
  private final CANSparkMax intakeSushi =
      new CANSparkMax(Intake.INTAKE_SUSHI, MotorType.kBrushless);
  private final CANSparkMax intakeFront =
      new CANSparkMax(Intake.INTAKE_FRONT, MotorType.kBrushless);

  private final ShuffleboardTab intake = Shuffleboard.getTab("Intake");

  public IntakeSubsystem() {
    intakeSushi.setInverted(Intake.INTAKE_SUSHI_INVERTED);
    intakeFront.setInverted(Intake.INTAKE_FRONT_INVERTED);

    intakeFront.setIdleMode(IdleMode.kBrake);
    intakeSushi.setIdleMode(IdleMode.kBrake);

    intake.add("Intake Sushi", intakeSushi);
    intake.add("Intake Front", intakeFront);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /** Run the intake at a set speed in the {@link Intake} class. */
  public void runIntake() {
    intakeSushi.set(Intake.INTAKE_SUSHI_SPEED);
    intakeFront.set(Intake.INTAKE_FRONT_SPEED);
  }

  /** Stop the intake. */
  public void stopIntake() {
    intakeSushi.set(0);
    intakeFront.set(0);
  }

  /** Reverse the intake at a set speed in the {@link Intake} class. */
  public void reverseIntake() {
    intakeSushi.set(-Intake.INTAKE_SUSHI_SPEED);
    intakeFront.set(-Intake.INTAKE_FRONT_SPEED);
  }
}
