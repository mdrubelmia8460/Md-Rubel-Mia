package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldGradient
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMetallic
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBlueDark
import com.example.ui.theme.RoyalBlueDeep
import com.example.ui.theme.RoyalBlueLight
import com.example.ui.theme.RoyalBlueSurface
import com.example.ui.theme.SilverHighlight

@Composable
fun ARBrandHeader(
    modifier: Modifier = Modifier,
    onLogoClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("brand_header_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = RoyalBlueDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, Brush.horizontalGradient(listOf(GoldDark, GoldMetallic, GoldDark)))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            RoyalBlueSurface.copy(alpha = 0.9f),
                            RoyalBlueDeep.copy(alpha = 0.95f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onLogoClick() }
                ) {
                    // 3D Emblem with Gold border
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .shadow(8.dp, CircleShape, spotColor = GoldMetallic)
                            .border(2.dp, GoldGradient, CircleShape)
                            .clip(CircleShape)
                            .background(RoyalBlueDeep),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ar_downloader_logo),
                            contentDescription = "AR 3D Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AR",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    brush = GoldGradient
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ALL VIDEO",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                        Text(
                            text = "DOWNLOADER & MUSIC ENGINE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GoldLight.copy(alpha = 0.85f),
                                letterSpacing = 1.2.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                // VIP Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(GoldDark, GoldMetallic)
                            )
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "VIP PRO",
                            tint = RoyalBlueDeep,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "PRO 3D",
                            fontSize = 10.sp,
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
fun DownloadStatsCard(
    completedCount: Int,
    totalBytes: Long,
    modifier: Modifier = Modifier
) {
    val totalMb = totalBytes / (1024 * 1024)
    val gbString = if (totalMb >= 1024) String.format("%.2f GB", totalMb / 1024f) else "$totalMb MB"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("stats_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = RoyalBlueSurface.copy(alpha = 0.6f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF243B6B))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(
                icon = Icons.Default.Download,
                value = "$completedCount",
                label = "Saved Media",
                accent = GoldMetallic
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(Color(0xFF2C4477))
            )

            StatItem(
                icon = Icons.Default.Speed,
                value = gbString,
                label = "Data Saved",
                accent = RoyalBlueLight
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(Color(0xFF2C4477))
            )

            StatItem(
                icon = Icons.Default.Shield,
                value = "100%",
                label = "Encrypted",
                accent = SilverHighlight
            )
        }
    }
}

@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    accent: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = SilverHighlight.copy(alpha = 0.7f),
            fontWeight = FontWeight.Normal
        )
    }
}
