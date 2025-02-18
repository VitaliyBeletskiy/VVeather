package vibe.weather.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import vibe.weather.BuildConfig
import vibe.weather.data.model.ApiLocation
import vibe.weather.utils.Failure
import vibe.weather.utils.Result
import vibe.weather.utils.Success
import javax.inject.Inject

interface IGeocodingDataSource {
    suspend fun searchLocation(query: String): Result<List<ApiLocation>, Exception>
}

private const val BASE_URL = "https://api.openweathermap.org/geo/1.0/direct"
private const val API_KEY = BuildConfig.OPEN_WEATHER_API_KEY

@Suppress("ktlint:standard:annotation")
class NetworkGeocodingDataSource @Inject constructor(
    private val client: HttpClient,
) : IGeocodingDataSource {

    // TODO: add better error reporting? (e.g. NetworkException, ServerException)
    override suspend fun searchLocation(query: String): Result<List<ApiLocation>, Exception> {
        val response = client.get("$BASE_URL?q=$query&limit=5&appid=$API_KEY")
        return if (response.status == HttpStatusCode.OK) {
            try {
                val locations = response.body<List<ApiLocation>>()
                Success(locations)
            } catch (e: Exception) {
                Failure(e)
            }
        } else {
            Failure(Exception("Failed to fetch locations"))
        }
    }
}
