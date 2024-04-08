package com.chummer.finance.ui.text

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.chummer.finance.ui.theme.LocalColors
import com.chummer.finance.utils.OnTextChanged

@Composable
fun InputTextField(
    text: String,
    onTextChanged: OnTextChanged,
    modifier: Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1
) = BasicTextField(
    value = text,
    onValueChange = onTextChanged,
    modifier = modifier,
    enabled = enabled,
    readOnly = readOnly,
    textStyle = textStyle,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    singleLine = singleLine,
    maxLines = maxLines,
    minLines = minLines
)

@Composable
fun FilledInputTextField(
    text: String,
    onTextChanged: OnTextChanged,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.defaultFilledInputTextViewModifier(),
    cornerColor: Color = LocalColors.current.primary, // TODO set corner when focused
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1
) = InputTextField(
    text = text,
    onTextChanged = onTextChanged,
    modifier = modifier,
    enabled = enabled,
    readOnly = readOnly,
    textStyle = textStyle,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    singleLine = singleLine,
    maxLines = maxLines,
    minLines = minLines
)

fun Modifier.defaultFilledInputTextViewModifier() = composed {
    this
        .background(
            color = LocalColors.current.backgroundSecondary,
            shape = RoundedCornerShape(4.dp)
        )
        .padding(8.dp)
}
