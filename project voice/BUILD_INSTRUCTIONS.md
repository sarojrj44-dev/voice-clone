# Voice Clone App - Build Instructions

## Prerequisites

Before building the Voice Clone App, ensure you have the following installed:

1. **Android Studio** (latest version recommended)
2. **Android SDK** (API level 34 or higher)
3. **JDK 11** or higher
4. **Git** (optional, for version control)

## Project Structure

The project follows the standard Android project structure:

```
project voice/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/voicecloneapp/
│   │   │   │   ├── audio/           # Audio recording components
│   │   │   │   ├── data/            # Data models and repository
│   │   │   │   ├── service/         # Voice cloning services
│   │   │   │   ├── ui/theme/        # UI theme components
│   │   │   │   └── MainActivity.kt  # Main activity
│   │   │   └── res/                 # Resources
│   │   └── build.gradle.kts         # App module build config
│   └── build.gradle.kts             # Project level build config
├── gradle/
│   └── wrapper/                     # Gradle wrapper files
├── build.gradle.kts                 # Top-level build file
├── gradle.properties               # Gradle properties
├── gradlew                         # Gradle wrapper script (Unix)
├── gradlew.bat                     # Gradle wrapper script (Windows)
├── settings.gradle.kts             # Project settings
└── README.md                       # Project documentation
```

## Building the Project

### Method 1: Using Android Studio (Recommended)

1. **Open the Project**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the project directory and select it

2. **Sync Project with Gradle Files**
   - Android Studio will automatically prompt to sync
   - If not, click "File" → "Sync Project with Gradle Files"

3. **Build the Project**
   - Click "Build" → "Make Project" or press `Ctrl+F9` (Windows/Linux) or `Cmd+F9` (Mac)

4. **Run the App**
   - Connect an Android device or start an emulator
   - Click "Run" → "Run 'app'" or press `Shift+F10` (Windows/Linux) or `Ctrl+R` (Mac)

### Method 2: Using Command Line

1. **Navigate to Project Directory**
   ```bash
   cd "c:\Users\97798\Desktop\project voice"
   ```

2. **Build the Project**
   ```bash
   # On Windows
   gradlew.bat build
   
   # On Unix/Mac
   ./gradlew build
   ```

3. **Install Debug APK to Device**
   ```bash
   # On Windows
   gradlew.bat installDebug
   
   # On Unix/Mac
   ./gradlew installDebug
   ```

## Key Components Explained

### 1. Audio Recording (`audio/AudioRecorder.kt`)

The `AudioRecorder` class handles capturing audio from the device's microphone:
- Uses Android's `AudioRecord` API
- Records in WAV format at 44.1kHz, 16-bit, mono
- Implements coroutine-based recording for non-blocking operation

### 2. Voice Cloning Service (`service/VoiceCloningService.kt`)

The `VoiceCloningService` provides:
- Text chunking for long scripts
- Audio stitching for combining chunks
- WAV/MP3 export functionality
- Framework for integrating with actual voice cloning APIs

### 3. Data Management (`data/AudioRepository.kt`)

The `AudioRepository` manages:
- Storage of recorded voice samples
- Storage of generated audio files
- File organization and cleanup
- URI generation for sharing files

### 4. User Interface (`VoiceCloneApp.kt`)

The main UI provides:
- Voice recording controls
- Text input for scripts
- Audio generation buttons
- List of generated audio files

## Integrating with Voice Cloning APIs

The app includes a framework for integrating with commercial voice cloning services. See `VOICE_CLONING_INTEGRATION.md` for detailed instructions on connecting with:

- ElevenLabs API
- Google Cloud Text-to-Speech
- Microsoft Azure Cognitive Services

## Customization Options

### Changing Audio Quality

Modify the constants in `AudioRecorder.kt`:
```kotlin
private val sampleRate = 44100  // Change sample rate
private val channelConfig = AudioFormat.CHANNEL_IN_MONO  // Change channels
private val audioFormat = AudioFormat.ENCODING_PCM_16BIT  // Change bit depth
```

### Adjusting Text Chunking

Modify the chunk size in `VoiceCloningService.kt`:
```kotlin
fun splitTextIntoChunks(text: String, maxChunkLength: Int = 1000): List<String>
```

### Updating UI Theme

Modify colors in `ui/theme/Color.kt` and `ui/theme/Theme.kt`.

## Troubleshooting

### Common Build Issues

1. **Gradle Sync Failed**
   - Ensure Android Studio is updated
   - Check internet connection for dependency downloads
   - Try "File" → "Invalidate Caches and Restart"

2. **Missing SDK Components**
   - Open SDK Manager in Android Studio
   - Install required SDK platforms and tools

3. **Permission Errors**
   - Ensure all required permissions are granted
   - Check `AndroidManifest.xml` for missing permissions

### Runtime Issues

1. **Recording Not Working**
   - Check microphone permissions
   - Verify device has a working microphone

2. **Audio Generation Fails**
   - Ensure proper integration with voice cloning service
   - Check internet connectivity for API calls

## Testing

The app includes basic UI tests and unit tests:
- Run tests using "Run" → "Run All Tests"
- Add custom tests in `app/src/test/` and `app/src/androidTest/`

## Deployment

To generate a release build:

1. **Create Signing Key**
   - "Build" → "Generate Signed Bundle / APK"
   - Follow the wizard to create a new key store

2. **Build Release APK**
   - Select "APK" option
   - Choose your signing key
   - Click "Finish"

3. **Distribute**
   - Upload to Google Play Store
   - Or distribute APK directly to users

## Support

For issues with building or running the app:
1. Check the Android Studio logs for error messages
2. Verify all prerequisites are installed
3. Consult the README.md and documentation files
4. Search online for specific error messages

## Contributing

To contribute to the project:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request