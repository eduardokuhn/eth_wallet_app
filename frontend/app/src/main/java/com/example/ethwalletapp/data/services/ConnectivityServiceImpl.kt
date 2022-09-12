package com.example.ethwalletapp.data.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject
import javax.inject.Singleton

interface IConnectivityService {
  fun hasConnection(): Boolean
}

@Singleton
class ConnectivityServiceImpl @Inject constructor(
  private val context: Context
) : IConnectivityService {
  override fun hasConnection(): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var result = false

    connectivityManager.let {
      it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
        result = when {
          hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
          hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
          else -> false
        }
      }
    }

    return result
  }
}