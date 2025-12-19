@echo off
echo Voice Clone App - Quick Setup and Build Script
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

sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0" --sdk_root=%ANDROID_SDK_ROOT%

echo.
echo Step 6: Accepting licenses...
sdkmanager --licenses --sdk_root=%ANDROID_SDK_ROOT%

echo.
echo Step 7: Building APK...
gradlew.bat assembleDebug

echo.
echo Build process completed!
echo.
echo If successful, your APK will be located at:
echo app\build\outputs\apk\debug\app-debug.apk
echo.
echo You can install it on your Android device by:
echo 1. Enabling Developer Options and USB Debugging
echo 2. Connecting your device via USB
echo 3. Copying the APK to your device
echo 4. Opening the APK file on your device to install

pause