package vibe.weather.ui.screens.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import vibe.weather.domain.GetLocationsUseCase
import javax.inject.Inject

@Suppress("ktlint:standard:annotation")
@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val getLocationsUseCase: GetLocationsUseCase,
) : ViewModel() {

    fun searchLocation(query: String) {
        viewModelScope.launch {
            getLocationsUseCase.searchLocation(query)
        }
    }
}
