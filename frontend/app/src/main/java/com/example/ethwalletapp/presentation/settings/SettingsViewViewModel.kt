package com.example.ethwalletapp.presentation.settings

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.ethwalletapp.data.data_sources.LocalAppDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.kethereum.keystore.api.KeyStore
import javax.inject.Inject
import javax.inject.Named

interface ISettingsViewViewModel {
  suspend fun resetWallet()
}

@HiltViewModel
class SettingsViewViewModel @Inject constructor(
  private val localAppDatabase: LocalAppDatabase,
  private val keyStore: KeyStore,
  @Named("sharedPreferences")
  private val sharedPreferences: SharedPreferences,
  @Named("encryptedSharedPreferences")
  private val encryptedSharedPreferences: SharedPreferences
) : ViewModel(), ISettingsViewViewModel {
  override suspend fun resetWallet() {
    localAppDatabase.accountDao().deleteAll()
    localAppDatabase.balanceDao().deleteAll()
    localAppDatabase.transactionDao().deleteAll()
    val addresses = keyStore.getAddresses()
    addresses.forEach { address ->
      keyStore.deleteKey(address)
    }
    sharedPreferences.edit().clear().commit()
    encryptedSharedPreferences.edit().clear().commit()
  }
}