package com.chummer.finance.db.mono.jar

import app.cash.sqldelight.TransactionWithoutReturn
import com.chummer.infrastructure.db.useCases.transaction.withoutResult.DbTransactionUseCase
import mono.JarQueries

class DeleteJarsThatAreNotInListUseCase(
    transacter: JarQueries
) : DbTransactionUseCase<List<String>, JarQueries>(KEY, transacter) {
    override fun TransactionWithoutReturn.execute(argument: List<String>) {
        val accountIds = transacter.getOpenJarIds().executeAsList()
        val accountsToDelete = accountIds.filter {
            !argument.contains(it)
        }
        accountsToDelete.forEach(transacter::softDeleteJarById)
    }

    private companion object {
        private const val KEY = "DELETE_JARS_THAT_ARE_NOT_IN_LIST"
    }
}
