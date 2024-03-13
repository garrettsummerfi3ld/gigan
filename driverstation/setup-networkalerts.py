import os
import platform

import urllib.request

# Define the URL of the .JAR file
jar_url = "https://github.com/Mechanical-Advantage/NetworkAlerts/releases/download/v1.0.0/NetworkAlerts.jar"

# Define the destination directory
plugins_dir = os.path.expanduser("~/Shuffleboard/plugins")

# Create the plugins directory if it doesn't exist
os.makedirs(plugins_dir, exist_ok=True)

# Download the .JAR file
jar_filename = os.path.basename(jar_url)
jar_path = os.path.join(plugins_dir, jar_filename)
urllib.request.urlretrieve(jar_url, jar_path)

# Print the path to the downloaded .JAR file
print(f"Downloaded {jar_filename} to {jar_path}")