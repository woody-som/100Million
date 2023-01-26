package com.taetae.taetaesocialservice.ui.screens.main

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taetae.taetaesocialservice.routes.MainRouteAction
import com.taetae.taetaesocialservice.viewmodels.AuthViewModel
import com.taetae.taetaesocialservice.viewmodels.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyPageScreen(
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    routeAction: MainRouteAction
) {
    val userId = authViewModel.currentUserIdFlow.collectAsState()
    val userEmail = authViewModel.currentUserEmailFlow.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(text = "마이페이지", fontSize = 30.sp)

        Text(text = "이메일 : ")
        Text(text = userEmail.value)

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "아이디 : ")
        Text(text = userId.value)

        TextButton(onClick = {
            coroutineScope.launch {
                authViewModel.isLoggedIn.emit(false)
                authViewModel.clearUserInfo()
            }
        }) {
            Text(text = "로그아웃")

        }
    }

}

