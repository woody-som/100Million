package com.taetae.taetaesocialservice.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taetae.taetaesocialservice.routes.AuthRoute
import com.taetae.taetaesocialservice.routes.AuthRouteAction
import com.taetae.taetaesocialservice.ui.components.BaseButton
import com.taetae.taetaesocialservice.ui.components.SnsBackButton
import com.taetae.taetaesocialservice.ui.components.SnsPasswordField
import com.taetae.taetaesocialservice.ui.components.SnsTextField
import com.taetae.taetaesocialservice.viewmodels.AuthViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    routeAction: AuthRouteAction) {

    val emailInput = authViewModel.emailInputFlow.collectAsState()
    val passwordInput = authViewModel.passwordInputFlow.collectAsState()
    val passwordConfirmInput = authViewModel.passwordConfirmInputFlow.collectAsState()

    val isPasswordInputEmpty = passwordInput.value.isNotEmpty() && passwordConfirmInput.value.isNotEmpty()
    val isPasswordConfirmed = passwordInput.value == passwordConfirmInput.value
    // 비밀번호를 노출하냐 안하냐에 대한 상태
    val isRegisterBtnActive = emailInput.value.isNotEmpty()
            && isPasswordInputEmpty && isPasswordConfirmed

    val scrollState = rememberScrollState()

    val snackbarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()

    val isLoading = authViewModel.isLoadingFlow.collectAsState()

    LaunchedEffect(key1 = Unit, block = {
        // 회원가입 성공 이벤트
        authViewModel.registerCompleteFlow.collectLatest {
            snackbarHostState
                .showSnackbar(
                    "회원가입 완료! 로그인 해주세요!!",
                    actionLabel = "확인", SnackbarDuration.Short)
                .let {
                    when(it) {
                        SnackbarResult.Dismissed -> Log.d("TAG","스낵바 닫힘")
                        SnackbarResult.ActionPerformed -> {
                            routeAction.navTo(AuthRoute.LOGIN)
                        }
                    }
                }
        }
    })


    Column(
        modifier = Modifier
            .verticalScroll(scrollState, enabled = true)
            .padding(horizontal = 22.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        SnsBackButton(
            modifier = Modifier.padding(top = 20.dp),
            onClick = {
                Log.d("회원가입화면","뒤로가기버튼 클릭!!")
                routeAction.goBack()
            })

        Text("회원가입 화면",
            fontSize = 30.sp,
            modifier =  Modifier.padding(bottom = 10.dp)
        )

        SnsTextField(
            label = "이메일",
            keyboardType = KeyboardType.Email,
            value = emailInput.value,
            onValueChanged = {
                coroutineScope.launch { authViewModel.emailInputFlow.emit(it) }
                }
        )

        SnsPasswordField(
            label = "비밀번호",
            value = passwordInput.value,
            onValueChanged = {
                coroutineScope.launch { authViewModel.passwordInputFlow.emit(it) }
                }
        )

        SnsPasswordField(
            label = "비밀번호 확인",
            value = passwordConfirmInput.value,
            onValueChanged = {
                coroutineScope.launch { authViewModel.passwordConfirmInputFlow.emit(it) }
            }
        )

        Spacer(modifier = Modifier.height(15.dp))

        BaseButton(title = "회원가입",
            enabled = isRegisterBtnActive,
            isLoading = isLoading.value,
            onClick = {
                Log.d("회원가입화면","회원가입 버튼 클릭!!")
                if (!isLoading.value) {
                    authViewModel.register()
                }
            })

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "이미 계정이 있으신가요?")

            TextButton(onClick = {
                Log.d("회원가입화면","로그인하러 가야 함")
                coroutineScope.launch {  authViewModel.clearInputs() }
                routeAction.navTo(AuthRoute.LOGIN)
            }) {
                Text(text = "로그인 하러가기")

            }
        }
        SnackbarHost(hostState = snackbarHostState)
    }


}
