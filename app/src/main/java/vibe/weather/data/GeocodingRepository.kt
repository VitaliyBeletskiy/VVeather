package vibe.weather.data

import vibe.weather.data.model.ApiLocation
import vibe.weather.data.network.IGeocodingDataSource
import vibe.weather.utils.Result
import javax.inject.Inject

interface IGeocodingRepository {
    suspend fun searchLocation(query: String): Result<List<ApiLocation>, Exception>
}

@Suppress("ktlint:standard:annotation")
class GeocodingRepositoryImpl @Inject constructor(
    private val geocodingDataSource: IGeocodingDataSource,
) : IGeocodingRepository {

    override suspend fun searchLocation(query: String): Result<List<ApiLocation>, Exception> =
        geocodingDataSource.searchLocation(query)
}
