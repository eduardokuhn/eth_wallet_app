package com.example.ethwalletapp.data.repositories

import com.example.ethwalletapp.data.data_sources.IEthereumApi
import com.example.ethwalletapp.data.data_sources.LocalAppDatabase
import com.example.ethwalletapp.data.data_sources.daos.BalanceDao
import com.example.ethwalletapp.data.models.BalanceEntry
import com.example.ethwalletapp.shared.utils.ApiResult
import com.example.ethwalletapp.shared.utils.EthereumUnitConverter
import org.kethereum.model.Address
import retrofit2.HttpException
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

interface IBalanceRepository {
  suspend fun getAddressBalance(address: Address): ApiResult<BalanceEntry>
  suspend fun getAddressesBalance(addresses: List<Address>)
}

@Singleton
class BalanceRepositoryImpl @Inject constructor(
  private val ethereumApi: IEthereumApi,
  private val balanceDao: BalanceDao
): IBalanceRepository {
  override suspend fun getAddressBalance(address: Address): ApiResult<BalanceEntry> {
    val resp = ethereumApi.balance(address.hex)
    val body = resp.body()

    return try {
      if (resp.isSuccessful && body != null) {
        val balance = BalanceEntry(
          address = address,
          chainId = BigInteger.valueOf(0),
          balance = body["result"].asBigInteger
        )
        balanceDao.add(balance)
        ApiResult.Success(balance)
      } else {
        ApiResult.Error(resp.code(), resp.message())
      }
    } catch (e: HttpException) {
      ApiResult.Error(e.code(), e.message())
    } catch (e: Throwable) {
      ApiResult.Exception(e)
    }
  }

  override suspend fun getAddressesBalance(addresses: List<Address>) {
    TODO("Not yet implemented")
  }
}