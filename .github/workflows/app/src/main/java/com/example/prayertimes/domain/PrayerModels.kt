package com.example.prayertimes.domain

import java.time.ZonedDateTime

enum class PrayerType(val displayName: String) {
    FAJR("Fajr"),
    SUNRISE("Sunrise"),
    DHUHR("Dhuhr"),
    ASR("Asr"),
    MAGHRIB("Maghrib"),
    ISHA("Isha")
}

data class PrayerScheduleItem(
    val type: PrayerType,
    val dateTime: ZonedDateTime,
    val isNext: Boolean
)

data class PrayerDaySchedule(
    val items: List<PrayerScheduleItem>,
    val nextPrayer: PrayerScheduleItem?,
    val formattedCountdown: String
)
