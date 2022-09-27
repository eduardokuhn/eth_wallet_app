package com.example.ethwalletapp.data.data_sources

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ethwalletapp.data.models.TransactionEntry
import com.example.ethwalletapp.data.repositories.ITransactionRepository
import com.example.ethwalletapp.shared.utils.NetworkResult
import org.kethereum.model.Address

class TransactionsPagingSource constructor(
  private val transactionRepository: ITransactionRepository,
  private val address: Address
) : PagingSource<Int, TransactionEntry>() {
  override fun getRefreshKey(state: PagingState<Int, TransactionEntry>): Int? {
    return state.anchorPosition?.let { anchorPosition ->
      val anchorPage = state.closestPageToPosition(anchorPosition)
      anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }
  }

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransactionEntry> {
    return try {
      val nextPageNumber = params.key ?: 1

      when (val resp = transactionRepository.getAddressTransactions(address, nextPageNumber)) {
        is NetworkResult.Success -> {
          LoadResult.Page(
            data = resp.data,
            prevKey = null,
            nextKey = if (resp.data.isNotEmpty()) resp.data.size + 1 else null
          )
        }
        is NetworkResult.Error -> {
          Log.e("TransactionsPagingSource.load", "Code: ${resp.code} => Message: ${resp.message}")
          LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        }
        is NetworkResult.Exception -> LoadResult.Error(resp.e)
      }
    } catch (e: Exception) {
      LoadResult.Error(e)
    }
  }
}