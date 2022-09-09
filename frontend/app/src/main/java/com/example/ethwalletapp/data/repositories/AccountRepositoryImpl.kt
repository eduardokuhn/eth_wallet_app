package com.example.ethwalletapp.data.repositories

import com.example.ethwalletapp.data.data_sources.daos.AccountDao
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.shared.theme.Gradient07
import org.kethereum.model.Address
import javax.inject.Inject
import javax.inject.Singleton

interface IAccountRepository {
  suspend fun getAddressIndex(): Int
  suspend fun getAllAccounts(): List<AccountEntry>
  suspend fun getAccountCount(): Int
  suspend fun saveAccount(address: Address, addressIndex: Int)
  suspend fun updateAccount(editedAccount: AccountEntry)
}

@Singleton
class AccountRepositoryImpl @Inject constructor(
  private val accountDao: AccountDao
) : IAccountRepository {
  override suspend fun getAddressIndex(): Int { return accountDao.latestAddressIndex() }

  override suspend fun getAllAccounts(): List<AccountEntry> { return accountDao.all() }

  override suspend fun getAccountCount(): Int { return accountDao.count() }

  override suspend fun saveAccount(address: Address, addressIndex: Int) {
    val account = AccountEntry(
      address = address,
      name = "Account $addressIndex",
      color = Gradient07.toString(),
      addressIndex = addressIndex
    )
    accountDao.add(account)
  }

  override suspend fun updateAccount(editedAccount: AccountEntry) { accountDao.update(editedAccount) }
}