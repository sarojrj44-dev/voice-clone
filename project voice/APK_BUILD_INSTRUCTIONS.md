# APK Build Instructions for Voice Clone App

Since Android development tools are not currently installed on this system, this document provides detailed instructions on how to build the APK for the Voice Clone App.

## Prerequisites

Before building the APK, you'll need to install the required development tools:

### Option 1: Install Android Studio (Recommended)

1. **Download Android Studio**
   - Visit [developer.android.com/studio](https://developer.android.com/studio)
   - Download the latest version of Android Studio

2. **Install Android Studio**
   - Run the installer and follow the setup wizard
   - During installation, make sure to install the Android SDK and Android Virtual Device (AVD)

3. **Launch Android Studio**
   - Open Android Studio after installation completes

### Option 2: Install Command-Line Tools Only

If you prefer not to install the full Android Studio IDE:

1. **Download Command Line Tools**
   - Visit [developer.android.com/studio#command-tools](https://developer.android.com/studio#command-tools)
   - Download the "Command line tools only" package for Windows

2. **Extract and Set Up**
   - Extract the downloaded ZIP file to a directory (e.g., `C:\android\sdk`)
   - Navigate to the `cmdline-tools\latest\bin` directory
   - Run the SDK manager to install required packages:
     ```
     sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
     ```

## Building the APK

### Method 1: Using Android Studio (Easiest)

1. **Open the Project**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the project directory (`c:\Users\97798\Desktop\project voice`) and select it

2. **Sync Project with Gradle Files**
   - Android Studio will automatically prompt to sync
   - If not, click "File" → "Sync Project with Gradle Files"

3. **Build the APK**
   - Click "Build" → "Build Bundle(s) / APK(s)" → "Build APK(s)"
   - Wait for the build process to complete
   - Once finished, Android Studio will show a notification with a link to locate the APK

4. **Locate the APK**
   - The APK will be located at:
     ```
     app/build/outputs/apk/debug/app-debug.apk
     ```

### Method 2: Using Command Line

If you have the Android SDK command-line tools installed:

1. **Navigate to Project Directory**
   ```
   cd "c:\Users\97798\Desktop\project voice"
   ```

2. **Build Debug APK**
   ```
   # On Windows
   gradlew.bat assembleDebug
   
   # On Unix/Mac
   ./gradlew assembleDebug
   ```

3. **Locate the APK**
   - The APK will be generated at:
     ```
     app/build/outputs/apk/debug/app-debug.apk
     ```

### Method 3: Using Android Studio Terminal

1. **Open Terminal in Android Studio**
   - In Android Studio, click "View" → "Tool Windows" → "Terminal"

2. **Run Build Command**
   ```
   ./gradlew assembleDebug
   ```

## APK Types

The project can generate two types of APKs:

### Debug APK
- Located at: `app/build/outputs/apk/debug/app-debug.apk`
- Automatically signed with debug key
- Suitable for testing and development
- Cannot be published to Google Play Store

### Release APK
- Requires signing with a release key
- Suitable for distribution
- Can be published to Google Play Store

To build a release APK:

1. **Generate Signed Bundle/APK**
   - In Android Studio, click "Build" → "Generate Signed Bundle / APK"
   - Select "APK" and click "Next"
   - Create a new keystore or use an existing one
   - Follow the wizard to complete the process

## Troubleshooting

### Common Build Issues

1. **Gradle Sync Failed**
   - Ensure stable internet connection
   - Check proxy settings if behind corporate firewall
   - Try "File" → "Invalidate Caches and Restart"

2. **Missing SDK Components**
   - Open SDK Manager in Android Studio
   - Install required SDK platforms and tools

3. **Insufficient Memory**
   - Increase heap size in `gradle.properties`:
     ```
     org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
     ```

4. **Compilation Errors**
   - Check the Messages tab in Android Studio for detailed error information
   - Ensure all dependencies are properly downloaded

### Runtime Issues

1. **App Crashes on Launch**
   - Check logcat output in Android Studio
   - Verify all permissions are properly declared in `AndroidManifest.xml`

2. **Recording Not Working**
   - Ensure microphone permissions are granted
   - Test on a physical device (emulators may have limitations)

## Distribution

### Installing APK on Device

1. **Enable Developer Options**
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times

2. **Enable USB Debugging**
   - Go to Settings → Developer Options
   - Enable "USB Debugging"

3. **Install APK**
   - Connect device via USB
   - Copy APK to device storage
   - Open file manager on device and tap the APK file
   - Allow installation from unknown sources if prompted

### Publishing to Google Play Store

1. **Prepare for Release**
   - Update version code and name in `app/build.gradle`
   - Add proper app icons and screenshots
   - Write compelling app description

2. **Generate Signed Release APK**
   - Follow the signing process described above

3. **Create Google Play Developer Account**
   - Visit [play.google.com/console](https://play.google.com/console)
   - Pay registration fee ($25 one-time)

4. **Upload App**
   - Create new application
   - Upload signed APK and required assets
   - Complete store listing information
   - Submit for review

## Additional Notes

- The app requires Android 7.0 (API level 24) or higher
- Internet permission is required for potential voice cloning API integration
- Microphone permission is required for voice recording
- Storage permissions are required for saving generated audio files

## Support

For issues with building or running the app:
1. Check Android Studio logs for error messages
2. Verify all prerequisites are installed
3. Consult the documentation files in the project directory
4. Search online for specific error messages