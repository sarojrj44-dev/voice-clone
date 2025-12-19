# Voice Clone App

An Android application that allows you to record your voice, clone it, and generate realistic audio from text scripts.

## Features

- Voice recording capability
- Voice cloning technology
- Text-to-speech generation with your cloned voice
- Automatic chunking and stitching of long texts (20-30 minutes)
- Export to MP3/WAV formats
- High-quality audio generation

## Architecture

The app is built with:
- Kotlin
- Jetpack Compose for UI
- Android Architecture Components
- Coroutines for asynchronous operations
- Media APIs for audio processing

## Setup Instructions

1. Install Android Studio
2. Open the project in Android Studio
3. Sync the project with Gradle files
4. Build and run the app on an Android device or emulator

## How to Use

1. **Record Your Voice**
   - Tap the "Start Recording" button
   - Speak naturally for 30-60 seconds
   - Tap "Stop Recording" when finished

2. **Generate Audio from Text**
   - Paste your script in the text field
   - Tap "Generate WAV" or "Export MP3"
   - Wait for the generation to complete

3. **Listen to Generated Audio**
   - Your generated audio files will appear in the list below
   - Tap the play button to listen to any file

## Technical Implementation

### Voice Recording
The app uses Android's `AudioRecord` API to capture high-quality audio from the device's microphone. The recorded audio is saved as WAV files.

### Voice Cloning
The voice cloning functionality simulates integration with cloud-based voice cloning services. In a production environment, this would connect to services like:
- ElevenLabs API
- Google Cloud Text-to-Speech with voice cloning
- Microsoft Azure Cognitive Services
- Amazon Polly Neural TTS

### Text Processing
Long texts are automatically split into manageable chunks for processing, then stitched back together to create seamless audio of 20-30 minutes duration.

### Audio Generation
The app generates high-quality audio by:
1. Converting text to speech using the cloned voice
2. Automatically chunking long texts
3. Stitching audio segments together
4. Exporting in WAV or MP3 format

## Dependencies

- Kotlin Coroutines
- AndroidX Libraries
- Jetpack Compose
- Media3 ExoPlayer (for future audio playback)

## Future Enhancements

- Integration with actual voice cloning APIs
- Advanced audio processing and noise reduction
- Custom voice training capabilities
- Cloud synchronization of voice profiles
- Enhanced audio editing features

## Permissions

The app requires the following permissions:
- RECORD_AUDIO: To record your voice
- INTERNET: To communicate with voice cloning services
- WRITE_EXTERNAL_STORAGE: To save generated audio files (API < 29)
- READ_EXTERNAL_STORAGE: To access saved audio files (API < 33)

## Support

For issues, feature requests, or contributions, please open an issue on the project repository.