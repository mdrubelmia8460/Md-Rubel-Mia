package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MediaType {
    VIDEO,
    AUDIO
}

enum class DownloadStatus {
    IDLE,
    DOWNLOADING,
    PAUSED,
    COMPLETED,
    FAILED,
    CANCELLED
}

@Entity(tableName = "download_items")
data class DownloadItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val sourceUrl: String,
    val downloadUrl: String,
    val thumbnail: String? = null,
    val mediaType: MediaType = MediaType.VIDEO,
    val quality: String = "720p", // 1080p, 720p, 480p, 360p, MP3, M4A, etc.
    val format: String = "mp4", // mp4, webm, mp3, m4a, wav, aac
    val filePath: String? = null,
    val totalBytes: Long = 0L,
    val downloadedBytes: Long = 0L,
    val progress: Int = 0, // 0 - 100
    val speedBps: Long = 0L,
    val status: DownloadStatus = DownloadStatus.IDLE,
    val errorMessage: String? = null,
    val durationSeconds: Long = 0L,
    val sourcePlatform: String = "Web", // YouTube, Facebook, TikTok, Instagram, Vimeo, X, Direct
    val createdAt: Long = System.currentTimeMillis()
)

data class MediaQualityOption(
    val label: String,        // "1080p Full HD", "720p HD", "480p SD", "MP3 320kbps"
    val resolutionOrBitrate: String, // "1080p", "720p", "320kbps"
    val format: String,       // "mp4", "webm", "mp3", "m4a", "wav", "aac"
    val mediaType: MediaType,
    val estimatedSizeBytes: Long,
    val downloadUrl: String,
    val isAudioExtraction: Boolean = false
)

data class AnalyzedMedia(
    val title: String,
    val originalUrl: String,
    val platform: String,
    val author: String = "AR Media",
    val duration: String = "03:45",
    val durationSeconds: Long = 225L,
    val thumbnail: String? = null,
    val formats: List<MediaQualityOption> = emptyList(),
    val description: String = ""
)

data class AudioTrack(
    val id: Long,
    val title: String,
    val artist: String = "AR Audio",
    val durationText: String = "03:45",
    val durationMs: Long = 225000L,
    val filePath: String,
    val albumArt: String? = null,
    val format: String = "mp3"
)
