// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.AlertContants;
import frc.robot.Constants.VisionConstants.ConveyorCamera;
import frc.robot.Constants.VisionConstants.IntakeCamera;
import frc.robot.Constants.VisionConstants.Limelight;
import frc.robot.util.Alert;
import frc.robot.util.Alert.AlertType;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;

public class Vision extends SubsystemBase {
  private final PhotonCamera limelight;
  private final PhotonCamera intakeCamera;
  private final PhotonCamera conveyorCamera;
  private final AprilTagFieldLayout aprilTagFieldLayout;
  private final PhotonPoseEstimator photonLimeLightPoseEstimator;

  private final Alert latencyAlert =
      new Alert("PhotonVision latency is too high, results may be inaccurate.", AlertType.WARNING);

  /**
   * Creates a new Vision subsystem, powered by PhotonVision.
   *
   * <p>PhotonVision is a vision processing library that provides a simple API for interacting with
   * a variety of vision targets, including reflective tape, vision targets, and AprilTags.
   */
  public Vision() {
    System.out.println("[VISION] Vision subsystem initialized.");

    aprilTagFieldLayout = AprilTagFields.k2024Crescendo.loadAprilTagLayoutField();

    limelight = new PhotonCamera(Limelight.NAME);
    intakeCamera = new PhotonCamera(IntakeCamera.NAME);
    conveyorCamera = new PhotonCamera(ConveyorCamera.NAME);

    // Cam mounted facing backwards, 12.5 in back from the robot, flat, and facing backwards
    Transform3d robotToLimelight =
        new Transform3d(
            new Translation3d(Limelight.X_LOC, Limelight.Y_LOC, Limelight.Z_LOC),
            new Rotation3d(Limelight.ROLL, Limelight.PITCH, Limelight.YAW));

    // Cam mounted facing forwards, 11.75 in front of the robot, 20 degrees down, and facing
    // forwards
    Transform3d robotToIntakeCamera =
        new Transform3d(
            new Translation3d(IntakeCamera.X_LOC, IntakeCamera.Y_LOC, IntakeCamera.Z_LOC),
            new Rotation3d(IntakeCamera.ROLL, IntakeCamera.PITCH, IntakeCamera.YAW));

    // Cam mounted facing downwards, 5.5 in front of the robot, 90 degrees down, and facing
    // backwards
    Transform3d robotToConveyorCamera =
        new Transform3d(
            new Translation3d(ConveyorCamera.X_LOC, ConveyorCamera.Y_LOC, ConveyorCamera.Z_LOC),
            new Rotation3d(ConveyorCamera.ROLL, ConveyorCamera.PITCH, ConveyorCamera.YAW));

    // Construct a PhotonPoseEstimator with the AprilTagFieldLayout, a PoseStrategy, the camera, and
    // the transform from the robot to the camera.
    photonLimeLightPoseEstimator =
        new PhotonPoseEstimator(
            aprilTagFieldLayout,
            PoseStrategy.CLOSEST_TO_REFERENCE_POSE,
            limelight,
            robotToLimelight);

    // Set the multi tag fallback strategy to closest to reference pose
    photonLimeLightPoseEstimator.setMultiTagFallbackStrategy(
        PoseStrategy.CLOSEST_TO_REFERENCE_POSE);
  }

  /**
   * Get the limelight camera
   *
   * @return the limelight camera {@link PhotonCamera} object
   */
  public PhotonCamera getLimelight() {
    return limelight;
  }

  /**
   * Get the intake camera
   *
   * @return the intake camera {@link PhotonCamera} object
   */
  public PhotonCamera getIntakeCamera() {
    return intakeCamera;
  }

  /**
   * Get the conveyor camera
   *
   * @return the conveyor camera {@link PhotonCamera} object
   */
  public PhotonCamera getConveyorCamera() {
    return conveyorCamera;
  }

  /**
   * Get the latest result from the limelight
   *
   * @return the latest result from the limelight in the form of a {@link PhotonPipelineResult}
   */
  public PhotonPipelineResult getLimelightResult() {
    return limelight.getLatestResult();
  }

  /**
   * Get the latest result from the intake camera
   *
   * @return the latest result from the intake camera in the form of a {@link PhotonPipelineResult}
   */
  public PhotonPipelineResult getIntakeCameraResult() {
    return intakeCamera.getLatestResult();
  }

  /**
   * Get the latest result from the conveyor camera
   *
   * @return the latest result from the conveyor camera in the form of a {@link
   *     PhotonPipelineResult}
   */
  public PhotonPipelineResult getConveyorCameraResult() {
    return conveyorCamera.getLatestResult();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    double limelightResult = getLimelightResult().getLatencyMillis() / 1000.0;
    double intakeCameraResult = getIntakeCameraResult().getLatencyMillis() / 1000.0;
    double conveyorCameraResult = getConveyorCameraResult().getLatencyMillis() / 1000.0;
    if (limelightResult > AlertContants.PHOTON_LATENCY
        || intakeCameraResult > AlertContants.PHOTON_LATENCY
        || conveyorCameraResult > AlertContants.PHOTON_LATENCY) {
      latencyAlert.set(true);
    } else {
      latencyAlert.set(false);
    }
  }
}
