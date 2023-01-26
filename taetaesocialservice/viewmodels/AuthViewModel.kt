package com.taetae.taetaesocialservice.viewmodels

import AuthResponse
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taetae.taetaesocialservice.network.AuthRepository
import com.taetae.taetaesocialservice.network.UserInfo
import io.ktor.client.call.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel: ViewModel() {

    companion object {
        const val TAG = "AuthViewModel"
    }

    var isLoggedIn = MutableStateFlow<Boolean> (false)

    var isLoadingFlow = MutableStateFlow<Boolean> (false)

    var registerCompleteFlow = MutableSharedFlow<Unit>()

    var emailInputFlow = MutableStateFlow<String>("")
    var passwordInputFlow = MutableStateFlow<String>("")
    var passwordConfirmInputFlow = MutableStateFlow<String>("")

    var currentUserEmailFlow = MutableStateFlow<String> ("")
    var currentUserIdFlow = MutableStateFlow<String> ("")

    fun login(){
        viewModelScope.launch { callLogin() }
    }

    fun register(){
        viewModelScope.launch { callRegister() }
    }


    private suspend fun callRegister() {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                isLoadingFlow.emit(true)
                delay(1500)
                AuthRepository.register(
                    emailInputFlow.value,
                    passwordInputFlow.value )
            }.onSuccess {
                if (it.status.value == 200) {
                    registerCompleteFlow.emit(Unit)
                }
                clearInputs()
                isLoadingFlow.emit(false)
            }.onFailure {
                Log.d("TAG", "회원가입 실패 - onFailure error : ${it.localizedMessage}")
                isLoadingFlow.emit(false)
            }
        }
    }

    private suspend fun callLogin() {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                isLoadingFlow.emit(true)
                delay(1500)
                AuthRepository.login(
                    emailInputFlow.value,
                    passwordInputFlow.value )
            }.onSuccess {
                if (it.status.value == 200) {
                    isLoggedIn.emit(true)
                }

                val authResponse = it.body<AuthResponse>()
                UserInfo.accessToken = authResponse.accessToken ?: ""
                UserInfo.userEmail = authResponse.user?.email ?: ""
                UserInfo.userId = authResponse.user?.id ?: ""

                currentUserEmailFlow.emit(UserInfo.userEmail)
                currentUserIdFlow.emit(UserInfo.userId)

                clearInputs()
                isLoadingFlow.emit(false)
            }.onFailure {
                Log.d("TAG", "로그인 실패 - onFailure error : ${it.localizedMessage}")
                isLoadingFlow.emit(false)
            }
        }
    }


    suspend fun clearInputs() {
        emailInputFlow.emit("")
        passwordInputFlow.emit("")
        passwordConfirmInputFlow.emit("")
    }

    fun clearUserInfo(){
        UserInfo.clearData()
    }

}