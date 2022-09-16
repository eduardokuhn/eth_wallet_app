package com.example.ethwalletapp.shared.hilt

import com.example.ethwalletapp.BuildConfig
import com.example.ethwalletapp.data.data_sources.EtherscanApi
import com.example.ethwalletapp.data.data_sources.IEthereumApi
import com.example.ethwalletapp.shared.network.HostSelectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
  @Singleton
  @Provides
  fun provideHostSelectionInterceptor(): HostSelectionInterceptor {
    return HostSelectionInterceptor()
  }

  @Singleton
  @Provides
  fun provideOkHttpClient(hostSelectionInterceptor: HostSelectionInterceptor): OkHttpClient =
    if (BuildConfig.DEBUG) {
      val loggingInterceptor = HttpLoggingInterceptor()
      loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
      OkHttpClient
        .Builder()
        .retryOnConnectionFailure(true)
        .connectTimeout(200, TimeUnit.SECONDS)
        .readTimeout(200, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(hostSelectionInterceptor)
        .build()
    } else {
      OkHttpClient()
        .newBuilder()
        .retryOnConnectionFailure(true)
        .connectTimeout(200, TimeUnit.SECONDS)
        .readTimeout(200, TimeUnit.SECONDS)
        .addInterceptor(hostSelectionInterceptor)
        .build()
    }

  @Singleton
  @Provides
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl("https://api-rinkeby.etherscan.io/")
    .client(okHttpClient)
    .build()

  @Singleton
  @Provides
  fun provideEthereumApi(retrofit: Retrofit): IEthereumApi = retrofit.create(EtherscanApi::class.java)
}