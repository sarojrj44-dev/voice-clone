@echo off
echo Voice Clone App - Emulator Setup and Run Script
echo =================================================

echo Step 1: Checking for Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Java is not installed. Please install JDK 11 or higher.
    echo Download from: https://adoptium.net/
    pause
    exit /b
) else (
    echo Java is installed.
    java -version
)

echo.
echo Step 2: Downloading Android SDK Command Line Tools...
echo This may take several minutes...

powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://dl.google.com/android/repository/commandlinetools-win-9477386_latest.zip' -OutFile 'commandlinetools.zip'}"

echo.
echo Step 3: Extracting SDK...
mkdir android-sdk >nul 2>&1
powershell -Command "Expand-Archive -Path 'commandlinetools.zip' -DestinationPath 'android-sdk'"

echo.
echo Step 4: Setting up SDK...
set ANDROID_SDK_ROOT=%cd%\android-sdk
set PATH=%PATH%;%ANDROID_SDK_ROOT%\cmdline-tools\latest\bin

echo.
echo Step 5: Installing required SDK components...
echo This may take several minutes...

sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0" "emulator" "system-images;android-34;google_apis;x86_64" --sdk_root=%ANDROID_SDK_ROOT%

echo.
echo Step 6: Accepting licenses...
sdkmanager --licenses --sdk_root=%ANDROID_SDK_ROOT%

echo.
echo Step 7: Creating Android Virtual Device (AVD)...
echo no | avdmanager create avd -n VoiceCloneEmulator -k "system-images;android-34;google_apis;x86_64" --force

echo.
echo Step 8: Building APK...
gradlew.bat assembleDebug

echo.
echo Step 9: Starting Emulator and Installing App...
start /min cmd /c "%ANDROID_SDK_ROOT%\emulator\emulator.exe -avd VoiceCloneEmulator -netdelay none -netspeed full"
timeout /t 30 /nobreak >nul

echo.
echo Installing APK on emulator...
adb install app\build\outputs\apk\debug\app-debug.apk

echo.
echo Setup and installation completed!
echo.
echo The emulator should now be running with your Voice Clone App installed.
echo If the app doesn't appear immediately, check the app drawer in the emulator.
echo.
echo You can also manually start the emulator later with:
echo %ANDROID_SDK_ROOT%\emulator\emulator.exe -avd VoiceCloneEmulator
echo.
echo And manually install the APK with:
echo adb install app\build\outputs\apk\debug\app-debug.apk

pause