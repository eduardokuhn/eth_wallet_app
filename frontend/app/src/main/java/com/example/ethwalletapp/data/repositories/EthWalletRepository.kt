package com.example.ethwalletapp.data.repositories

import org.kethereum.DEFAULT_ETHEREUM_BIP44_PATH
import org.kethereum.bip32.generateChildKey
import org.kethereum.bip32.model.ExtendedKey
import org.komputing.kbip44.BIP44
import org.kethereum.bip39.*
import org.kethereum.bip39.model.MnemonicWords
import org.kethereum.bip39.wordlists.WORDLIST_ENGLISH
import org.kethereum.crypto.toAddress
import org.kethereum.model.Address
import org.kethereum.model.ECKeyPair
import org.kethereum.model.PrivateKey
import org.kethereum.model.PublicKey
import javax.inject.Inject

/*
1. Generate mnemonic (secret recovery phrase) with the BIP39
2. Generate seed from existing mnemonic (The seed is used to get a master key for a hierarchical deterministic wallet (or HD wallet)
3. Generate a private key from the seed
4. Get the public key derived from private key using ECDSA algorithm
5. From the public key derived, compute a keccak256 hash
6. From the keccak256, take the last 20 bytes, prefix it with "0x" and that's the Ethereum address
 */

class EthWalletRepository @Inject constructor() {
  private val saltPhrase: String = "Eduardo is lovely"

  fun createMasterAccount(): ECKeyPair {
    // Create a new 12-word mnemonic phrase (secret recovery phrase)
    val mnemonicPhrase: String = generateMnemonic(128, WORDLIST_ENGLISH)
    println("Mnemonic: $mnemonicPhrase")
    val mnemonicWords: MnemonicWords = dirtyPhraseToMnemonicWords(mnemonicPhrase)
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

  // Getting address from public key
  // 1. Compute a keccak256 hash from the public key
  // 2. Take the last 20 bytes from the keccak256 hash
  // 3. Convert the last 20 bytes to hexadecimal and prefix it with "0x"

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