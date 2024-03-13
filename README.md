# gigan

A 2024 Crescendo FRC robot written in Java using GradleRIO and WPILib. Focus on stability and rich diagnostics for driver operation.

## Build Status

| Action            | Status                                                                                                                                                                                                                           |
| ----------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| CI                | [![CI](https://github.com/garrettsummerfi3ld/gigan/actions/workflows/ci.yml/badge.svg)](https://github.com/garrettsummerfi3ld/gigan/actions/workflows/ci.yml)                                                                    |
| Qodana            | [![Qodana](https://github.com/garrettsummerfi3ld/gigan/actions/workflows/qodana.yml/badge.svg)](https://github.com/garrettsummerfi3ld/gigan/actions/workflows/qodana.yml)                                                        |
| CodeQL            | [![CodeQL Scanning](https://github.com/garrettsummerfi3ld/gigan/actions/workflows/codeql.yml/badge.svg)](https://github.com/garrettsummerfi3ld/gigan/actions/workflows/codeql.yml)                                               |
| Spotless          | [![Syntax Check](https://github.com/garrettsummerfi3ld/gigan/actions/workflows/syntax-check.yml/badge.svg)](https://github.com/garrettsummerfi3ld/gigan/actions/workflows/syntax-check.yml)                                      |
| Gradle Validation | [![Validate Gradle Wrapper](https://github.com/garrettsummerfi3ld/gigan/actions/workflows/gradle-wrapper-validation.yml/badge.svg)](https://github.com/garrettsummerfi3ld/gigan/actions/workflows/gradle-wrapper-validation.yml) |

## How to use

Download the repository and prep for deployment.

```bash
git clone https://github.com/garrettsummerfi3ld/gigan.git

# On macOS/Linux
chmod +x ./gradlew

# To build and load dependencies
./gradlew build
```

Ensure you are connected to a roboRIO via Wi-Fi or USB direct connect.

> [!TIP]
>
> It is recommended to open up Visual Studio Code with WPILib or [WPILib's distribution of VS Code](https://github.com/wpilibsuite/allwpilib) for easier access to diagnostic tools and console logs, as well as GradleRIO being default included.

Deploy the software to the roboRIO.

```bash
./gradlew deploy
```

_If you are using the WPILib extension:_

- `Ctrl+Shift+P` (Windows/Linux) / `Command(⌘)+Shift+P` (macOS)
- WPILib: Deploy Robot Code

> [!NOTE]
>
> You can also access commonly used WPILib commands when clicking on the WPILib icon on an open file, this only shows up if you have WPILib VS Code opened

### Competition use

> [!WARNING]
>
> You should always before a competition to create an `event` branch, to separate the new code written and not dirty up the main branch. You will need to install [Event Deploy for WPILib](https://marketplace.visualstudio.com/items?itemName=Mechanical-Advantage.event-deploy-wpilib) from the VS Code marketplace.

To create an `event` branch, open up the repository in either GitHub Desktop, VS Code, or a terminal.

It is also recommended to have a naming convention of `event/[name-of-event]` where all changes are committed to that branch during an event.

If you were to have multiple programmers present at an event, you can also have another naming convention of `event/[name-of-event]/[programmer]`.

### VS Code

In the "Source Control" tab, go to the branches tab and click on the "+" button to create a branch.

Name the branch accordingly.

You can also switch branches with the same process.

### GitHub Desktop

Click on the current branch name and click on the "New Branch" button.

Name the branch accordingly.

You can also switch branches with the same process.

### Terminal

Browse to your project in the terminal, with `cd` commands to search around. Once in the root directory of the project, input the following commands:

```bash
# Creates and checks out a new branch under the name provided
git checkout -b event/[name-of-event-here]
```

Name the branch accordingly.

To switch branches back and forth input the following into the terminal:

```bash
git checkout [branch-name-here]
```

## Features

### Code features and quality gates

- Preconfigured setup for GitHub Actions (helpful for [CI/CD](https://en.wikipedia.org/wiki/CI/CD))
  - Build action for building the robot code (helpful for ensuring code compiles)
  - [Qodana](https://www.jetbrains.com/qodana/) action for static analysis (helpful for finding bugs and code smells)
  - [CodeQL](https://codeql.github.com/) action for static analysis and security scanning (helpful for finding bugs and security vulnerabilities)
  - [Spotless](https://github.com/diffplug/spotless) enforcement action for code formatting (helpful for keeping code cleanly formatted after commits)
  - [Gradle Validation](https://github.com/gradle/wrapper-validation-action/tree/v1/?tab=readme-ov-file#the-gradle-wrapper-problem-in-open-source) action for validating the Gradle wrapper (helpful for ensuring [supply chain](https://en.wikipedia.org/wiki/Supply_chain_attack) security)
- Preconfigured setup for [Command-Based Robot](https://docs.wpilib.org/en/stable/docs/software/commandbased/index.html) projects (helpful for getting started)
- [Dependabot](https://docs.github.com/en/code-security/dependabot) for dependency updates (helpful for keeping dependencies up to date)
- Preconfigured setup for [Spotless](https://github.com/diffplug/spotless) inside of Gradle (helpful for keeping code cleanly formatted during development)

### Robot features

- [YAGSL](https://github.com/BroncBotz3481/YAGSL) is used for the swerve drivetrain
- [AdvantageKit](https://github.com/Mechanical-Advantage/AdvantageKit) logging and diagnostics
  - [Unofficial Rev CAN Logger](https://github.com/Mechanical-Advantage/URCL) (URCL) to perform additional logging
  - Power Distribution logging for power status during robot runtime
  - Writing all logs directly to a USB Drive connected to the RoboRIO
- [PhotonVision](https://photonvision.org/) setup for performing autonomous tasks with Note detection
- [PathPlanner](https://github.com/mjansen4857/pathplanner) to run semi-autonomous or full autonomous tasks
- [NetworkAlerts](https://github.com/Mechanical-Advantage/NetworkAlerts) support for any alerts relating to the operation of the robot
  - To install NetworkAlerts for Shuffleboard, run `py driverstation/install-networkalerts.py` on any computer.

## Requirements

- WPILib 2024.3.1
- Internet connection (for Gradle to download dependencies)
- NetworkAlerts (For Shuffleboard/FRC Web Components)
  - Automatic installation requires [Python 3](https://www.python.org/) to be installed
