package com.example.ethwalletapp.data.data_sources

import com.example.ethwalletapp.shared.utils.Env
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface IEthereumApi {
  suspend fun price(): Response<JsonObject>
  suspend fun balance(address: String): Response<JsonObject>
  suspend fun balances(addresses: String): Response<JsonObject>
  suspend fun transactions(address: String, page: Int?): Response<JsonObject>
  suspend fun transactionReceiptStatus(hash: String): Response<JsonObject>
  suspend fun transactionCount(address: String): Response<JsonObject>
  suspend fun sendRawTransaction(hex: String): Response<JsonObject>
}

interface EtherscanApi : IEthereumApi {
  @GET("api?module=stats&action=ethprice&apikey=${Env.ETHERSCAN_API_TOKEN}")
  override suspend fun price(): Response<JsonObject>

  @GET("api?module=account&action=balance&tag=latest&apikey=${Env.ETHERSCAN_API_TOKEN}")
  override suspend fun balance(@Query("address") address: String): Response<JsonObject>

  @GET("api?module=account&action=balancemulti&tag=latest&apikey=${Env.ETHERSCAN_API_TOKEN}")
  override suspend fun balances(@Query("address") addresses: String): Response<JsonObject>

  @GET("api?module=account&action=txlist&startblock=0&endblock=99999999&offset=10&sort=desc&apikey=${Env.ETHERSCAN_API_TOKEN}")
  override suspend fun transactions(@Query("address") address: String, @Query("page") page: Int?): Response<JsonObject>

  @GET("api?module=transaction&action=gettxreceiptstatus&apikey=${Env.ETHERSCAN_API_TOKEN}")
  override suspend fun transactionReceiptStatus(@Query("txhash") hash: String): Response<JsonObject>

  @GET("api?module=proxy&action=eth_getTransactionCount&tag=latest&apikey=${Env.ETHERSCAN_API_TOKEN}")
  override suspend fun transactionCount(@Query("address") address: String): Response<JsonObject>

  @FormUrlEncoded
  @POST("api?module=proxy&action=eth_sendRawTransaction&apikey=${Env.ETHERSCAN_API_TOKEN}")
  override suspend fun sendRawTransaction(@Field("hex") hex: String): Response<JsonObject>
}