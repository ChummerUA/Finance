package com.chummer.finance.db.mono.jar

import app.cash.sqldelight.Query
import com.chummer.infrastructure.db.useCases.flow.DbListFlowUseCase
import com.chummer.models.None
import mono.JarQueries

class GetJarsUseCase(
    queries: JarQueries
): DbListFlowUseCase<None, JarListItem, JarQueries>(KEY, queries) {

    override fun JarQueries.getQuery(argument: None): Query<JarListItem> {
        return getJars { id, title, currencyCode, balance, goal ->
            JarListItem(
                id = id,
                name = title,
                balance = balance,
                goal = goal,
                currencyCode = currencyCode
            )
        }
    }
}

private const val KEY = "get_jars"
