package com.example.ethwalletapp.data.repositories

import android.content.SharedPreferences
import android.util.Log
import com.example.ethwalletapp.data.data_sources.IEthereumApi
import com.example.ethwalletapp.data.data_sources.daos.BalanceDao
import com.example.ethwalletapp.data.models.BalanceEntry
import com.example.ethwalletapp.data.services.IConnectivityService
import com.example.ethwalletapp.shared.utils.Constant
import com.example.ethwalletapp.shared.utils.NetworkResult
import org.kethereum.model.Address
import retrofit2.HttpException
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

interface IBalanceRepository {
  suspend fun getPrice(): NetworkResult<Double>
  suspend fun getAddressBalance(address: Address): NetworkResult<BalanceEntry>
  suspend fun getAddressesBalance(addresses: List<Address>): NetworkResult<List<BalanceEntry>>
}

@Singleton
class BalanceRepositoryImpl @Inject constructor(
  private val ethereumApi: IEthereumApi,
  private val balanceDao: BalanceDao,
  private val sharedPreferences: SharedPreferences,
  private val connectivityService: IConnectivityService
): IBalanceRepository {
  override suspend fun getPrice(): NetworkResult<Double> {
    if (connectivityService.hasConnection()) {
      val resp = ethereumApi.price()
      val body = resp.body()

      return try {
        if (resp.isSuccessful && body != null) {
          val price = body["result"].asJsonObject["ethusd"].asString
          sharedPreferences.edit().putString("ethusd_price", price)
          NetworkResult.Success(price.toDouble())
        } else {
          NetworkResult.Error(resp.code(), resp.message())
        }
      } catch (e: HttpException) {
        NetworkResult.Error(resp.code(), resp.message())
      } catch (e: Throwable) {
        Log.e("BalanceRepositoryImpl.getPrice", "Exception: $e")
        NetworkResult.Exception(e)
      }
    } else {
      val price = sharedPreferences.getString("ethusd_price", "0.0") ?: "0.0"
      return NetworkResult.Success(price.toDouble())
    }
  }

  override suspend fun getAddressBalance(address: Address): NetworkResult<BalanceEntry> {
    if (connectivityService.hasConnection()) {
      val resp = ethereumApi.balance(address.hex)
      val body = resp.body()

      return try {
        if (resp.isSuccessful && body != null) {
          val balance = BalanceEntry(
            address = address,
            // TODO: set correct chainId
            chainId = BigInteger.valueOf(0),
            balance = BigInteger(body["result"].asString),
            tokenAddress = Address(Constant.ETHEREUM_TOKEN_ADDRESS)
          )
          balanceDao.add(balance)
          NetworkResult.Success(balance)
        } else {
          NetworkResult.Error(resp.code(), resp.message())
        }
      } catch (e: HttpException) {
        NetworkResult.Error(e.code(), e.message())
      } catch (e: Throwable) {
        NetworkResult.Exception(e)
      }
    } else {
      return NetworkResult.Success(balanceDao.balance(address))
    }
  }

  override suspend fun getAddressesBalance(addresses: List<Address>): NetworkResult<List<BalanceEntry>> {
    if (connectivityService.hasConnection()) {
      val resp = ethereumApi.balances(addresses.joinToString(","))
      val body = resp.body()

      return try {
        if (resp.isSuccessful && body != null) {
          val balances = mutableListOf<BalanceEntry>()

          body["result"].asJsonArray.forEach { json ->
            val j = json.asJsonObject
            val balance =  BalanceEntry(
              address = Address(j["account"].asString),
              // TODO: set correct chainId
              chainId = BigInteger.valueOf(0),
              balance = BigInteger(j["balance"].asString),
              tokenAddress = Address(Constant.ETHEREUM_TOKEN_ADDRESS)
            )
            balances.add(balance)
            balanceDao.add(balance)
          }
          NetworkResult.Success(balances)
        } else {
          NetworkResult.Error(resp.code(), resp.message())
        }
      } catch (e: HttpException) {
        NetworkResult.Error(e.code(), e.message())
      } catch (e: Throwable) {
        Log.e("getAddressesBalance", "Exception: $e")
        NetworkResult.Exception(e)
      }
    } else {
      return NetworkResult.Success(balanceDao.balances(addresses))
    }
  }
}