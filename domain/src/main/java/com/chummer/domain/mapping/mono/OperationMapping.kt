package com.chummer.domain.mapping.mono

import com.chummer.networkmodels.mono.Transaction
import mono.Operation

internal fun Transaction.toDbOperation(
    accountId: String?,
    jarId: String?
) = Operation(
    id = id,
    time = time,
    description = description,
    mcc = mcc,
    original_mcc = originalMcc,
    hold = hold,
    amount = amount,
    operation_amount = operationAmount,
    currency_code = currencyCode,
    commission_rate = commissionRate,
    cashback_amount = cashbackAmount,
    balance = balance,
    comment = comment,
    receipt_id = receiptId,
    invoice_id = invoiceId,
    counter_edrpou = counterEdrpou,
    counter_iban = counterIban,
    counter_name = counterName,
    account_id = accountId,
    jar_id = jarId
)
