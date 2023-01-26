package com.taetae.taetaesocialservice.viewmodels

import Post
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taetae.taetaesocialservice.network.AuthRepository
import com.taetae.taetaesocialservice.network.PostRepository
import com.taetae.taetaesocialservice.routes.MainRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    var navAction = MutableSharedFlow<MainRoute>()

    var dataUpdatedFlow = MutableSharedFlow<Unit>()

    val isLoadingFlow = MutableStateFlow<Boolean>(false)

    val postsFlow = MutableStateFlow<List<Post>>(emptyList())

    companion object {
        const val TAG = "홈뷰모델"
    }
    init {
        fetchPosts()
    }

     fun fetchPosts() {
        viewModelScope.launch {  callFetchPosts()  }
    }
    // private 되어 있는 걸 풀리프레시 때문에 삭제함, 오류시 수정
    private suspend fun callFetchPosts() {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                isLoadingFlow.emit(true)
                delay(1500)
                PostRepository.fetchAllPosts( )
            }.onSuccess {
                postsFlow.emit(it)
                dataUpdatedFlow.emit(Unit)
                isLoadingFlow.emit(false)
            }.onFailure {
                Log.d("TAG", "실패 - onFailure error : ${it.localizedMessage}")
                isLoadingFlow.emit(false)
            }
        }
    }
}

