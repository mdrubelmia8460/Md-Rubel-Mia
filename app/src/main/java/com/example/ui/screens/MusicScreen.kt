package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AudioTrack
import com.example.data.model.DownloadItem
import com.example.data.model.MediaType
import com.example.ui.state.PlayerState
import com.example.ui.theme.ButtonGoldGradient
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldGradient
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMetallic
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBlueDark
import com.example.ui.theme.RoyalBlueDeep
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.theme.RoyalBlueSurface
import com.example.ui.theme.SilverHighlight

@Composable
fun MusicScreen(
    downloads: List<DownloadItem>,
    playerState: PlayerState,
    onPlayTrack: (AudioTrack) -> Unit,
    onTogglePlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    // Collect downloaded audio items plus sample tracks
    val audioItems = downloads.filter { it.mediaType == MediaType.AUDIO }

    // Pre-populate demo music library if none downloaded yet
    val trackList = if (audioItems.isEmpty()) {
        listOf(
            AudioTrack(
                id = 101,
                title = "AR Royal Symphony (Gold Edition)",
                artist = "AR Orchestra",
                durationText = "03:45",
                durationMs = 225000L,
                filePath = "",
                format = "mp3"
            ),
            AudioTrack(
                id = 102,
                title = "Cyberwave Streaming Waveform",
                artist = "Sonic Horizon",
                durationText = "04:12",
                durationMs = 252000L,
                filePath = "",
                format = "m4a"
            ),
            AudioTrack(
                id = 103,
                title = "Deep Bass Acoustic Resonance",
                artist = "Studio Master",
                durationText = "02:50",
                durationMs = 170000L,
                filePath = "",
                format = "wav"
            ),
            AudioTrack(
                id = 104,
                title = "Golden Harmony & Velvet Melodies",
                artist = "Acoustic Gold",
                durationText = "03:15",
                durationMs = 195000L,
                filePath = "",
                format = "aac"
            )
        )
    } else {
        audioItems.map { item ->
            AudioTrack(
                id = item.id,
                title = item.title,
                artist = "${item.sourcePlatform} Audio",
                durationText = "03:30",
                durationMs = 210000L,
                filePath = item.filePath ?: "",
                format = item.format
            )
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(RoyalBlueDeep)
            .testTag("music_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Music Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = RoyalBlueDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldDark)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(RoyalBlueSurface)
                            .border(1.dp, GoldMetallic, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LibraryMusic,
                            contentDescription = null,
                            tint = GoldMetallic,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "AUDIO & MUSIC PLAYER",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "MP3 • M4A • WAV • AAC Hi-Fi Player",
                            fontSize = 11.sp,
                            color = SilverHighlight.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        // Active Player Card (Showcase with waveform visualizer)
        val activeTrack = playerState.currentTrack ?: trackList.firstOrNull()
        if (activeTrack != null) {
            item {
                ActivePlayerHeroCard(
                    track = activeTrack,
                    isPlaying = playerState.isPlaying,
                    currentPos = playerState.currentPositionMs,
                    totalDur = if (playerState.totalDurationMs > 0) playerState.totalDurationMs else activeTrack.durationMs,
                    onTogglePlay = {
                        if (playerState.currentTrack == null) {
                            onPlayTrack(activeTrack)
                        } else {
                            onTogglePlayPause()
                        }
                    },
                    onSeek = onSeek
                )
            }
        }

        // Playlist / Extracted Audios Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlaylistPlay,
                        contentDescription = null,
                        tint = GoldMetallic,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "MUSIC PLAYLIST (${trackList.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = "Tap to Play",
                    fontSize = 11.sp,
                    color = SilverHighlight.copy(alpha = 0.6f)
                )
            }
        }

        // Tracks List
        items(trackList, key = { it.id }) { track ->
            val isCurrent = playerState.currentTrack?.id == track.id
            AudioTrackRow(
                track = track,
                isCurrent = isCurrent,
                isPlaying = isCurrent && playerState.isPlaying,
                onClick = { onPlayTrack(track) }
            )
        }
    }
}

@Composable
private fun ActivePlayerHeroCard(
    track: AudioTrack,
    isPlaying: Boolean,
    currentPos: Long,
    totalDur: Long,
    onTogglePlay: () -> Unit,
    onSeek: (Long) -> Unit
) {
    val curSec = currentPos / 1000
    val totSec = (totalDur / 1000).coerceAtLeast(1)
    val curTime = String.format("%02d:%02d", curSec / 60, curSec % 60)
    val totTime = String.format("%02d:%02d", totSec / 60, totSec % 60)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("active_player_hero_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = RoyalBlueDark),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(GoldDark, GoldMetallic, GoldDark)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Disc Artwork with 3D Ring
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(RoyalBlueSurface, RoyalBlueDeep)
                        )
                    )
                    .border(2.dp, GoldMetallic, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Album,
                    contentDescription = null,
                    tint = GoldMetallic,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = track.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${track.artist} • ${track.format.uppercase()} High Fidelity",
                fontSize = 12.sp,
                color = GoldLight.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Seekbar
            Slider(
                value = currentPos.toFloat().coerceIn(0f, totalDur.toFloat()),
                onValueChange = { onSeek(it.toLong()) },
                valueRange = 0f..totalDur.toFloat().coerceAtLeast(1f),
                modifier = Modifier.fillMaxWidth().height(24.dp),
                colors = SliderDefaults.colors(
                    thumbColor = GoldMetallic,
                    activeTrackColor = GoldPrimary,
                    inactiveTrackColor = RoyalBlueDeep
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = curTime, fontSize = 11.sp, color = SilverHighlight.copy(alpha = 0.8f))
                Text(text = totTime, fontSize = 11.sp, color = SilverHighlight.copy(alpha = 0.8f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Big 3D Play/Pause Button
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(ButtonGoldGradient)
                    .clickable { onTogglePlay() }
                    .testTag("hero_play_toggle"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = RoyalBlueDeep,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun AudioTrackRow(
    track: AudioTrack,
    isCurrent: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isCurrent) RoyalBlueSurface else RoyalBlueDark)
            .border(
                1.dp,
                if (isCurrent) GoldMetallic else Color(0xFF1E325C),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (isCurrent) GoldDark else RoyalBlueDeep),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.GraphicEq else Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = GoldMetallic,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = track.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrent) GoldLight else Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${track.artist} • ${track.format.uppercase()}",
                    fontSize = 11.sp,
                    color = SilverHighlight.copy(alpha = 0.7f)
                )
            }
        }

        Text(
            text = track.durationText,
            fontSize = 11.sp,
            color = if (isCurrent) GoldMetallic else SilverHighlight.copy(alpha = 0.6f)
        )
    }
}
