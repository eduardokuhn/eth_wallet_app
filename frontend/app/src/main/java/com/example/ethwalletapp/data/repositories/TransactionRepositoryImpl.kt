package com.example.ethwalletapp.data.repositories

import android.util.Log
import com.example.ethwalletapp.data.data_sources.IEthereumApi
import com.example.ethwalletapp.data.data_sources.daos.TransactionDao
import com.example.ethwalletapp.data.models.TransactionEntry
import com.example.ethwalletapp.data.models.TransactionState
import com.example.ethwalletapp.data.services.IConnectivityService
import com.example.ethwalletapp.data.services.IEthereumNetworkService
import com.example.ethwalletapp.shared.utils.NetworkResult
import org.kethereum.extensions.transactions.encode
import org.kethereum.model.Address
import org.kethereum.model.ChainId
import org.kethereum.model.createTransactionWithDefaults
import org.komputing.khex.extensions.toHexString
import retrofit2.HttpException
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

interface ITransactionRepository {
  suspend fun getAddressNonce(address: Address): NetworkResult<BigInteger>
  suspend fun getTransaction(hash: String): TransactionEntry
  suspend fun getAddressTransactions(address: Address, page: Int?): NetworkResult<List<TransactionEntry>>
  suspend fun getAddressPendingTransactions(address: Address): List<TransactionEntry>
  suspend fun getTransactionState(hash: String): NetworkResult<TransactionState>
  suspend fun sendTransaction(transaction: TransactionEntry): NetworkResult<String>
}

@Singleton
class TransactionRepositoryImpl @Inject constructor(
  private val ethereumApi: IEthereumApi,
  private val ethereumNetworkService: IEthereumNetworkService,
  private val transactionDao: TransactionDao,
  private val connectivityService: IConnectivityService
) : ITransactionRepository {
  override suspend fun getAddressNonce(address: Address): NetworkResult<BigInteger> {
    return if (connectivityService.hasConnection()) {
      try {
        val resp = ethereumApi.transactionCount(address.hex)
        val body = resp.body()

        if (resp.isSuccessful &&  body != null) NetworkResult.Success(BigInteger.valueOf(java.lang.Long.decode(body["result"].asString)))
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
    page: Int?
  ): NetworkResult<List<TransactionEntry>> {
    return if (connectivityService.hasConnection()) {
      try {
        val resp = ethereumApi.transactions(address.hex, page)
        val body = resp.body()

        if (resp.isSuccessful && body != null) {
          val transactions = mutableListOf<TransactionEntry>()

          body["result"].asJsonArray.forEach { json ->
            val j = json.asJsonObject
            val transaction = TransactionEntry(
              hash = j["hash"].asString,
              transaction = createTransactionWithDefaults(
                chain = ChainId(ethereumNetworkService.selectedNetwork.chainId()),
                creationEpochSecond = j["timeStamp"].asString.toLongOrNull(),
                from = Address(j["from"].asString),
                to = Address(j["to"].asString),
                value = j["value"].asString.toBigInteger(),
                nonce = j["nonce"].asString.toBigIntegerOrNull(),
                gasLimit = j["gas"].asString.toBigInteger(),
                gasPrice = j["gasPrice"].asString.toBigIntegerOrNull(),
                txHash = j["hash"].asString,
                blockHash = j["blockHash"].asString,
                blockNumber = j["blockNumber"].asString.toBigIntegerOrNull()
              ),
              state = TransactionState.fromString(j["txreceipt_status"].asString),
              signatureData = null
            )
            transactions.add(transaction)
            transactionDao.add(transaction)
          }

          NetworkResult.Success(transactions)
        } else NetworkResult.Error(resp.code(), resp.message())
      } catch (e: HttpException) {
        NetworkResult.Error(e.code(), e.message())
      } catch (e: Throwable) {
        Log.e("TransactionRepositoryImpl.getAddressTransactions", "Exception: $e")
        NetworkResult.Exception(e)
      }

    } else {
      NetworkResult.Success(transactionDao.transactions(address, ethereumNetworkService.selectedNetwork.chainId()))
    }
  }

  override suspend fun getAddressPendingTransactions(address: Address): List<TransactionEntry> {
    return transactionDao.transactionsByState(address, ethereumNetworkService.selectedNetwork.chainId(), TransactionState.Pending)
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
    } else NetworkResult.Error(503, "No internet connection")
  }

  override suspend fun sendTransaction(transaction: TransactionEntry): NetworkResult<String> {
    return if (connectivityService.hasConnection()) {
      val rawTransaction = transaction.transaction.encode(transaction.signatureData).toHexString()
      val resp = ethereumApi.sendRawTransaction(rawTransaction)
      val body = resp.body()

      try {
        if (resp.isSuccessful && body != null && body["result"] != null) {
          transactionDao.add(transaction)
          NetworkResult.Success(body["result"].asString)
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