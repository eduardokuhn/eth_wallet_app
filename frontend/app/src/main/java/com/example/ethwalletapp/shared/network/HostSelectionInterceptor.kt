package com.example.ethwalletapp.shared.network

import android.util.Log
import com.example.ethwalletapp.data.services.EthereumNetwork
import com.example.ethwalletapp.data.services.IEthereumNetworkService
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.URISyntaxException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HostSelectionInterceptor : Interceptor {
  private var host: HttpUrl? = "https://api-rinkeby.etherscan.io/".toHttpUrlOrNull()

  fun setHostBaseUrl(baseUrl: String) {
    host = baseUrl.toHttpUrlOrNull()
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    var request = chain.request()

    if (host != null) {
      var newUrl: HttpUrl? = null
      try {
        newUrl = request
          .url
          .newBuilder()
          .scheme(host!!.scheme)
          .host(host!!.host)
          .build()
      } catch (e: URISyntaxException) {
        Log.e("HostSelectionInterceptorImpl.intercept", "${e.message}")
      }
      if (newUrl != null)
        request = request
          .newBuilder()
          .url(newUrl)
          .build()
    }
    return chain.proceed(request)
  }
}