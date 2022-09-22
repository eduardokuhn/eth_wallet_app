package com.example.ethwalletapp.data.services

import android.content.SharedPreferences
import android.util.Log
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.TransactionEntry
import com.example.ethwalletapp.data.models.TransactionState
import com.example.ethwalletapp.data.repositories.ITransactionRepository
import com.example.ethwalletapp.shared.utils.NetworkResult
import org.kethereum.DEFAULT_GAS_LIMIT
import org.kethereum.DEFAULT_GAS_PRICE
import org.kethereum.eip155.signViaEIP155
import org.kethereum.erc681.ERC681
import org.kethereum.extensions.transactions.*
import org.kethereum.keccakshortcut.keccak
import org.kethereum.keystore.api.KeyStore
import org.kethereum.model.ChainId
import org.kethereum.model.createTransactionWithDefaults
import org.kethereum.rlp.encode
import org.komputing.khex.extensions.toHexString
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/*
  Create Transaction
  1. Create a transaction data structure, containing nine fields: nonce, gasPrice, gasLimit, to, value, data, chainID, 0, 0.
  2. Produce an RLP-encoded serialized message of the transaction data structure.
     Recursive Length Prefix (RLP) standardizes the transfer of data between nodes in a space-efficient format.
  3. Compute the Keccak-256 hash of this serialized message.
  4. Compute the ECDSA signature, signing the hash with the originating EOA’s private key.
  5. Append the ECDSA signature’s computed v, r, and s values to the transaction.
 */

interface ITransactionService {
  suspend fun createTransaction(
    from: AccountEntry,
    to: AccountEntry,
    value: BigInteger
  ): TransactionEntry?
}

@Singleton
class TransactionServiceImpl @Inject constructor(
  private val keyStore: KeyStore,
  @Named("encryptedSharedPreferences")
  private val encryptedSharedPreferences: SharedPreferences,
  private val ethereumNetworkService: IEthereumNetworkService,
  private val transactionRepository: ITransactionRepository
) : ITransactionService {
  override suspend fun createTransaction(
    from: AccountEntry,
    to: AccountEntry,
    value: BigInteger
  ): TransactionEntry? {
    val nonceResult = transactionRepository.getAddressNonce(from.address)
    val chainId = ChainId(ethereumNetworkService.selectedNetwork.chainId())

    return if (nonceResult is NetworkResult.Success) {
      // Creates transaction data structure
      val transaction = createTransactionWithDefaults(
        from = from.address,
        to = to.address,
        // TODO always 0
        value = value,
        chain = chainId,
        creationEpochSecond = System.currentTimeMillis() / 1000,
        nonce = nonceResult.data,
        gasPrice = DEFAULT_GAS_PRICE,
        gasLimit = DEFAULT_GAS_LIMIT
      )

      try {
        val password = encryptedSharedPreferences.getString("password", "")
        val signatureData = keyStore.getKeyForAddress(from.address, password = password!!)?.let {
          transaction.signViaEIP155(it, chainId)
        }
        val txHash = transaction.encode(signatureData).keccak().toHexString()
        transaction.txHash = txHash

        TransactionEntry(
          hash = transaction.txHash!!,
          transaction = transaction,
          signatureData = signatureData,
          state = TransactionState.Pending
        )
      } catch (e: Exception) {
        Log.e("TransactionServiceImpl.createTransaction", "Exception: $e")
        null
      }
    } else null
  }
}