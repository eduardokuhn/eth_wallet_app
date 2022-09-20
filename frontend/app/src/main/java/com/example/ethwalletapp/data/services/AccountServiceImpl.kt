package com.example.ethwalletapp.data.services

import android.content.SharedPreferences
import android.util.Log
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.repositories.IAccountRepository
import org.kethereum.DEFAULT_ETHEREUM_BIP44_PATH
import org.kethereum.bip32.model.ExtendedKey
import org.kethereum.bip39.dirtyPhraseToMnemonicWords
import org.kethereum.bip39.generateMnemonic
import org.kethereum.bip39.model.MnemonicWords
import org.kethereum.bip39.toKey
import org.kethereum.bip39.validate
import org.kethereum.bip39.wordlists.WORDLIST_ENGLISH
import org.kethereum.crypto.toAddress
import org.kethereum.crypto.toECKeyPair
import org.kethereum.eip191.signWithEIP191PersonalSign
import org.kethereum.erc55.isValid
import org.kethereum.keystore.api.KeyStore
import org.kethereum.model.Address
import org.kethereum.model.ECKeyPair
import org.kethereum.model.PrivateKey
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/*
  Create Wallet Steps
  1. Generate mnemonic (secret recovery phrase) with the BIP39
  2. Generate seed from existing mnemonic (The seed is used to get a master key for a hierarchical deterministic wallet (or HD wallet)
  3. Generate a private key from the master key
  4. Get the public key derived from private key using ECDSA algorithm
  5. From the public key derived, compute a keccak256 hash
  6. From the keccak256, take the last 20 bytes, prefix it with "0x" and that's the Ethereum address
 */

interface IAccountService {
  fun generateSecretRecoveryPhrase(): String
  fun validateSecretRecoveryPhrase(secretRecoveryPhrase: String): Boolean
  fun validateAddress(address: String): Boolean
  suspend fun createMasterAccount(secretRecoveryPhrase: String, password: String)
  suspend fun createChildAccount(name: String?): AccountEntry?
  suspend fun importMasterAccount(secretRecoveryPhrase: String, password: String)
  suspend fun importChildAccount(): AccountEntry?
  suspend fun importExternAccount(privateKey: String): AccountEntry?
  suspend fun hasAccount(): Boolean
}

