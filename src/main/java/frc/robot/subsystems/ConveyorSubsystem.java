// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MechanismConstants.Conveyor;

public class ConveyorSubsystem extends SubsystemBase {
  private final CANSparkMax conveyorMotorLeft =
      new CANSparkMax(Conveyor.CONVEYOR_MOTOR_LEFT, MotorType.kBrushless);
  private final CANSparkMax conveyorMotorRight =
      new CANSparkMax(Conveyor.CONVEYOR_MOTOR_RIGHT, MotorType.kBrushless);

  public ConveyorSubsystem() {
    conveyorMotorLeft.setInverted(Conveyor.CONVEYOR_MOTOR_LEFT_INVERTED);
    conveyorMotorRight.setInverted(Conveyor.CONVEYOR_MOTOR_RIGHT_INVERTED);

    conveyorMotorLeft.setIdleMode(IdleMode.kBrake);
    conveyorMotorRight.setIdleMode(IdleMode.kBrake);
  }

  @Override
  public void periodic() {}

  /** Run the conveyor at a set speed in the {@link Conveyor} class. */
  public void runConveyor() {
    conveyorMotorLeft.set(Conveyor.CONVEYOR_MOTOR_SPEED);
    conveyorMotorRight.set(Conveyor.CONVEYOR_MOTOR_SPEED);
  }

  /** Stop the conveyor. */
  public void stopConveyor() {
    conveyorMotorLeft.set(0);
    conveyorMotorRight.set(0);
  }

  /** Reverse the conveyor at a set speed in the {@link Conveyor} class. */
  public void reverseConveyor() {
    conveyorMotorLeft.set(-Conveyor.CONVEYOR_MOTOR_SPEED);
    conveyorMotorRight.set(-Conveyor.CONVEYOR_MOTOR_SPEED);
  }
}
