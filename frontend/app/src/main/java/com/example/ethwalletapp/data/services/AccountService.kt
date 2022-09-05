package com.example.ethwalletapp.data.services

import org.kethereum.DEFAULT_ETHEREUM_BIP44_PATH
import org.kethereum.bip32.model.ExtendedKey
import org.kethereum.bip39.*
import org.kethereum.bip39.model.MnemonicWords
import org.kethereum.bip39.wordlists.WORDLIST_ENGLISH
import org.kethereum.model.ECKeyPair
import javax.inject.Inject
import javax.inject.Singleton

/*
1. Generate mnemonic (secret recovery phrase) with the BIP39
2. Generate seed from existing mnemonic (The seed is used to get a master key for a hierarchical deterministic wallet (or HD wallet)
3. Generate a private key from the master key
4. Get the public key derived from private key using ECDSA algorithm
5. From the public key derived, compute a keccak256 hash
6. From the keccak256, take the last 20 bytes, prefix it with "0x" and that's the Ethereum address
 */

@Singleton
class AccountService @Inject constructor() {
  private val saltPhrase: String = "Eduardo is lovely"

  fun generateSecretRecoveryPhrase(): String {
    // Create a new 12-word mnemonic phrase (secret recovery phrase)
    return generateMnemonic(128, WORDLIST_ENGLISH)
  }

  fun createMasterAccount(secretRecoveryPhrase: String): ECKeyPair {
    val mnemonicWords: MnemonicWords = dirtyPhraseToMnemonicWords(secretRecoveryPhrase)
    // Generate the master key (512bit long key) from seed of the mnemonic phrase
    // Master key path: (m/44'/60'/0'/0/0)
    val masterKey: ExtendedKey = mnemonicWords.toKey(DEFAULT_ETHEREUM_BIP44_PATH, saltPhrase)
    // Key pair from the master key (master account)
    return masterKey.keyPair
  }

  fun createChildAccount(secretRecoveryPhrase: String, addressIndex: Int): ECKeyPair {
    // From the mnemonic phrase the master key can be created
    val mnemonicWords: MnemonicWords = dirtyPhraseToMnemonicWords(secretRecoveryPhrase)
    var path: String = DEFAULT_ETHEREUM_BIP44_PATH
    // Increase the address_index (m/44'/60'/0'/0/address_index)
    path = path.substring(0, 15) + addressIndex.toString()
    // Generate the child key from the mnemonic phrase and updated path
    val childKey: ExtendedKey = mnemonicWords.toKey(path, saltPhrase)
    // Key pair from the child key
    return childKey.keyPair
  }

  fun importMasterAccount(secretRecoveryPhrase: String): ECKeyPair {
    val mnemonicWords: MnemonicWords = dirtyPhraseToMnemonicWords(secretRecoveryPhrase)
    val masterKey: ExtendedKey = mnemonicWords.toKey(DEFAULT_ETHEREUM_BIP44_PATH, saltPhrase)

    return masterKey.keyPair
  }

  fun importChildAccount(secretRecoveryPhrase: String): List<ECKeyPair> {
    var childAccounts: List<ECKeyPair> = mutableListOf()

    repeat(9) { addressIndex ->
      val mnemonicWords: MnemonicWords = dirtyPhraseToMnemonicWords(secretRecoveryPhrase)
      var path: String = DEFAULT_ETHEREUM_BIP44_PATH
      path = path.substring(0, 15) + addressIndex.toString()
      val childKey: ExtendedKey = mnemonicWords.toKey(path, saltPhrase)

      childAccounts.plus(childKey)
    }

    return childAccounts
  }
}