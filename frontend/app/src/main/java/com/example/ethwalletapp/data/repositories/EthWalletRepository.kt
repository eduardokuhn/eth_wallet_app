package com.example.ethwalletapp.data.repositories

import cash.z.ecc.android.bip39.Mnemonics
import cash.z.ecc.android.bip39.toSeed
import org.komputing.khash.keccak.Keccak
import org.komputing.khash.keccak.KeccakParameter
import org.komputing.khash.sha256.Sha256
import org.komputing.khex.extensions.toHexString
import java.math.BigInteger
import java.security.*
import java.security.spec.ECParameterSpec
import java.security.spec.ECPrivateKeySpec
import java.security.spec.KeySpec
import java.security.spec.PKCS8EncodedKeySpec
import javax.inject.Inject

/*
1. Generate mnemonic (secret recovery phrase) with the BIP39
2. Generate seed from existing mnemonic
3. Generate a private key from the seed
4. Get the public key derived from private key using ECDSA algorithm
5. From the public key derived, compute a keccak256 hash
6. From the keccak256, take the last 20 bytes, prefix it with "0x" and that's the Ethereum address
 */

class EthWalletRepository @Inject constructor() {
  fun createDefaultAccount(password: String): KeyPair {
    // Create a new 12-word mnemonic phrase (secret recovery phrase)
    val mnemonicCode: Mnemonics.MnemonicCode = Mnemonics.MnemonicCode(Mnemonics.WordCount.COUNT_12)
    // Generate seed with password
    val seed: ByteArray = mnemonicCode.toSeed(password.toCharArray())
    // Delete the mnemonic from memory
    mnemonicCode.clear()

    val keyGenerator: KeyPairGenerator = KeyPairGenerator.getInstance("EC")
    // Randomness of the private key
    val random: SecureRandom = SecureRandom.getInstance("SHA1PRNG")
    random.setSeed(seed)
    keyGenerator.initialize(256, random)

    return keyGenerator.generateKeyPair()
  }

  fun createAdditionalAccount(previousPrivateKey: PrivateKey): KeyPair {
    // Hash the previous private key to get a new raw private key related to the previous one
    val encodedPrivateKey: ByteArray = Sha256.digest(previousPrivateKey.encoded)

    val keyFactory = KeyFactory.getInstance("EC")

    // Generate the public key from raw private key
    val publicKey: PublicKey = keyFactory.generatePublic(PKCS8EncodedKeySpec(encodedPrivateKey))
    // Generate the private key from raw
    val privateKey: PrivateKey = keyFactory.generatePrivate(PKCS8EncodedKeySpec(encodedPrivateKey))

    return KeyPair(publicKey, privateKey)
  }

  fun addressFromPublicKey(publicKey: PublicKey): String {
    // Compute a keccak256 hash from the public key
    val keccak256: ByteArray = Keccak.digest(publicKey.encoded, KeccakParameter.KECCAK_256)
    // Take the last 20 bytes from the keccak256 hash
    val address: ByteArray = keccak256.copyOfRange(fromIndex = 12, toIndex = 32)
    // Convert the last 20 bytes to hexadecimal and prefix it with "0x"
    return address.toHexString()
  }
}