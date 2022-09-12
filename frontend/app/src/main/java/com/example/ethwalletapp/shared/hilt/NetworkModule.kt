package com.example.ethwalletapp.shared.hilt

import com.example.ethwalletapp.BuildConfig
import com.example.ethwalletapp.data.data_sources.EtherscanApi
import com.example.ethwalletapp.data.data_sources.IEthereumApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
  @Singleton
  @Provides
  fun provideOkHttpClient(): OkHttpClient =
    if (BuildConfig.DEBUG) {
      val loggingInterceptor = HttpLoggingInterceptor()
      loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
      OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    } else {
      OkHttpClient().newBuilder()
        .build()
    }

  @Singleton
  @Provides
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl("https://api.etherscan.io/")
    .client(okHttpClient)
    .build()

  @Singleton
  @Provides
  fun provideEthereumApi(retrofit: Retrofit): IEthereumApi = retrofit.create(EtherscanApi::class.java)
}