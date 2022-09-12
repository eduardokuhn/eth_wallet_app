package com.example.ethwalletapp.data.repositories

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
  private val connectivityService: IConnectivityService
): IBalanceRepository {
  override suspend fun getPrice(): NetworkResult<Double> {
    if (connectivityService.hasConnection()) {
      val resp = ethereumApi.price()
      val body = resp.body()

      return try {
        if (resp.isSuccessful && body != null) {
          NetworkResult.Success(body["ethusd"].asDouble)
        } else {
          NetworkResult.Error(resp.code(), resp.message())
        }
      } catch (e: HttpException) {
        NetworkResult.Error(resp.code(), resp.message())
      } catch (e: Throwable) {
        NetworkResult.Exception(e)
      }
    } else {
      // TODO get price local from shared preferences
      return NetworkResult.Success(0.0)
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
            chainId = BigInteger.valueOf(0),
            balance = body["result"].asBigInteger,
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
          var balances: List<BalanceEntry> = listOf()

          body["result"].asJsonArray.forEach { json ->
            val j = json.asJsonObject
            val balance =  BalanceEntry(
              address = Address(j["account"].asString),
              chainId = BigInteger.valueOf(0),
              balance = j["balance"].asBigInteger,
              tokenAddress = Address(Constant.ETHEREUM_TOKEN_ADDRESS)
            )
            balances.plus(balance)
            balanceDao.add(balance)
          }
          NetworkResult.Success(balances)
        } else {
          NetworkResult.Error(resp.code(), resp.message())
        }
      } catch (e: HttpException) {
        NetworkResult.Error(e.code(), e.message())
      } catch (e: Throwable) {
        NetworkResult.Exception(e)
      }
    } else {
      return NetworkResult.Success(balanceDao.balances(addresses))
    }
  }
}