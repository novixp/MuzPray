package com.example.prayertimes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

data class PrayerTime(val name: String, val time: String, val isNext: Boolean = false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0F172A) // Deep Slate / Midnight
                ) {
                    PrayerDashboard()
                }
            }
        }
    }
}

@Composable
fun PrayerDashboard() {
    val currentDate = remember {
        SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(Date())
    }

    val prayers = listOf(
        PrayerTime("Fajr", "05:15 AM"),
        PrayerTime("Sunrise", "06:30 AM"),
        PrayerTime("Dhuhr", "12:30 PM", isNext = true),
        PrayerTime("Asr", "04:15 PM"),
        PrayerTime("Maghrib", "06:35 PM"),
        PrayerTime("Isha", "07:55 PM")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "MuzPray",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = currentDate,
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8)
                )
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF1E293B))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Offline Mode",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF10B981)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Hero Highlight Card (Next Prayer)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF065F46), Color(0xFF047857)) // Rich emerald gradient
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "UPCOMING PRAYER",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    color = Color(0xFFA7F3D0)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "Dhuhr",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "12:30 PM",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Today's Schedule",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Prayer Timetable List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(prayers) { prayer ->
                PrayerRow(prayer = prayer)
            }
        }
    }
}

@Composable
fun PrayerRow(prayer: PrayerTime) {
    val cardBackground = if (prayer.isNext) Color(0xFF1E293B) else Color(0xFF161F30)
    val borderColor = if (prayer.isNext) Color(0xFF10B981) else Color.Transparent

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        border = if (prayer.isNext) androidx.compose.foundation.BorderStroke(1.dp, borderColor) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (prayer.isNext) Color(0xFF10B981) else Color(0xFF475569))
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = prayer.name,
                    fontSize = 16.sp,
                    fontWeight = if (prayer.isNext) FontWeight.Bold else FontWeight.Normal,
                    color = if (prayer.isNext) Color.White else Color(0xFFE2E8F0)
                )
            }
            Text(
                text = prayer.time,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (prayer.isNext) Color(0xFF34D399) else Color(0xFF94A3B8)
            )
        }
    }
}
