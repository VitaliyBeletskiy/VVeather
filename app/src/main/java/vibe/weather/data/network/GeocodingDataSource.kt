package vibe.weather.data.network

import vibe.weather.utils.logD
import javax.inject.Inject

interface IGeocodingDataSource {
    suspend fun searchLocation(query: String)
}

@Suppress("ktlint:standard:annotation")
class NetworkGeocodingDataSource @Inject constructor() : IGeocodingDataSource {

    override suspend fun searchLocation(query: String) {
        logD("Looking for $query")
    }
}
