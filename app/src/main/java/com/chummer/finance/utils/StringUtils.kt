package com.chummer.finance.utils

import android.content.Context
import com.chummer.finance.R
import com.chummer.networkmodels.mono.CARD_TYPE_BLACK
import com.chummer.networkmodels.mono.CARD_TYPE_EAID
import com.chummer.networkmodels.mono.CARD_TYPE_IRON
import com.chummer.networkmodels.mono.CARD_TYPE_PLATINUM
import com.chummer.networkmodels.mono.CARD_TYPE_WHITE
import com.chummer.networkmodels.mono.CARD_TYPE_YELLOW
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Currency
import java.util.Locale

fun getFormattedAmountAndCurrency(amount: Long, currencyCode: Int): String {
    val format = NumberFormat.getCurrencyInstance()
    val currency = getCurrencyByNumericCode(currencyCode) ?: return TODO("")

    format.currency = currency
    val formattedAmount = getFormattedAmount(amount)
    return format.format(formattedAmount)
}

private fun getFormattedAmount(amount: Long): Double {
    return amount / 100.0
}

fun getAccountName(currencyCode: Int): String {
    return getCurrencyByNumericCode(currencyCode)
        ?.getDisplayName(Locale.getDefault())
        ?.uppercase()
        ?.replaceFirstChar(Char::titlecaseChar)
        ?: ""
}

fun getAccountName(currencyCode: Int, type: String, context: Context): String {
    val id = when (type) {
        CARD_TYPE_EAID -> R.string.card_e_aid
        CARD_TYPE_IRON -> R.string.card_iron
        CARD_TYPE_WHITE -> R.string.card_white
        CARD_TYPE_PLATINUM -> R.string.card_platinum
        CARD_TYPE_YELLOW -> R.string.card_yellow
        CARD_TYPE_BLACK -> R.string.card_black
        else -> 0
    }
    return if (id != 0)
        context.getString(id)
    else getCurrencyByNumericCode(currencyCode)?.getDisplayName(Locale.getDefault()) ?: ""
}

private fun getCurrencyByNumericCode(code: Int): Currency? {
    val currencies = Currency.getAvailableCurrencies()
    return currencies.firstOrNull {
        it.numericCode == code
    }
}

fun LocalDateTime.toTimeString(): String = format(DateTimeFormatter.ofPattern("H:mm"))

fun LocalDate.toDateString(): String {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    return format(formatter)
}

fun LocalDateTime.toDateTimeString() = "${toLocalDate().toDateString()} ${toTimeString()}"
