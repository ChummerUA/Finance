package com.chummer.finance.ui.text

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.chummer.finance.ui.theme.AppTheme

//region prices
@Composable
fun LargePriceText(
    text: String,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    style = TextStyle(
        fontSize = 36.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight(400),
        color = AppTheme.colors.textPrimary
    ),
    modifier = modifier
)

@Composable
fun MediumPriceText(text: String) = BasicText(
    text = text,
    style = TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight(400),
        color = AppTheme.colors.textPrimary
    )
)
//endregion

//region titles
@Composable
fun ItemTitleText(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    style = TextStyle(
        fontSize = 12.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight(400),
        color = color
    ),
    modifier = modifier
)

@Composable
fun GroupTitleText(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    style = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight(400),
        color = color
    ),
    modifier = modifier
)
//endregion

//region description
@Composable
fun GroupDescriptionText(
    text: String,
    color: Color
) = BasicText(
    text = text,
    style = TextStyle(
        fontSize = 12.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight(400),
        color = color
    )
)

@Composable
fun ItemDescriptionText(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    style = TextStyle(
        fontSize = 10.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight(400),
        color = color
    ),
    modifier = modifier
)

@Composable
fun ClickableText(
    text: String,
    color: Color
) = BasicText(
    text = text,
    style = TextStyle(
        fontSize = 12.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight(400),
        color = color
    )
)
//endregion
