


## Setup Wireless Connectivity
1. Add %LOCALAPPDATA%\Android\Sdk\platform-tools to the $PATH variable in the environment settings.
2. Connect Control to Computer via USB and WIFI
3. Start Android Studio and open your bots code base. Wait for Grabble to finish.
4. Start the terminal in Android Studio (Bottom left)
5. Type command "adb tcpip 5555"

## Connect to Bot
1. Start Android Studio and open your bots code base. Wait for Grabble to finish.
2. Connect to Control Hub over WIFI.
3. Start the terminal in Android Studio (Bottom left)
4. Type command "adb connect 192.168.43.1:5555"

The Rev Robotics Control Hub v1.0 will appear at the top.



## Diagnostics Page:
After connecting to the robot you can visit the diag page over http://192.168.43.1:8080
