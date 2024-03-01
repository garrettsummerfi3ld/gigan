package io.garrettsummerfi3ld.gigan;

public class RobotContainerTest {
  @test
  public void createRobotContainer() {
    RobotContainer container = new RobotContainer();
    assertNotNull(container);
  }
}
