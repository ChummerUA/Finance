package com.chummer.finance.db.mono.jar

import app.cash.sqldelight.TransactionWithoutReturn
import com.chummer.infrastructure.db.useCases.transaction.withoutResult.DbTransactionUseCase
import mono.Jar
import mono.JarQueries

class UpsertJarsUseCase(
    queries: JarQueries
) : DbTransactionUseCase<List<Jar>, JarQueries>(KEY, queries) {
    override fun TransactionWithoutReturn.execute(argument: List<Jar>) {
        argument.forEach {
            transacter.upsertJar(
                id = it.id,
                title = it.title,
                description = it.description,
                currencyCode = it.currency_code,
                balance = it.balance,
                goal = it.goal,
                clientId = it.client_id
            )
        }
    }
}

private const val KEY = "upsert_jars"
