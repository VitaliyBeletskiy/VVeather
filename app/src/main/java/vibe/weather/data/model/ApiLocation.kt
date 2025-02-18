package vibe.weather.data.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ApiLocation(
    val name: String,
    @JsonNames("local_names") val localNames: Map<String, String>? = null,
    val lat: Double,
    val lon: Double,
    val country: String? = null,
    val state: String? = null,
)
