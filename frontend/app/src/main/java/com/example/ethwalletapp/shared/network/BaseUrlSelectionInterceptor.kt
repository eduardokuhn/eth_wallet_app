package com.example.ethwalletapp.shared.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Singleton

interface IBaseUrlSelectionInterceptor : Interceptor

@Singleton
class BaseUrlSelectionInterceptorImpl : IBaseUrlSelectionInterceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    TODO("Not yet implemented")
  }
}