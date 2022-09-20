package com.example.ethwalletapp.data.repositories

import android.util.Log
import com.example.ethwalletapp.data.data_sources.IEthereumApi
import com.example.ethwalletapp.data.data_sources.daos.TransactionDao
import com.example.ethwalletapp.data.models.TransactionEntry
import com.example.ethwalletapp.data.models.TransactionState
import com.example.ethwalletapp.data.services.IConnectivityService
import com.example.ethwalletapp.shared.utils.NetworkResult
import org.kethereum.extensions.transactions.encode
import org.kethereum.model.Address
import org.komputing.khex.extensions.toHexString
import retrofit2.HttpException
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

interface ITransactionRepository {
  suspend fun getAddressNonce(address: Address): NetworkResult<BigInteger>
  suspend fun getTransaction(hash: String): TransactionEntry
  suspend fun getAddressTransactions(address: Address, chainId: BigInteger): List<TransactionEntry>
  suspend fun getTransactionState(hash: String): NetworkResult<TransactionState>
  suspend fun sendTransaction(transaction: TransactionEntry): NetworkResult<Boolean>
}

@Singleton
class TransactionRepositoryImpl @Inject constructor(
  private val ethereumApi: IEthereumApi,
  private val transactionDao: TransactionDao,
  private val connectivityService: IConnectivityService
) : ITransactionRepository {
  override suspend fun getAddressNonce(address: Address): NetworkResult<BigInteger> {
    return if (connectivityService.hasConnection()) {
      try {
        val resp = ethereumApi.transactionCount(address.hex)
        val body = resp.body()

        if (resp.isSuccessful &&  body != null) NetworkResult.Success(BigInteger(body["result"].asString.removePrefix("0x")))
        else NetworkResult.Error(resp.code(), resp.message())
      } catch (e: HttpException) {
        NetworkResult.Error(e.code(), e.message())
      } catch (e: Throwable) {
        Log.e("TransactionRepositoryImpl.getAddressNonce", "Exception: $e")
        NetworkResult.Exception(e)
      }
    } else {
      NetworkResult.Error(503, "No internet connection")
    }
  }

  override suspend fun getTransaction(hash: String): TransactionEntry {
    return transactionDao.transaction(hash)
  }

  override suspend fun getAddressTransactions(
    address: Address,
    chainId: BigInteger
  ): List<TransactionEntry> {
    return transactionDao.transactions(address, chainId)
  }

  override suspend fun getTransactionState(hash: String): NetworkResult<TransactionState> {
    return if (connectivityService.hasConnection()) {
      try {
        val resp = ethereumApi.transactionReceiptStatus(hash)
        val body = resp.body()

        if (resp.isSuccessful && body != null) {
          val j = body["result"].asJsonObject
          NetworkResult.Success(TransactionState.fromString(j["status"].asString))
        } else {
          NetworkResult.Error(resp.code(), resp.message())
        }
      } catch (e: HttpException) {
        NetworkResult.Error(e.code(), e.message())
      } catch (e: Throwable) {
        Log.e("TransactionRepositoryImpl.getTransactionState", "Exception: $e")
        NetworkResult.Exception(e)
      }
    }  else {
      NetworkResult.Error(503, "No internet connection")
    }
  }

  override suspend fun sendTransaction(transaction: TransactionEntry): NetworkResult<Boolean> {
    return if (connectivityService.hasConnection()) {
      val rawTransaction = transaction.transaction.encode(transaction.signatureData).toHexString()
      val resp = ethereumApi.sendRawTransaction(rawTransaction)
      val body = resp.body()

      try {
        if (resp.isSuccessful && body != null) {
          transactionDao.add(transaction)
          NetworkResult.Success(true)
        }
        else NetworkResult.Error(resp.code(), resp.message())
      } catch (e: HttpException) {
        NetworkResult.Error(e.code(), e.message())
      } catch (e: Throwable) {
        Log.e("TransactionRepositoryImpl.sendTransaction", "Exception: $e")
        NetworkResult.Exception(e)
      }
    } else {
      NetworkResult.Error(503, "No internet connection")
    }
  }
}