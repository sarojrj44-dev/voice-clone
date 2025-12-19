package com.example.voicecloneapp

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.voicecloneapp.audio.AudioRecorder
import com.example.voicecloneapp.data.AudioFile
import com.example.voicecloneapp.data.AudioRepository
import com.example.voicecloneapp.service.VoiceCloningService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun VoiceCloneApp() {
    var scriptText by remember { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }
    var generatedAudioList by remember { mutableStateOf<List<AudioFile>>(emptyList()) }
    var recordedVoiceFile by remember { mutableStateOf<File?>(null) }
    
    val context = LocalContext.current
    val audioRepository = remember { AudioRepository(context) }
    val voiceCloningService = remember { VoiceCloningService(context) }
    val audioRecorder = remember { AudioRecorder(audioRepository.createRecordingFile()) }
    
    // Load existing generated audio files
    LaunchedEffect(Unit) {
        generatedAudioList = audioRepository.getAllGeneratedAudio()
    }
    
    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    
    // Check and request permission
    fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    fun requestPermission() {
        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
    
    fun toggleRecording() {
        if (checkPermission()) {
            isRecording = !isRecording
            if (isRecording) {
                // Start recording
                recordedVoiceFile = audioRepository.createRecordingFile()
                audioRecorder.startRecording(CoroutineScope(Dispatchers.Main))
            } else {
                // Stop recording
                audioRecorder.stopRecording()
                Toast.makeText(context, "Recording saved", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermission()
        }
    }
    
    fun generateAudioFromText() {
        if (scriptText.isBlank()) return
        
        Toast.makeText(context, "Generating audio...", Toast.LENGTH_SHORT).show()
        
        // Split text into chunks for long texts
        val chunks = voiceCloningService.splitTextIntoChunks(scriptText)
        
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val audioFiles = mutableListOf<File>()
                
                // Generate audio for each chunk
                for ((index, chunk) in chunks.withIndex()) {
                    val outputFile = audioRepository.createGeneratedAudioFile("wav")
                    val result = voiceCloningService.generateSpeechFromText(
                        chunk,
                        recordedVoiceFile,
                        outputFile,
                        this
                    )
                    
                    if (result.isSuccess) {
                        audioFiles.add(result.getOrNull()!!)
                    }
                }
                
                // If we have multiple chunks, stitch them together
                val finalAudioFile = if (audioFiles.size > 1) {
                    val stitchedFile = audioRepository.createGeneratedAudioFile("wav")
                    val stitchResult = voiceCloningService.stitchAudioFiles(audioFiles, stitchedFile, this)
                    if (stitchResult.isSuccess) {
                        stitchResult.getOrNull()!!
                    } else {
                        audioFiles.firstOrNull()
                    }
                } else {
                    audioFiles.firstOrNull()
                }
                
                // Convert to MP3 if needed
                // val mp3File = audioRepository.createGeneratedAudioFile("mp3")
                // voiceCloningService.convertToMp3(finalAudioFile!!, mp3File, this)
                
                if (finalAudioFile != null) {
                    Toast.makeText(context, "Audio generated successfully!", Toast.LENGTH_SHORT).show()
                    // Refresh the list
                    generatedAudioList = audioRepository.getAllGeneratedAudio()
                } else {
                    Toast.makeText(context, "Failed to generate audio", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Voice Clone App",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Record Your Voice",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { toggleRecording() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(if (isRecording) "Stop Recording" else "Start Recording")
                    }
                }
                
                if (recordedVoiceFile != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Last recording: ${recordedVoiceFile!!.name}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Generate Audio from Script",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = scriptText,
                    onValueChange = { scriptText = it },
                    label = { Text("Enter your script here") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 6
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { generateAudioFromText() },
                        modifier = Modifier.weight(1f),
                        enabled = scriptText.isNotBlank()
                    ) {
                        Text("Generate WAV")
                    }
                    
                    Button(
                        onClick = { 
                            // TODO: Implement MP3 export
                            generateAudioFromText()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = scriptText.isNotBlank()
                    ) {
                        Text("Export MP3")
                    }
                }
            }
        }
        
        if (generatedAudioList.isNotEmpty()) {
            Text(
                text = "Generated Audio Files",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            
            LazyColumn {
                items(generatedAudioList) { audioFile ->
                    AudioFileItem(audioFile = audioFile)
                }
            }
        }
    }
}

@Composable
fun AudioFileItem(audioFile: AudioFile) {
    var isPlaying by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = audioFile.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${audioFile.duration} • ${audioFile.dateCreated}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Button(onClick = { isPlaying = !isPlaying }) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null
                )
            }
        }
    }
}
