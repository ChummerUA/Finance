package com.chummer.finance.utils

typealias OnClickListener = (() -> Unit)

typealias TypedOnClickListener<Input, Output> = ((Input) -> Output)

typealias OnTextChanged = ((String) -> Unit)
