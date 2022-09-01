package com.example.ethwalletapp.data.repositories

import cash.z.ecc.android.bip39.Mnemonics
import cash.z.ecc.android.bip39.toSeed
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.spec.ECParameterSpec

class EthWalletRepository {
  fun createDefaultAccount(password: String) {
    // Create a new 12-word mnemonic phrase (secret recovery phrase)
    val mnemonicCode: Mnemonics.MnemonicCode = Mnemonics.MnemonicCode(Mnemonics.WordCount.COUNT_12)
    // Generate seed with password
    val seed: ByteArray = mnemonicCode.toSeed(password.toCharArray())

    val keyGenerator: KeyPairGenerator = KeyPairGenerator.getInstance("EC")
    val random: SecureRandom = SecureRandom.getInstance("SHA1PRNG")
    random.setSeed(seed)
    keyGenerator.initialize(256, random)

    val keyPair: KeyPair = keyGenerator.generateKeyPair()

    print("Private key: ${keyPair.private}")
    print("Public key: ${keyPair.public}")

    // Delete the mnemonic from memory
    mnemonicCode.clear()
  }
}

/*
1. Generate secret recovery phrase (BIP39 mnemonic)
2. Generate seed from existing mnemonic
3. Generate a private key from the seed
4. Get the public key derived from private key using ECDSA algorithm
5. From the public key derived, compute a keccak256 hash
6. From the keccak256, take the last 20 bytes, prefix it with "0x" and that's the Ethereum address
 */