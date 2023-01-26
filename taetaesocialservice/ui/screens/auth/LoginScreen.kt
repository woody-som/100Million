package com.taetae.taetaesocialservice.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.RowScopeInstance.weight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taetae.taetaesocialservice.ui.screens.LottieAnimationView
import com.taetae.taetaesocialservice.ui.theme.Border
import com.taetae.taetaesocialservice.ui.theme.Gray
import com.taetae.taetaesocialservice.ui.theme.LightGray
import com.taetae.taetaesocialservice.R
import com.taetae.taetaesocialservice.routes.AuthRoute
import com.taetae.taetaesocialservice.routes.AuthRouteAction
import com.taetae.taetaesocialservice.ui.components.*
import com.taetae.taetaesocialservice.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    routeAction: AuthRouteAction,
    authViewModel: AuthViewModel
) {

    val emailInput = authViewModel.emailInputFlow.collectAsState()
    val passwordInput = authViewModel.passwordInputFlow.collectAsState()
    // 비밀번호를 노출하냐 안하냐에 대한 상태

    val isLoading = authViewModel.isLoadingFlow.collectAsState()

    val isLoginBtnActive = emailInput.value.isNotEmpty()
            && passwordInput.value.isNotEmpty()

    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier.padding(horizontal = 22.dp),
    ) {

        SnsBackButton(
            modifier = Modifier.padding(vertical = 20.dp),
            onClick = {
            Log.d("로그인화면","뒤로가기버튼 클릭!!")
                routeAction.goBack()
        })

        Text("로그인 화면",
            fontSize = 30.sp,
            modifier =  Modifier.padding(bottom = 30.dp)
            )

        SnsTextField(
            label = "이메일",
            keyboardType = KeyboardType.Email,
            value = emailInput.value,
            onValueChanged = {
                coroutineScope.launch {
                    authViewModel.emailInputFlow.emit(it)
                }
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

        SnsPasswordField(
            label = "비밀번호",
            value = passwordInput.value,
            onValueChanged = {
                coroutineScope.launch {
                    authViewModel.passwordInputFlow.emit(it)
                }
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        BaseButton(title = "로그인",
            enabled = isLoginBtnActive,
            isLoading = isLoading.value,
            onClick = {
                Log.d("로그인화면","로그인 버튼 클릭!!")
                authViewModel.login()
        })

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "계정이 없으신가요?")

            TextButton(onClick = {
                coroutineScope.launch {
                    authViewModel.clearInputs()
                }
                Log.d("로그인화면","회원가입하러 가야 함")
                routeAction.navTo(AuthRoute.REGISTER)
            }) {
                Text(text = "회원가입 하러가기")
            }
        }
        
//        TextButton(onClick = {
//            coroutineScope.launch { authViewModel.isLoggedIn.emit(true) }
//        }) {
//            Text(text = "로그인 완료(테스트)")
//
//        }
    }
}
