package com.example.ui.viewmodel

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ARDownloaderApp
import com.example.data.model.AudioTrack
import com.example.data.model.DownloadItem
import com.example.data.model.DownloadStatus
import com.example.data.model.MediaQualityOption
import com.example.data.model.MediaType
import com.example.data.repository.DownloadRepository
import com.example.ui.state.AppSettings
import com.example.ui.state.MainUiState
import com.example.ui.state.PlayerState
import com.example.ui.state.ScreenTab
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(
    private val repository: DownloadRepository = ARDownloaderApp.instance.repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private var progressTrackingJob: Job? = null

    init {
        observeDownloads()
        loadDemoLibraryIfEmpty()
    }

    private fun observeDownloads() {
        viewModelScope.launch {
            repository.allDownloads.collect { list ->
                _uiState.update { it.copy(downloads = list) }
            }
        }
        viewModelScope.launch {
            repository.completedCount.collect { count ->
                _uiState.update { it.copy(completedCount = count) }
            }
        }
        viewModelScope.launch {
            repository.totalBytesDownloaded.collect { total ->
                _uiState.update { it.copy(totalDownloadedBytes = total) }
            }
        }
    }

    private fun loadDemoLibraryIfEmpty() {
        viewModelScope.launch {
            // Preload a few helpful high quality sample download links for the user to try right away
            if (_uiState.value.inputUrl.isBlank()) {
                _uiState.update { it.copy(inputUrl = "https://www.w3schools.com/html/mov_bbb.mp4") }
            }
        }
    }

    fun selectTab(tab: ScreenTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun onUrlChange(newUrl: String) {
        _uiState.update { it.copy(inputUrl = newUrl, analysisError = null) }
    }

    fun analyzeUrl(urlToAnalyze: String? = null) {
        val targetUrl = urlToAnalyze ?: _uiState.value.inputUrl
        if (targetUrl.isBlank()) {
            _uiState.update { it.copy(analysisError = "Please enter or paste a valid link") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isAnalyzing = true, analysisError = null, inputUrl = targetUrl) }
            val result = repository.analyzeUrl(targetUrl)
            result.onSuccess { media ->
                _uiState.update {
                    it.copy(
                        isAnalyzing = false,
                        analyzedMedia = media,
                        analysisError = null
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isAnalyzing = false,
                        analysisError = err.localizedMessage ?: "Failed to analyze media link"
                    )
                }
            }
        }
    }

    fun clearAnalysis() {
        _uiState.update { it.copy(analyzedMedia = null, analysisError = null) }
    }

    fun startDownload(option: MediaQualityOption) {
        val media = _uiState.value.analyzedMedia ?: return
        viewModelScope.launch {
            repository.startDownload(
                title = media.title,
                sourceUrl = media.originalUrl,
                option = option,
                platform = media.platform
            )
            _uiState.update {
                it.copy(
                    analyzedMedia = null,
                    snackbarMessage = "Download started: ${option.label}"
                )
            }
        }
    }

    fun quickDownloadVideo() {
        val media = _uiState.value.analyzedMedia ?: return
        val defaultVideo = media.formats.firstOrNull { it.mediaType == MediaType.VIDEO && it.resolutionOrBitrate == "720p" }
            ?: media.formats.firstOrNull { it.mediaType == MediaType.VIDEO }
        if (defaultVideo != null) {
            startDownload(defaultVideo)
        }
    }

    fun quickDownloadAudio() {
        val media = _uiState.value.analyzedMedia ?: return
        val defaultAudio = media.formats.firstOrNull { it.mediaType == MediaType.AUDIO && it.format == "mp3" }
            ?: media.formats.firstOrNull { it.mediaType == MediaType.AUDIO }
        if (defaultAudio != null) {
            startDownload(defaultAudio)
        }
    }

    fun pauseDownload(id: Long) {
        viewModelScope.launch { repository.pauseDownload(id) }
    }

    fun resumeDownload(id: Long) {
        viewModelScope.launch { repository.resumeDownload(id) }
    }

    fun cancelDownload(id: Long) {
        viewModelScope.launch { repository.cancelDownload(id) }
    }

    fun deleteDownload(id: Long) {
        viewModelScope.launch { repository.deleteDownload(id) }
    }

    fun retryDownload(id: Long) {
        viewModelScope.launch { repository.retryDownload(id) }
    }

    fun clearCompletedDownloads() {
        viewModelScope.launch { repository.clearCompleted() }
    }

    // Audio Player functionality
    fun playAudio(track: AudioTrack) {
        viewModelScope.launch {
            try {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null

                val file = File(track.filePath)
                if (file.exists() && file.length() > 0) {
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(track.filePath)
                        prepare()
                        start()
                        setOnCompletionListener {
                            _uiState.update {
                                it.copy(playerState = it.playerState.copy(isPlaying = false, currentPositionMs = 0L))
                            }
                        }
                    }
                    val totalMs = mediaPlayer?.duration?.toLong()?.coerceAtLeast(1000L) ?: track.durationMs
                    _uiState.update {
                        it.copy(
                            playerState = PlayerState(
                                currentTrack = track,
                                isPlaying = true,
                                currentPositionMs = 0L,
                                totalDurationMs = totalMs
                            )
                        )
                    }
                    startProgressTracker()
                } else {
                    // Provide a virtual audio stream playback experience for demonstration
                    _uiState.update {
                        it.copy(
                            playerState = PlayerState(
                                currentTrack = track,
                                isPlaying = true,
                                currentPositionMs = 0L,
                                totalDurationMs = track.durationMs
                            )
                        )
                    }
                    startVirtualProgressTracker(track.durationMs)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        playerState = PlayerState(
                            currentTrack = track,
                            isPlaying = true,
                            currentPositionMs = 0L,
                            totalDurationMs = track.durationMs
                        )
                    )
                }
                startVirtualProgressTracker(track.durationMs)
            }
        }
    }

    fun togglePlayPause() {
        val current = _uiState.value.playerState
        if (current.currentTrack == null) return

        if (mediaPlayer != null) {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.pause()
                _uiState.update { it.copy(playerState = current.copy(isPlaying = false)) }
            } else {
                mediaPlayer!!.start()
                _uiState.update { it.copy(playerState = current.copy(isPlaying = true)) }
                startProgressTracker()
            }
        } else {
            // Virtual toggle
            val newPlaying = !current.isPlaying
            _uiState.update { it.copy(playerState = current.copy(isPlaying = newPlaying)) }
            if (newPlaying) {
                startVirtualProgressTracker(current.totalDurationMs)
            }
        }
    }

    fun seekAudioTo(positionMs: Long) {
        val current = _uiState.value.playerState
        if (mediaPlayer != null) {
            try {
                mediaPlayer?.seekTo(positionMs.toInt())
            } catch (ignored: Exception) {}
        }
        _uiState.update { it.copy(playerState = current.copy(currentPositionMs = positionMs)) }
    }

    private fun startProgressTracker() {
        progressTrackingJob?.cancel()
        progressTrackingJob = viewModelScope.launch {
            while (isActive && mediaPlayer?.isPlaying == true) {
                val currentPos = mediaPlayer?.currentPosition?.toLong() ?: 0L
                _uiState.update {
                    it.copy(playerState = it.playerState.copy(currentPositionMs = currentPos))
                }
                delay(300)
            }
        }
    }

    private fun startVirtualProgressTracker(totalDuration: Long) {
        progressTrackingJob?.cancel()
        progressTrackingJob = viewModelScope.launch {
            while (isActive && _uiState.value.playerState.isPlaying) {
                val current = _uiState.value.playerState
                val nextPos = (current.currentPositionMs + 500L).coerceAtMost(totalDuration)
                if (nextPos >= totalDuration) {
                    _uiState.update { it.copy(playerState = current.copy(isPlaying = false, currentPositionMs = 0L)) }
                    break
                } else {
                    _uiState.update { it.copy(playerState = current.copy(currentPositionMs = nextPos)) }
                }
                delay(500)
            }
        }
    }

    // Browser navigation & media sniffing
    fun onBrowserUrlChange(url: String) {
        _uiState.update { it.copy(browserUrl = url) }
    }

    fun onMediaDetectedInBrowser(mediaUrl: String) {
        val list = _uiState.value.detectedMediaInBrowser.toMutableList()
        if (!list.contains(mediaUrl)) {
            list.add(0, mediaUrl)
            _uiState.update { it.copy(detectedMediaInBrowser = list.take(5)) }
        }
    }

    fun dismissSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    // Settings
    fun updateDefaultQuality(quality: String) {
        _uiState.update { it.copy(settings = it.settings.copy(defaultQuality = quality)) }
    }

    fun updateDefaultAudioFormat(format: String) {
        _uiState.update { it.copy(settings = it.settings.copy(defaultAudioFormat = format)) }
    }

    fun updateLanguage(lang: String) {
        _uiState.update { it.copy(settings = it.settings.copy(language = lang)) }
    }

    fun toggleTheme(isDark: Boolean) {
        _uiState.update { it.copy(settings = it.settings.copy(isDarkMode = isDark)) }
    }

    fun toggleWifiOnly(wifiOnly: Boolean) {
        _uiState.update { it.copy(settings = it.settings.copy(autoDownloadViaWifiOnly = wifiOnly)) }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
        progressTrackingJob?.cancel()
    }
}
