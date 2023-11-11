package com.chummer.finance.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chummer.finance.ui.theme.AppTheme

@Composable
fun DividerView(
    paddingValues: PaddingValues
) = Row(
    Modifier
        .fillMaxWidth(1f)
        .padding(paddingValues)
        .height(1.dp)
        .background(AppTheme.colors.divider)
) { }
