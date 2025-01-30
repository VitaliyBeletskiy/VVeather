package vibe.weather.domain

import vibe.weather.data.IGeocodingRepository
import javax.inject.Inject

@Suppress("ktlint:standard:annotation")
class GetLocationsUseCase @Inject constructor(
    private val geocodingRepository: IGeocodingRepository,
) {

    suspend fun searchLocation(query: String) {
        geocodingRepository.searchLocation(query)
    }
}
