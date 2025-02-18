package vibe.weather.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openMapsApp(context: Context, latitude: Double, longitude: Double, noMapApp: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("geo:$latitude,$longitude")
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        noMapApp.invoke()
    }
}