@Singleton
class AccountServiceImpl @Inject constructor(
  private val keyStore: KeyStore,
  @Named("encryptedSharedPreferences")
  private val encryptedSharedPreferences: SharedPreferences,
  private val accountRepository: IAccountRepository
) : IAccountService {
  private val saltPhrase: String = "Bitcoin Seed"

  override fun generateSecretRecoveryPhrase(): String {
    // Create a new 12-word mnemonic phrase (secret recovery phrase)
    return generateMnemonic(128, WORDLIST_ENGLISH)
  }

  override fun validateSecretRecoveryPhrase(secretRecoveryPhrase: String): Boolean {
    val mnemonicWords: MnemonicWords = dirtyPhraseToMnemonicWords(secretRecoveryPhrase)
    return mnemonicWords.validate(WORDLIST_ENGLISH)
  }

  override fun validateAddress(address: String): Boolean {
    return Address(address).isValid()
  }

  private fun ECKeyPair.isValid() = try {
    signWithEIP191PersonalSign("test".toByteArray())
    true
  } catch (e:java.lang.Exception) {
    false
  }

  override suspend fun createMasterAccount(secretRecoveryPhrase: String, password: String) {
    val mnemonicWords: MnemonicWords = dirtyPhraseToMnemonicWords(secretRecoveryPhrase)
    // Generate the master key (512bit long key) from seed of the mnemonic phrase
    // Master key path: (m/44'/60'/0'/0/0)
    val keyPair: ECKeyPair = mnemonicWords.toKey(DEFAULT_ETHEREUM_BIP44_PATH, saltPhrase).keyPair
    keyStore.addKey(keyPair, password, true)
    encryptedSharedPreferences.edit()
      .putString("srp", secretRecoveryPhrase)
      .putString("password", password)
      .apply()
    accountRepository.saveAccount(
      AccountEntry(
        address = keyPair.toAddress(),
        name = "Account 1",
        addressIndex = 0
      )
    )
  }

  override suspend fun createChildAccount(name: String?): AccountEntry? {
    // From the mnemonic phrase the master key can be created
    val secretRecoveryPhrase = encryptedSharedPreferences.getString("srp", "") ?: ""
    val password = encryptedSharedPreferences.getString("password", "") ?: ""

    return if (secretRecoveryPhrase.isNotEmpty() && password.isNotEmpty()) {
      val mnemonicWords: MnemonicWords = dirtyPhraseToMnemonicWords(secretRecoveryPhrase)
      val addressIndex: Int = accountRepository.getLatestAddressIndex() + 1
      var path: String = DEFAULT_ETHEREUM_BIP44_PATH
      // Increase the address_index (m/44'/60'/0'/0/address_index)
      path = path.substring(0, 15) + (addressIndex).toString()
      // Generate the child key from the mnemonic phrase and updated path
      val keyPair: ECKeyPair = mnemonicWords.toKey(path, saltPhrase).keyPair
      keyStore.addKey(keyPair, password, true)
      accountRepository.saveAccount(
        AccountEntry(
          address = keyPair.toAddress(),
          name = name ?: "Account ${addressIndex + 1}",
          addressIndex = addressIndex
        )
      )
    } else {
      Log.e("AccountServiceImpl.createChildAccount", "SRP and password fields are null")
      null
    }
  }

  override suspend fun importMasterAccount(secretRecoveryPhrase: String, password: String) {
    val mnemonicWords: MnemonicWords = dirtyPhraseToMnemonicWords(secretRecoveryPhrase)
    val keyPair: ECKeyPair = mnemonicWords.toKey(DEFAULT_ETHEREUM_BIP44_PATH, saltPhrase).keyPair
    keyStore.addKey(keyPair, password, true)
    encryptedSharedPreferences.edit()
      .putString("srp", secretRecoveryPhrase)
      .putString("password", password)
      .apply()
    accountRepository.saveAccount(
      AccountEntry(
        address = keyPair.toAddress(),
        name = "Account 1",
        addressIndex = 0
      )
    )
  }

  override suspend fun importChildAccount(): AccountEntry? {
    TODO("Not yet implemented")
  }

  override suspend fun importExternAccount(privateKey: String): AccountEntry? {
    try {
      val importedKey = PrivateKey(HexString(privateKey).hexToByteArray()).toECKeyPair()
      return if (importedKey.isValid()) {
        println("Private key: ${importedKey.privateKey}")
        val password = encryptedSharedPreferences.getString("password", "") ?: ""
        val addressIndex: Int = accountRepository.getLatestAddressIndex() + 1
        keyStore.addKey(importedKey, password, light = true)
        return accountRepository.saveAccount(
          AccountEntry(
            address = importedKey.toAddress(),
            name = "Imported account ${addressIndex + 1}",
            addressIndex= addressIndex
          )
        )
      } else null
    } catch (e: IllegalArgumentException) {
      return null
    }
  }

  // TODO do
  suspend fun importChildAccount(secretRecoveryPhrase: String, password: String) {
    repeat(9) { addressIndex ->
      val mnemonicWords: MnemonicWords = dirtyPhraseToMnemonicWords(secretRecoveryPhrase)
      var path: String = DEFAULT_ETHEREUM_BIP44_PATH
      path = path.substring(0, 15) + addressIndex.toString()
      val childKey: ExtendedKey = mnemonicWords.toKey(path, saltPhrase)
      keyStore.addKey(childKey.keyPair, password, true)
    }
  }

  override suspend fun hasAccount(): Boolean {
    return accountRepository.getAccountCount() >= 1
  }
}