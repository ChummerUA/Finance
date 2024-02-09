package com.chummer.finance.ui.button

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chummer.finance.ui.text.ItemTitleText
import com.chummer.finance.ui.theme.LocalColors
import com.chummer.finance.utils.OnClickListener

@Composable
fun ColumnScope.PrimaryButton(
    text: String,
    onClick: OnClickListener
) = Button(
    onClick = onClick,
    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 16.dp),
    shape = RoundedCornerShape(12.dp),
    colors = ButtonDefaults.buttonColors(
        containerColor = LocalColors.current.primary,
        contentColor = LocalColors.current.onPrimary
    ),
    modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth(1f)
) {
    ItemTitleText(text = text, color = LocalColors.current.onPrimary)
}
