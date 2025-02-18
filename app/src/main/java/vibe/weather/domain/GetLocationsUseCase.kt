package vibe.weather.domain

import vibe.weather.data.IGeocodingRepository
import vibe.weather.data.model.Location
import vibe.weather.utils.Failure
import vibe.weather.utils.Result
import vibe.weather.utils.Success
import vibe.weather.utils.fold
import java.util.Locale
import javax.inject.Inject

@Suppress("ktlint:standard:annotation")
class GetLocationsUseCase @Inject constructor(
    private val geocodingRepository: IGeocodingRepository,
) {
    suspend fun searchLocation(query: String): Result<List<Location>, Exception> {
        geocodingRepository.searchLocation(query).fold(
            ifSuccess = { apiLocations ->
                val locations = apiLocations.map { apiLocation ->
                    val region = apiLocation.country?.let { country ->
                        val countryName = Locale("", country).displayCountry
                        apiLocation.state?.let { state ->
                            "$countryName, $state"
                        } ?: countryName
                    } ?: ""
                    Location(
                        name = apiLocation.name,
                        lat = apiLocation.lat,
                        lon = apiLocation.lon,
                        region = region,
                    )
                }
                return Success(locations)
            },
            ifFailure = { error ->
                return Failure(error)
            },
        )
    }
}
