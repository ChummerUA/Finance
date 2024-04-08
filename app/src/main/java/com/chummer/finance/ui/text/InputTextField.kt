package com.chummer.finance.ui.text

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.CombinedModifier
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chummer.finance.ui.spacing.Space
import com.chummer.finance.ui.theme.LocalColors
import com.chummer.finance.utils.OnTextChanged

@Composable
fun InputTextField(
    text: String,
    onTextChanged: OnTextChanged,
    modifier: Modifier = Modifier,
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
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1
) {
    var isFocused by rememberSaveable {
        mutableStateOf(false)
    }

    val shape = RoundedCornerShape(4.dp)
    val colors = LocalColors.current
    val targetCornerColor = if (isFocused) colors.primary else colors.backgroundSecondary
    val cornerColor by animateColorAsState(targetValue = targetCornerColor)
    val borderModifier = (if (isFocused) Modifier.border(1.dp, cornerColor, shape) else Modifier)
    val outerModifier = borderModifier
        .onFocusChanged { isFocused = it.isFocused }
        .clip(shape)
        .background(colors.backgroundSecondary)
        .padding(7.dp)
    val combinedModifier = CombinedModifier(inner = outerModifier, outer = modifier)

    InputTextField(
        text = text,
        onTextChanged = onTextChanged,
        modifier = combinedModifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines
    )
}

@Preview(showBackground = true, widthDp = 300, backgroundColor = 0xFFFFFFFF)
@Composable
fun InputTextFieldPreview() = Column(
    Modifier
        .background(color = LocalColors.current.backgroundPrimary)
        .padding(vertical = 40.dp, horizontal = 16.dp)
) {
    var text by remember {
        mutableStateOf("")
    }

    val onTextChanged: OnTextChanged = remember {
        { text = it }
    }
    FilledInputTextField(text, onTextChanged, modifier = Modifier.fillMaxWidth())
    Space(size = 16.dp)
    InputTextField(text = text, onTextChanged = onTextChanged, modifier = Modifier.fillMaxWidth(1f))
}
