package com.chummer.finance.db.mono.account

import app.cash.sqldelight.TransactionWithoutReturn
import com.chummer.infrastructure.db.useCases.transaction.withoutResult.DbTransactionUseCase
import mono.AccountQueries

class DeleteAccountsThatAreNotInListUseCase(
    transacter: AccountQueries
) : DbTransactionUseCase<List<String>, AccountQueries>(KEY, transacter) {
    override fun TransactionWithoutReturn.execute(argument: List<String>) {
        val accountIds = transacter.getOpenAccountIds().executeAsList()
        val accountsToDelete = accountIds.filter {
            !argument.contains(it)
        }
        accountsToDelete.forEach(transacter::softDeleteAccountById)
    }

    private companion object {
        const val KEY = "DELETE_ACCOUNTS_THAT_ARE_NOT_IN_LIST"
    }
}
