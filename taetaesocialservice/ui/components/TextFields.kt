package com.taetae.taetaesocialservice.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taetae.taetaesocialservice.R
import com.taetae.taetaesocialservice.ui.theme.Border
import com.taetae.taetaesocialservice.ui.theme.Gray
import com.taetae.taetaesocialservice.ui.theme.LightGray

@Composable
fun SnsTextField(
    modifier: Modifier = Modifier,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    value: String,
    onValueChanged: (String) -> Unit
) {
    Column() {
        Text(text = label, color = Gray)
        Surface(
            color = LightGray,
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Border),
            shape = RoundedCornerShape(8.dp)
        ) {
            BasicTextField(
                modifier = modifier.padding(
                    horizontal = 16.dp,
                    vertical= 20.dp
                ),
                textStyle = TextStyle(fontSize = 18.sp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                value = value,
                onValueChange = onValueChanged )
        }

    }
}

@Composable
fun SnsPasswordField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit
) {
    val passwordVisible = remember { mutableStateOf(false) }
    val passwordVisibleIcon = if (passwordVisible.value) R.drawable.ic_visible else R.drawable.ic_invisible

    Column() {
        Text(text = label, color = Gray)
        Surface(
            color = LightGray,
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Border),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 20.dp
                    )
                        .weight(1f),
                    textStyle = TextStyle(fontSize = 18.sp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    value = value,
                    onValueChange = onValueChanged)
                IconButton(onClick = {
                    passwordVisible.value = !passwordVisible.value

                }) {
                    Image(
                        painter = painterResource(id = passwordVisibleIcon),
                        contentDescription = "비밀번호 보기/안보기 버튼" )

                }
            }
        }

    }
}