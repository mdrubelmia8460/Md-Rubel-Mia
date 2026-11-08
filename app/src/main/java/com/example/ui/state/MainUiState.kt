package com.example.ui.state

import com.example.data.model.AnalyzedMedia
import com.example.data.model.AudioTrack
import com.example.data.model.DownloadItem

enum class ScreenTab {
    HOME,
    BROWSER,
    DOWNLOADS,
    MUSIC,
    SETTINGS
}

data class AppSettings(
    val defaultQuality: String = "720p HD",
    val defaultAudioFormat: String = "MP3",
    val language: String = "en", // "en" or "bn" (Bengali)
    val isDarkMode: Boolean = true,
    val autoDownloadViaWifiOnly: Boolean = false,
    val saveFolder: String = "Internal / AR_Media"
)

data class PlayerState(
    val currentTrack: AudioTrack? = null,
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val totalDurationMs: Long = 0L
)

data class MainUiState(
    val currentTab: ScreenTab = ScreenTab.HOME,
    val inputUrl: String = "",
    val isAnalyzing: Boolean = false,
    val analyzedMedia: AnalyzedMedia? = null,
    val analysisError: String? = null,
    val downloads: List<DownloadItem> = emptyList(),
    val completedCount: Int = 0,
    val totalDownloadedBytes: Long = 0L,
    val playerState: PlayerState = PlayerState(),
    val settings: AppSettings = AppSettings(),
    val browserUrl: String = "https://www.google.com",
    val detectedMediaInBrowser: List<String> = emptyList(),
    val snackbarMessage: String? = null
)
