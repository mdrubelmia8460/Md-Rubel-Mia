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
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AudioTrack
import com.example.data.model.DownloadItem
import com.example.data.model.MediaQualityOption
import com.example.data.model.MediaType
import com.example.ui.components.ARBrandHeader
import com.example.ui.components.DownloadItemCard
import com.example.ui.components.DownloadStatsCard
import com.example.ui.components.MediaAnalysisResultCard
import com.example.ui.components.SupportedPlatformsRow
import com.example.ui.components.VideoUrlInputCard
import com.example.ui.state.MainUiState
import com.example.ui.theme.ButtonGoldGradient
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldGradient
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMetallic
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBlueDark
import com.example.ui.theme.RoyalBlueDeep
import com.example.ui.theme.RoyalBlueLight
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.theme.RoyalBlueSurface
import com.example.ui.theme.SilverHighlight

@Composable
fun HomeScreen(
    uiState: MainUiState,
    onUrlChange: (String) -> Unit,
    onAnalyze: () -> Unit,
    onDownloadSelected: (MediaQualityOption) -> Unit,
    onCloseAnalysis: () -> Unit,
    onSelectPlatform: (String) -> Unit,
    onPauseDownload: (Long) -> Unit,
    onResumeDownload: (Long) -> Unit,
    onCancelDownload: (Long) -> Unit,
    onDeleteDownload: (Long) -> Unit,
    onRetryDownload: (Long) -> Unit,
    onPlayMedia: (DownloadItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Brand Header
        item {
            ARBrandHeader()
        }

        // 2. Hero 3D Showcase Banner
        item {
            HeroShowcaseBanner(
                onQuickTry = {
                    onUrlChange("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4")
                    onAnalyze()
                }
            )
        }

        // 3. Paste URL & Analyze Input
        item {
            VideoUrlInputCard(
                url = uiState.inputUrl,
                isAnalyzing = uiState.isAnalyzing,
                onUrlChange = onUrlChange,
                onAnalyze = onAnalyze
            )
        }

        // 4. Analysis Error notification if any
        if (uiState.analysisError != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("analysis_error_banner"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF7F1D1D).copy(alpha = 0.8f))
                ) {
                    Text(
                        text = uiState.analysisError,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // 5. Analysis Result Card (Video & Audio Formats Selection)
        if (uiState.analyzedMedia != null) {
            item {
                MediaAnalysisResultCard(
                    media = uiState.analyzedMedia,
                    onDownloadSelected = onDownloadSelected,
                    onClose = onCloseAnalysis
                )
            }
        }

        // 6. Download Statistics Card
        item {
            DownloadStatsCard(
                completedCount = uiState.completedCount,
                totalBytes = uiState.totalDownloadedBytes
            )
        }

        // 7. Supported Platforms Row
        item {
            SupportedPlatformsRow(
                onSelectPlatform = onSelectPlatform
            )
        }

        // 8. Recent Downloads Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = GoldMetallic,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "RECENT DOWNLOADS",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = "${uiState.downloads.size} items",
                    fontSize = 11.sp,
                    color = SilverHighlight.copy(alpha = 0.7f)
                )
            }
        }

        if (uiState.downloads.isEmpty()) {
            item {
                EmptyRecentDownloadsBanner(
                    onTrySample = {
                        onUrlChange("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
                        onAnalyze()
                    }
                )
            }
        } else {
            items(uiState.downloads.take(4), key = { it.id }) { item ->
                DownloadItemCard(
                    item = item,
                    onPause = onPauseDownload,
                    onResume = onResumeDownload,
                    onCancel = onCancelDownload,
                    onDelete = onDeleteDownload,
                    onRetry = onRetryDownload,
                    onPlay = onPlayMedia
                )
            }
        }
    }
}

@Composable
private fun HeroShowcaseBanner(
    onQuickTry: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hero_showcase_banner"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = RoyalBlueDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldDark)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_downloader),
                contentDescription = "AR 3D Downloader",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            // Scrim overlay
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                RoyalBlueDeep.copy(alpha = 0.95f),
                                RoyalBlueDeep.copy(alpha = 0.65f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .matchParentSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(GoldMetallic)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "ULTRA HD",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = RoyalBlueDeep
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "1080p & MP3 EXTRACTOR",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "High Speed Media Engine",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ButtonGoldGradient)
                        .clickable { onQuickTry() }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("quick_sample_try_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = RoyalBlueDeep,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Test Sample Link",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalBlueDeep
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyRecentDownloadsBanner(
    onTrySample: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = RoyalBlueDark.copy(alpha = 0.6f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E325C))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(RoyalBlueSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    tint = GoldMetallic,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "No Downloads Yet",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Paste a video link above or browse supported platforms to start downloading media.",
                fontSize = 12.sp,
                color = SilverHighlight.copy(alpha = 0.7f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(RoyalBlueSurface)
                    .border(1.dp, GoldDark, RoundedCornerShape(8.dp))
                    .clickable { onTrySample() }
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Load Sample Video",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GoldLight
                )
            }
        }
    }
}
