package com.example.ethwalletapp.data.repositories

import com.example.ethwalletapp.data.data_sources.daos.AccountDao
import com.example.ethwalletapp.data.models.AccountEntry
import javax.inject.Inject
import javax.inject.Singleton

interface IAccountRepository {
  suspend fun getLatestAddressIndex(): Int
  suspend fun getAllAccounts(): List<AccountEntry>
  suspend fun getAccountCount(): Int
  suspend fun saveAccount(account: AccountEntry): AccountEntry
  suspend fun updateAccount(editedAccount: AccountEntry)
}

@Singleton
class AccountRepositoryImpl @Inject constructor(
  private val accountDao: AccountDao
) : IAccountRepository {
  override suspend fun getLatestAddressIndex(): Int { return accountDao.latestAddressIndex() }

  override suspend fun getAllAccounts(): List<AccountEntry> { return accountDao.all() }

  override suspend fun getAccountCount(): Int { return accountDao.count() }

  override suspend fun saveAccount(account: AccountEntry): AccountEntry {
    println("Address ${account.address}, Name: ${account.name}, Index: ${account.addressIndex}")
    accountDao.add(account)
    return account
  }

  override suspend fun updateAccount(editedAccount: AccountEntry) { accountDao.update(editedAccount) }
}