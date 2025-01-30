package vibe.weather.data

import vibe.weather.data.network.IGeocodingDataSource
import javax.inject.Inject

interface IGeocodingRepository {
    suspend fun searchLocation(query: String)
}

@Suppress("ktlint:standard:annotation")
class GeocodingRepositoryImpl @Inject constructor(
    private val geocodingDataSource: IGeocodingDataSource,
) : IGeocodingRepository {

    override suspend fun searchLocation(query: String) {
        geocodingDataSource.searchLocation(query)
    }
}
