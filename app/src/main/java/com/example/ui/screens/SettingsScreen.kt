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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.state.AppSettings
import com.example.ui.theme.ButtonGoldGradient
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMetallic
import com.example.ui.theme.RoyalBlueDark
import com.example.ui.theme.RoyalBlueDeep
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.theme.RoyalBlueSurface
import com.example.ui.theme.SilverHighlight

@Composable
fun SettingsScreen(
    settings: AppSettings,
    onUpdateDefaultQuality: (String) -> Unit,
    onUpdateDefaultAudioFormat: (String) -> Unit,
    onUpdateLanguage: (String) -> Unit,
    onToggleTheme: (Boolean) -> Unit,
    onToggleWifiOnly: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var showQualityDropdown by remember { mutableStateOf(false) }
    var showAudioDropdown by remember { mutableStateOf(false) }
    var showLangDropdown by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    val qualityList = listOf("1080p Full HD", "720p HD", "480p SD", "360p Low Data")
    val audioList = listOf("MP3 (320kbps)", "M4A (256kbps)", "WAV (Uncompressed)", "AAC (192kbps)")
    val langList = listOf(Pair("English", "en"), Pair("Bengali (বাংলা)", "bn"))

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(RoyalBlueDeep)
            .testTag("settings_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // App Identity Card
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
                            .border(1.5.dp, GoldMetallic, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ar_downloader_logo),
                            contentDescription = "AR Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(46.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "AR All Video Downloader",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Version 1.0.0 • Developer: AR",
                            fontSize = 11.sp,
                            color = GoldLight.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Section: Download Preferences
        item {
            Text(
                text = "DOWNLOAD PREFERENCES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight,
                letterSpacing = 0.8.sp
            )
        }

        // Default Video Quality
        item {
            SettingsClickableRow(
                icon = Icons.Default.HighQuality,
                title = "Default Video Quality",
                subtitle = settings.defaultQuality,
                onClick = { showQualityDropdown = true }
            ) {
                DropdownMenu(
                    expanded = showQualityDropdown,
                    onDismissRequest = { showQualityDropdown = false },
                    modifier = Modifier.background(RoyalBlueDark)
                ) {
                    qualityList.forEach { q ->
                        DropdownMenuItem(
                            text = { Text(q, color = Color.White) },
                            onClick = {
                                onUpdateDefaultQuality(q)
                                showQualityDropdown = false
                            }
                        )
                    }
                }
            }
        }

        // Default Audio Format
        item {
            SettingsClickableRow(
                icon = Icons.Default.MusicNote,
                title = "Default Audio Format",
                subtitle = settings.defaultAudioFormat,
                onClick = { showAudioDropdown = true }
            ) {
                DropdownMenu(
                    expanded = showAudioDropdown,
                    onDismissRequest = { showAudioDropdown = false },
                    modifier = Modifier.background(RoyalBlueDark)
                ) {
                    audioList.forEach { a ->
                        DropdownMenuItem(
                            text = { Text(a, color = Color.White) },
                            onClick = {
                                onUpdateDefaultAudioFormat(a)
                                showAudioDropdown = false
                            }
                        )
                    }
                }
            }
        }

        // Storage Folder
        item {
            SettingsClickableRow(
                icon = Icons.Default.Folder,
                title = "Storage Destination",
                subtitle = settings.saveFolder,
                onClick = {}
            )
        }

        // Section: System & General
        item {
            Text(
                text = "GENERAL & INTERFACE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight,
                letterSpacing = 0.8.sp
            )
        }

        // Language Selection (English / Bengali)
        item {
            val curLangLabel = if (settings.language == "bn") "Bengali (বাংলা)" else "English"
            SettingsClickableRow(
                icon = Icons.Default.Language,
                title = "Language Selection",
                subtitle = curLangLabel,
                onClick = { showLangDropdown = true }
            ) {
                DropdownMenu(
                    expanded = showLangDropdown,
                    onDismissRequest = { showLangDropdown = false },
                    modifier = Modifier.background(RoyalBlueDark)
                ) {
                    langList.forEach { (label, code) ->
                        DropdownMenuItem(
                            text = { Text(label, color = Color.White) },
                            onClick = {
                                onUpdateLanguage(code)
                                showLangDropdown = false
                            }
                        )
                    }
                }
            }
        }

        // Dark / Light Theme Toggle
        item {
            SettingsSwitchRow(
                icon = Icons.Default.Brightness4,
                title = "3D Royal Dark Mode",
                subtitle = if (settings.isDarkMode) "Active Royal Blue & Gold theme" else "Light theme",
                checked = settings.isDarkMode,
                onCheckedChange = onToggleTheme
            )
        }

        // Wi-Fi Only Toggle
        item {
            SettingsSwitchRow(
                icon = Icons.Default.Wifi,
                title = "Download via Wi-Fi Only",
                subtitle = "Conserve mobile data",
                checked = settings.autoDownloadViaWifiOnly,
                onCheckedChange = onToggleWifiOnly
            )
        }

        // Section: Security & About
        item {
            Text(
                text = "SECURITY & COMPLIANCE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight,
                letterSpacing = 0.8.sp
            )
        }

        // Privacy Policy
        item {
            SettingsClickableRow(
                icon = Icons.Default.Security,
                title = "Privacy Policy",
                subtitle = "Zero data logging • 100% User privacy",
                onClick = { showPrivacyDialog = true }
            )
        }

        // About Application
        item {
            SettingsClickableRow(
                icon = Icons.Default.Info,
                title = "About AR Downloader",
                subtitle = "Developer: AR • Policy & DRM Compliance",
                onClick = { showAboutDialog = true }
            )
        }
    }

    // Privacy Policy Dialog
    if (showPrivacyDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            confirmButton = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ButtonGoldGradient)
                        .clickable { showPrivacyDialog = false }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text("OK", color = RoyalBlueDeep, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text("Privacy & Security", color = GoldLight, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "AR All Video Downloader respects your confidentiality. All URL parsing, media downloads, and file conversions occur directly on your device. We do not transmit or collect browsing history, personal data, or downloaded media. Real HTTPS connections are enforced.",
                    color = SilverHighlight,
                    fontSize = 13.sp
                )
            },
            containerColor = RoyalBlueDark
        )
    }

    // About Application Dialog
    if (showAboutDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            confirmButton = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ButtonGoldGradient)
                        .clickable { showAboutDialog = false }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text("Close", color = RoyalBlueDeep, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text("AR All Video Downloader", color = GoldLight, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Developer: AR\nPlatform: Android\nLanguage: Kotlin / Jetpack Compose\nDesign: Premium 3D Royal Blue and Gold\n\nCompliance Notice:\nThis application is intended strictly for authorized media download workflows and educational purposes. It honors digital copyright and platform terms of service.",
                    color = SilverHighlight,
                    fontSize = 13.sp
                )
            },
            containerColor = RoyalBlueDark
        )
    }
}

@Composable
private fun SettingsClickableRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("settings_${title.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = RoyalBlueDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E325C))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(RoyalBlueSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = GoldMetallic,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = SilverHighlight.copy(alpha = 0.7f)
                    )
                }
            }

            content()
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = RoyalBlueDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E325C))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(RoyalBlueSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = GoldMetallic,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = SilverHighlight.copy(alpha = 0.7f)
                    )
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = RoyalBlueDeep,
                    checkedTrackColor = GoldMetallic,
                    uncheckedThumbColor = SilverHighlight,
                    uncheckedTrackColor = RoyalBlueSurface
                )
            )
        }
    }
}
