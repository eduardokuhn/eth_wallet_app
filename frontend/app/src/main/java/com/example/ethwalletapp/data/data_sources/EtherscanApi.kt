package com.example.ethwalletapp.data.data_sources

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface IEthereumApi {
  suspend fun price(): Response<JsonObject>
  suspend fun balance(address: String): Response<JsonObject>
  suspend fun balances(address: String): Response<JsonObject>
  suspend fun txReceiptStatus(hash: String): Response<JsonObject>
  suspend fun transactionInfo(hash: String): Response<JsonObject>
  suspend fun transactionCount(address: String): Response<JsonObject>
  suspend fun sendTransaction(transaction: String): Response<JsonObject>
}

interface EtherscanApi : IEthereumApi {
  @GET("api?module=stats&action=ethprice&apikey=YourApiKeyToken")
  override suspend fun price(): Response<JsonObject>

  @GET("api?module=account&action=balance&address={address}&tag=latest&apikey=YourApiKeyToken")
  override suspend fun balance(@Path("address") address: String): Response<JsonObject>

  @GET("api?module=account&action=balancemulti&address={address}&tag=latest&apikey=YourApiKeyToken")
  override suspend fun balances(@Path("address") address: String): Response<JsonObject>

  @GET("api?module=transaction&action=gettxreceiptstatus&txhash={hash}&apikey=YourApiKeyToken")
  override suspend fun txReceiptStatus(@Path("hash") hash: String): Response<JsonObject>

  @GET("api?module=proxy&action=eth_getTransactionByHash&txhash={hash}&apikey=YourApiKeyToken")
  override suspend fun transactionInfo(@Path("hash") hash: String): Response<JsonObject>

  @GET("api?module=proxy&action=eth_getTransactionCount&address={address}&tag=latest&apikey=YourApiKeyToken")
  override suspend fun transactionCount(@Path("address") address: String): Response<JsonObject>

  @FormUrlEncoded
  @POST("api?module=proxy&action=eth_sendRawTransaction&hex={rawTransaction}&apikey=YourApiKeyToken")
  override suspend fun sendTransaction(@Field("rawTransaction") transaction: String): Response<JsonObject>
}