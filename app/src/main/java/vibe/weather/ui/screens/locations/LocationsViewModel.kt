package vibe.weather.ui.screens.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vibe.weather.R
import vibe.weather.data.model.Location
import vibe.weather.domain.GetLocationsUseCase
import vibe.weather.utils.fold
import javax.inject.Inject

data class LocationsUiState(
    val isLoading: Boolean = false,
    val locations: List<Location> = emptyList(),
    val messageResId: Int? = null,
)

@Suppress("ktlint:standard:annotation")
@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val getLocationsUseCase: GetLocationsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationsUiState())
    val uiState: StateFlow<LocationsUiState>
        get() = _uiState

    fun searchLocation(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getLocationsUseCase.searchLocation(query).fold(
                ifSuccess = { locations ->
                    _uiState.value = _uiState.value.copy(locations = locations)
                    if (locations.isEmpty()) {
                        _uiState.value = _uiState.value.copy(messageResId = R.string.no_location_found)
                    }
                },
                ifFailure = { error ->
                    _uiState.value = _uiState.value.copy(messageResId = R.string.find_location_failed)
                },
            )
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(messageResId = null) }
    }
}
