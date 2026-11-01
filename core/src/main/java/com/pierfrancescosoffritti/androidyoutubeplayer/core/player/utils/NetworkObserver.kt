package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi

/** Class used to observe changes to network state. Must be used on the main thread. */
internal class NetworkObserver(private val context: Context) {

  interface Listener {
    fun onNetworkAvailable()
    fun onNetworkUnavailable()
  }

  val listeners = mutableListOf<Listener>()

  private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  private var networkBroadcastReceiver: NetworkBroadcastReceiver? = null
  private var networkCallback: ConnectivityManager.NetworkCallback? = null

  /** Start observing network changes, replacing any previous observation */
  fun observeNetwork() {
    stopObserving()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      doObserveNetwork()
    } else {
      doObserveNetworkLegacy()
    }
  }

  /** Stop observing network changes and cleanup */
  fun destroy() {
    stopObserving()
    listeners.clear()
  }

  private fun stopObserving() {
    networkCallback?.let { connectivityManager.unregisterNetworkCallback(it) }
    networkBroadcastReceiver?.let { runCatching { context.unregisterReceiver(it) } }

    networkCallback = null
    networkBroadcastReceiver = null
  }

  @RequiresApi(Build.VERSION_CODES.N)
  private fun doObserveNetwork() {
    val callback = object : ConnectivityManager.NetworkCallback() {
      private val mainThreadHandler = Handler(Looper.getMainLooper())

      override fun onAvailable(network: Network) = notifyListeners { it.onNetworkAvailable() }

      override fun onLost(network: Network) = notifyListeners { it.onNetworkUnavailable() }

      private fun notifyListeners(event: (Listener) -> Unit) {
        // the callback is not on the main thread
        mainThreadHandler.post {
          // a replaced callback can still have events queued on the main thread
          if (networkCallback === this) {
            listeners.forEach(event)
          }
        }
      }
    }

    connectivityManager.registerDefaultNetworkCallback(callback)
    networkCallback = callback
  }

  private fun doObserveNetworkLegacy() {
    networkBroadcastReceiver = NetworkBroadcastReceiver(
      onNetworkAvailable = { listeners.forEach { it.onNetworkAvailable() } },
      onNetworkUnavailable = { listeners.forEach { it.onNetworkUnavailable() } },
    )
    context.registerReceiver(networkBroadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
  }
}

/** Broadcast receiver used to react to changes in internet connectivity */
private class NetworkBroadcastReceiver(
  private val onNetworkAvailable: () -> Unit,
  private val onNetworkUnavailable: () -> Unit
) : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    if (isConnectedToInternet(context)) {
      onNetworkAvailable()
    }
    else {
      onNetworkUnavailable()
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
