package com.example.ethwalletapp.data.services

import android.util.Log
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.TransactionEntry
import com.example.ethwalletapp.data.models.TransactionState
import com.example.ethwalletapp.data.repositories.ITransactionRepository
import com.example.ethwalletapp.shared.utils.NetworkResult
import org.kethereum.DEFAULT_GAS_LIMIT
import org.kethereum.DEFAULT_GAS_PRICE
import org.kethereum.extensions.transactions.encode
import org.kethereum.extensions.transactions.toTransaction
import org.kethereum.extensions.transactions.toTransactionSignatureData
import org.kethereum.keccakshortcut.keccak
import org.kethereum.model.ChainId
import org.kethereum.model.SignatureData
import org.kethereum.model.Transaction
import org.kethereum.model.createTransactionWithDefaults
import org.kethereum.rlp.RLPList
import org.kethereum.rlp.RLPType
import org.kethereum.rlp.decodeRLP
import org.kethereum.rlp.encode
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.extensions.toHexString
import org.komputing.khex.model.HexString
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
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
    value: Double
  ): TransactionEntry?
}

@Singleton
class TransactionServiceImpl @Inject constructor(
  private val ethereumNetworkService: IEthereumNetworkService,
  private val transactionRepository: ITransactionRepository
) : ITransactionService {
  override suspend fun createTransaction(
    from: AccountEntry,
    to: AccountEntry,
    value: Double
  ): TransactionEntry? {
    val nonceResult = transactionRepository.getAddressNonce(from.address)

    return if (nonceResult is NetworkResult.Success) {
      // Creates transaction data structure
      val transactionDataStructure = createTransactionWithDefaults(
        chain = ChainId(ethereumNetworkService.selectedNetwork.chainId()),
        creationEpochSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
        from = from.address,
        to = to.address,
        value = BigInteger.valueOf(value.toLong()),
        gasLimit = DEFAULT_GAS_LIMIT,
        gasPrice = DEFAULT_GAS_PRICE,
        nonce = nonceResult.data
      )

      try {
        // Encodes transaction data structure with RLP and returns the RLP serialized message
        val transactionRLP = HexString(transactionDataStructure.encode().toHexString()).hexToByteArray()
        val transactionRLPList = transactionRLP.decodeRLP() as RLPList
        require(transactionRLPList.element.size == 9) { "RLP list has the wrong size ${transactionRLPList.element.size} != 9" }

        val signatureData = transactionRLPList.toTransactionSignatureData()
        val transaction = transactionRLPList.toTransaction()?.apply {
          txHash = transactionRLPList.encode().keccak().toHexString()
        }

        val transactionState = TransactionState.Pending

        if (transaction?.txHash != null) {
          TransactionEntry(
            hash = transaction.txHash!!,
            transaction = transaction,
            signatureData = signatureData,
            state = transactionState
          )
        } else null
      } catch (e: Exception) {
        Log.e("TransactionServiceImpl.createTransaction", "Exception: $e")
        null
      }
    } else null
  }
}