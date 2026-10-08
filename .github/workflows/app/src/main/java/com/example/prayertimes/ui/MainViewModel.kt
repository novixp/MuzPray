package com.example.prayertimes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prayertimes.domain.PrayerCalculator
import com.example.prayertimes.domain.PrayerDaySchedule
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class MainViewModel : ViewModel() {

    private val calculator = PrayerCalculator()

    private val defaultLat = 21.4225
    private val defaultLng = 39.8262

    private val _scheduleState = MutableStateFlow<PrayerDaySchedule?>(null)
    val scheduleState: StateFlow<PrayerDaySchedule?> = _scheduleState.asStateFlow()

    init {
        startTimerLoop()
    }

    private fun startTimerLoop() {
        viewModelScope.launch {
            while (isActive) {
                _scheduleState.value = calculator.calculateDaySchedule(
                    latitude = defaultLat,
                    longitude = defaultLng,
                    now = ZonedDateTime.now()
                )
                delay(1000L)
            }
        }
    }
}
