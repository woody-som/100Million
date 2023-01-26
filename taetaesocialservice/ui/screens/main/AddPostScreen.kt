package com.taetae.taetaesocialservice.ui.screens.main

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taetae.taetaesocialservice.MainActivity
import com.taetae.taetaesocialservice.routes.ActivityCloseAction
import com.taetae.taetaesocialservice.routes.ActivityCloseActionName
import com.taetae.taetaesocialservice.routes.AuthRouteAction
import com.taetae.taetaesocialservice.ui.components.BaseButton
import com.taetae.taetaesocialservice.ui.components.SnsBackButton
import com.taetae.taetaesocialservice.ui.components.SnsPasswordField
import com.taetae.taetaesocialservice.ui.components.SnsTextField
import com.taetae.taetaesocialservice.viewmodels.AddPostViewModel
import com.taetae.taetaesocialservice.viewmodels.AddPostViewModel.Companion.TAG
import com.taetae.taetaesocialservice.viewmodels.AuthViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddPostScreen(
    addPostViewModel: AddPostViewModel
) {
    val titleInput = addPostViewModel.titleInputFlow.collectAsState()
    val contentInput = addPostViewModel.contentInputFlow.collectAsState()
    // 타이틀만 입력되면 포스트 올리기 버튼 활성화 됨
    val isAddPostBtnActive = titleInput.value.isNotEmpty()

    val isLoading = addPostViewModel.isLoadingFlow.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val snackbarHostState = remember { SnackbarHostState() }

    val activity = (LocalContext.current as? Activity)
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit, block = {
        addPostViewModel.addPostCompleteFlow.collectLatest {
            snackbarHostState
                .showSnackbar("포스트가 등록 되었습니다.",
                actionLabel = "홈으로", SnackbarDuration.Short
                ).let {
                    when(it) {
                        SnackbarResult.Dismissed -> Log.d(TAG,"스낵바가 닫혔다")
                        SnackbarResult.ActionPerformed -> {
                            val intent = Intent(context, MainActivity::class.java).apply {
                                putExtra(ActivityCloseActionName, ActivityCloseAction.POST_ADDED.actionName)
                            }
                            activity?.setResult(RESULT_OK, intent)
                            activity?.finish()
                        }
                    }
                }
        }
    })


    Column(
        modifier = Modifier
            .padding(horizontal = 22.dp)
            .verticalScroll(scrollState, enabled = true)
    ) {
        Text(
            "포스트 추가하기",
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        SnsTextField(
            label = "제목",
            value = titleInput.value,
            onValueChanged = {
                coroutineScope.launch{
                    addPostViewModel.titleInputFlow.emit(it)
                }
            })

        Spacer(modifier = Modifier.height(15.dp))

        SnsTextField(
            modifier = Modifier.height(300.dp),
            label = "내용",
            value = contentInput.value,
            singleLine = false,
            onValueChanged = {
                coroutineScope.launch{
                    addPostViewModel.contentInputFlow.emit(it)
                }
            }
        )
        Spacer(modifier = Modifier.height(40.dp))

        BaseButton(title = "포스트 올리기",
            enabled = isAddPostBtnActive,
            isLoading = isLoading.value,
            onClick = {
                Log.d("포스트 추가 화면", "포스트 올리기 버튼 클릭!!")
                if (!isLoading.value) {
                    addPostViewModel.addPost()
                }

            })
        Spacer(modifier = Modifier.weight(1f))

        SnackbarHost(hostState = snackbarHostState)
    }
}