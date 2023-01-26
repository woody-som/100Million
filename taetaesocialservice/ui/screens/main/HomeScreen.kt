package com.taetae.taetaesocialservice.ui.screens.main


import Post
import android.util.Log
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taetae.taetaesocialservice.routes.AuthRouteAction
import com.taetae.taetaesocialservice.routes.MainRoute
import com.taetae.taetaesocialservice.routes.MainRouteAction
import com.taetae.taetaesocialservice.ui.components.SimpleDialog
import com.taetae.taetaesocialservice.ui.components.SnsAddPostButton
import com.taetae.taetaesocialservice.ui.components.SnsDialogAction
import com.taetae.taetaesocialservice.ui.theme.Dark
import com.taetae.taetaesocialservice.viewmodels.HomeViewModel
import com.taetae.taetaesocialservice.viewmodels.HomeViewModel.Companion.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    routeAction: MainRouteAction,
    homeViewModel: HomeViewModel
) {
//    val isRefreshing by homeViewModel.isRefreshing.collectAsState()
//    val refreshing by homeViewModel.isRefreshing.collectAsState()

    val refreshScope = rememberCoroutineScope()
    var refreshing: Boolean by remember { mutableStateOf(false) }
    var posts = homeViewModel.postsFlow.collectAsState()

    fun refreshData() = refreshScope.launch {
        refreshing = true
        delay(1000)
        posts = posts
        refreshing = false
        Log.d(TAG, "refreshData() called")
    }

    val pullRefreshState = rememberPullRefreshState(refreshing, ::refreshData)
    val postsListsScrollState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    var selectedPostIdForDelete: String? by remember {
        mutableStateOf(null)
    }
    val isLoading by homeViewModel.isLoadingFlow.collectAsState()
    val isDialogShown = !selectedPostIdForDelete.isNullOrBlank()

//    val posts = homeViewModel.postsFlow.collectAsState()

    LaunchedEffect(key1 = Unit, block = {
        homeViewModel.dataUpdatedFlow.collectLatest {
            postsListsScrollState.animateScrollToItem(posts.value.size)
        }
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState, true),
        contentAlignment = Alignment.TopCenter

    ) {
        Column() {
            Surface(color = Dark,
                contentColor = Color.White
                ) {
                Text(text = "총 포스팅 : ${posts.value.size}",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(1f)
                )
            }
            LazyColumn(
                state = postsListsScrollState,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp),
                reverseLayout = true
            ) {
                items(posts.value) { aPost ->
                    PostItemView(
                        aPost,
                        coroutineScope,
                        homeViewModel,
                        onDeletePostClicked = {
                            selectedPostIdForDelete = aPost.id.toString()
                        })
                }
            }
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter))

        SnsAddPostButton(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                coroutineScope.launch {
                    homeViewModel.navAction.emit(MainRoute.AddPost)
                }
            })
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.scale(0.7f)
                    .padding(5.dp)
            )
        }

        if (isDialogShown) {
            SimpleDialog(isLoading, onDialogAction = {
                when(it) {
                    SnsDialogAction.CLOSE -> selectedPostIdForDelete = null
                    SnsDialogAction.ACTION -> {
                        println("아이템 삭제해야함 $selectedPostIdForDelete")
                    }
                }
            })
        }
    }
}

@Composable
fun PostItemView(
    data: Post,
    coroutineScope: CoroutineScope,
    homeViewModel: HomeViewModel,
    onDeletePostClicked: ()-> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        elevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        Column() {

            Text(text = "User Id : ${data.userID}")

            Row() {
                Text(
                    text = "${data.id}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                )
                TextButton(onClick = onDeletePostClicked){
                    Text(text = "삭제")
                }

                TextButton(onClick = {
                    coroutineScope.launch {
                        homeViewModel.navAction
                            .emit(MainRoute.EditPost(postId = "${data.id}"))
                    }
                }) { Text(text = "수정")
                }

            }
            Text(
                text = "${data.title}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "${data.content}",
                maxLines = 5,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            )
        }
    }
}

