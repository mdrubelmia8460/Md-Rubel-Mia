package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.ButtonGoldGradient
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMetallic
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBlueDark
import com.example.ui.theme.RoyalBlueDeep
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.theme.RoyalBlueSurface
import com.example.ui.theme.SilverHighlight

@Composable
fun VideoUrlInputCard(
    url: String,
    isAnalyzing: Boolean,
    onUrlChange: (String) -> Unit,
    onAnalyze: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(RoyalBlueDark)
            .border(1.dp, GoldDark, RoundedCornerShape(20.dp))
            .padding(16.dp)
            .testTag("url_input_card")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = null,
                    tint = GoldMetallic,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PASTE VIDEO / AUDIO LINK",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    letterSpacing = 0.5.sp
                )
            }

            // Paste button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(RoyalBlueSurface)
                    .clickable {
                        val clip = clipboardManager.getText()?.text
                        if (!clip.isNullOrBlank()) {
                            onUrlChange(clip.trim())
                        }
                    }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .testTag("paste_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ContentPaste,
                        contentDescription = "Paste",
                        tint = GoldLight,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "PASTE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Input TextField
        OutlinedTextField(
            value = url,
            onValueChange = onUrlChange,
            placeholder = {
                Text(
                    text = "https://example.com/video.mp4",
                    color = SilverHighlight.copy(alpha = 0.5f),
                    fontSize = 13.sp
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onAnalyze() }
            ),
            trailingIcon = {
                if (url.isNotEmpty()) {
                    IconButton(
                        onClick = { onUrlChange("") },
                        modifier = Modifier.testTag("clear_url_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = SilverHighlight
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = RoyalBlueDeep,
                unfocusedContainerColor = RoyalBlueDeep,
                focusedBorderColor = GoldMetallic,
                unfocusedBorderColor = Color(0xFF243B6B),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = GoldMetallic
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("url_text_field")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Analyze Media Button with 3D Gold Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(ButtonGoldGradient)
                .clickable(enabled = !isAnalyzing && url.isNotBlank()) { onAnalyze() }
                .testTag("analyze_button"),
            contentAlignment = Alignment.Center
        ) {
            if (isAnalyzing) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = RoyalBlueDeep,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "ANALYZING MEDIA STREAMS...",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlueDeep
                    )
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Analyze",
                        tint = RoyalBlueDeep,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ANALYZE & DOWNLOAD",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RoyalBlueDeep,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SupportedPlatformsRow(
    onSelectPlatform: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val platforms = listOf(
        Pair("TikTok", "https://www.tiktok.com"),
        Pair("Facebook", "https://www.facebook.com"),
        Pair("Instagram", "https://www.instagram.com"),
        Pair("YouTube", "https://www.youtube.com"),
        Pair("Vimeo", "https://vimeo.com"),
        Pair("X (Twitter)", "https://twitter.com")
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "SUPPORTED SOURCES & DIRECT PLATFORMS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = GoldLight.copy(alpha = 0.9f),
            letterSpacing = 0.8.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            platforms.take(3).forEach { (name, url) ->
                PlatformChip(name = name, onClick = { onSelectPlatform(url) })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            platforms.drop(3).forEach { (name, url) ->
                PlatformChip(name = name, onClick = { onSelectPlatform(url) })
            }
        }
    }
}

@Composable
private fun PlatformChip(
    name: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(RoyalBlueDark)
            .border(1.dp, Color(0xFF203562), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("platform_chip_${name.lowercase().replace(" ", "_").replace("(", "").replace(")", "")}"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = SilverHighlight
        )
    }
}
