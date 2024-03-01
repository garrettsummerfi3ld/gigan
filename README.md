# gigan

A 2024 Cresendo FRC robot written in Java using GradleRIO and WPILib. Focus on stability and rich diagnostics for driver operation.

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

[TBD]

## Requirements

- WPILib 2024.3.1
- Internet connection (for Gradle to download dependencies)
