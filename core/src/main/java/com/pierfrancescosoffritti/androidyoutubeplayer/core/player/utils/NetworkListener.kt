package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

/** Network listener used to react to changes in internet connectivity */
internal class NetworkListener : BroadcastReceiver() {

  interface Listener {
    fun onNetworkAvailable()
    fun onNetworkUnavailable()
  }

  val listeners = mutableListOf<Listener>()

  override fun onReceive(context: Context, intent: Intent) {
    if (isConnectedToInternet(context)) {
      listeners.forEach { it.onNetworkAvailable() }
    }
    else {
      listeners.forEach { it.onNetworkUnavailable() }
    }
  }
}

private fun isConnectedToInternet(context: Context): Boolean {
  val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
    networkCapabilities.isConnectedToInternet()
  } else {
    val networkInfo = connectivityManager.activeNetworkInfo
    networkInfo != null && networkInfo.isConnected
  }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
private fun NetworkCapabilities.isConnectedToInternet(): Boolean {
  return (hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
          hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
          hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
}
