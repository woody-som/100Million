package com.taetae.taetaesocialservice.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taetae.taetaesocialservice.network.AuthRepository
import com.taetae.taetaesocialservice.network.PostRepository
import com.taetae.taetaesocialservice.network.UserInfo
import io.ktor.client.call.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPostViewModel: ViewModel() {
    companion object {
        const val TAG = "AddPostViewModel"
    }

    var isLoadingFlow = MutableStateFlow<Boolean> (false)

    var addPostCompleteFlow = MutableSharedFlow<Unit>()

    var titleInputFlow = MutableStateFlow<String>("")
    var contentInputFlow = MutableStateFlow<String>("")

    fun addPost(){
        viewModelScope.launch { callAddPost() }
    }

    private suspend fun callAddPost() {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                isLoadingFlow.emit(true)
                delay(1500)
                PostRepository.addPostItem(
                    titleInputFlow.value,
                    contentInputFlow.value )
            }.onSuccess {
                if (it.status.value == 201) {
                    addPostCompleteFlow.emit(Unit)
                }
                clearInputs()
                isLoadingFlow.emit(false)
            }.onFailure {
                Log.d("TAG", "포스트 등록 실패 - onFailure error : ${it.localizedMessage}")
                isLoadingFlow.emit(false)
            }
        }
    }

    suspend fun clearInputs() {
        titleInputFlow.emit("")
        contentInputFlow.emit("")

    }
}