package com.example.prayertimes.domain

import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.CalculationParameters
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class PrayerCalculator {

    fun calculateDaySchedule(
        latitude: Double,
        longitude: Double,
        zoneId: ZoneId = ZoneId.systemDefault(),
        method: CalculationMethod = CalculationMethod.MUSLIM_WORLD_LEAGUE,
        madhab: Madhab = Madhab.SHAFI,
        now: ZonedDateTime = ZonedDateTime.now(zoneId)
    ): PrayerDaySchedule {
        val today = now.toLocalDate()
        val coordinates = Coordinates(latitude, longitude)
        val params: CalculationParameters = method.parameters.apply {
            this.madhab = madhab
        }

        val todayTimes = computeTimesForDate(today, coordinates, params)
        val allSlots = listOf(
            PrayerType.FAJR to todayTimes.fajr,
            PrayerType.SUNRISE to todayTimes.sunrise,
            PrayerType.DHUHR to todayTimes.dhuhr,
            PrayerType.ASR to todayTimes.asr,
            PrayerType.MAGHRIB to todayTimes.maghrib,
            PrayerType.ISHA to todayTimes.isha
        ).map { (type, date) ->
            type to date.toInstant().atZone(zoneId)
        }

        val upcomingToday = allSlots.firstOrNull { (_, time) -> time.isAfter(now) }
        val nextPair: Pair<PrayerType, ZonedDateTime> = upcomingToday ?: run {
            val tomorrow = today.plusDays(1)
            val tomorrowTimes = computeTimesForDate(tomorrow, coordinates, params)
            PrayerType.FAJR to tomorrowTimes.fajr.toInstant().atZone(zoneId)
        }

        val scheduleItems = allSlots.map { (type, time) ->
            PrayerScheduleItem(
                type = type,
                dateTime = time,
                isNext = (type == nextPair.first && upcomingToday != null)
            )
        }

        val nextItem = PrayerScheduleItem(
            type = nextPair.first,
            dateTime = nextPair.second,
            isNext = true
        )

        val duration = Duration.between(now, nextPair.second).coerceAtLeast(Duration.ZERO)
        val hours = duration.toHours()
        val minutes = (duration.toMinutes() % 60)
        val seconds = (duration.seconds % 60)
        val formattedCountdown = String.format("%02d:%02d:%02d", hours, minutes, seconds)

        return PrayerDaySchedule(
            items = scheduleItems,
            nextPrayer = nextItem,
            formattedCountdown = formattedCountdown
        )
    }

    private fun computeTimesForDate(
        date: LocalDate,
        coordinates: Coordinates,
        params: CalculationParameters
    ): PrayerTimes {
        val dateComponents = DateComponents(date.year, date.monthValue, date.dayOfMonth)
        return PrayerTimes(coordinates, dateComponents, params)
    }
}
