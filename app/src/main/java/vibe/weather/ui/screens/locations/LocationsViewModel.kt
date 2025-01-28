package vibe.weather.ui.screens.locations

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import vibe.weather.utils.logD
import javax.inject.Inject

@Suppress("ktlint:standard:annotation")
@HiltViewModel
class LocationsViewModel @Inject constructor() : ViewModel() {
    fun searchLocation(query: String) {
        logD("Looking for $query")
    }
}
