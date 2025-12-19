package com.example.voicecloneapp.data

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.example.voicecloneapp.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AudioRepository(private val context: Context) {
    
    private val audioDirectory by lazy {
        File(context.filesDir, "recordings").apply {
            if (!exists()) mkdirs()
        }
    }
    
    private val generatedDirectory by lazy {
        File(context.filesDir, "generated").apply {
            if (!exists()) mkdirs()
        }
    }
    
    fun createRecordingFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "REC_$timestamp.wav"
        return File(audioDirectory, fileName)
    }
    
    fun createGeneratedAudioFile(extension: String = "wav"): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "GEN_$timestamp.$extension"
        return File(generatedDirectory, fileName)
    }
    
    fun getAllRecordings(): List<AudioFile> {
        return audioDirectory.listFiles { file ->
            file.extension.equals("wav", ignoreCase = true)
        }?.map { file ->
            AudioFile(
                id = file.nameWithoutExtension,
                title = file.name,
                duration = getFileDuration(file),
                filePath = file.absolutePath,
                dateCreated = Date(file.lastModified())
            )
        }?.sortedByDescending { it.dateCreated } ?: emptyList()
    }
    
    fun getAllGeneratedAudio(): List<AudioFile> {
        return generatedDirectory.listFiles { file ->
            file.extension.equals("wav", ignoreCase = true) ||
            file.extension.equals("mp3", ignoreCase = true)
        }?.map { file ->
            AudioFile(
                id = file.nameWithoutExtension,
                title = file.name,
                duration = getFileDuration(file),
                filePath = file.absolutePath,
                dateCreated = Date(file.lastModified())
            )
        }?.sortedByDescending { it.dateCreated } ?: emptyList()
    }
    
    fun deleteAudioFile(filePath: String): Boolean {
        return try {
            val file = File(filePath)
            if (file.exists()) {
                file.delete()
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("AudioRepository", "Error deleting file: $filePath", e)
            false
        }
    }
    
    fun getAudioFileUri(file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
    
    private fun getFileDuration(file: File): String {
        // Simplified duration calculation
        // In a real app, you would parse the audio file to get actual duration
        return try {
            val size = file.length()
            // Rough estimation: assuming 44.1kHz, 16-bit, mono WAV
            val durationSeconds = (size / (44100 * 2)).toInt()
            String.format("%d:%02d", durationSeconds / 60, durationSeconds % 60)
        } catch (e: Exception) {
            "0:00"
        }
    }
}

data class AudioFile(
    val id: String,
    val title: String,
    val duration: String,
    val filePath: String,
    val dateCreated: Date = Date()
)