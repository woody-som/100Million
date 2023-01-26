package com.taetae.taetaesocialservice.routes

import androidx.navigation.NavHostController
import com.taetae.taetaesocialservice.R

const val ActivityCloseActionName = "CLOSE_ACTION"

enum class ActivityCloseAction(val actionName: String) {
    POST_ADDED("POST_ADDED"),
    POST_EDITED("POST_EDITED"),
    POST_DELETED("POST_DELETED");

    companion object {
        fun getActionType(name: String) : ActivityCloseAction? {
            return when(name) {
                POST_ADDED.actionName -> POST_ADDED
                POST_EDITED.actionName -> POST_EDITED
                POST_DELETED.actionName -> POST_DELETED
                else -> null
            }
        }
    }
}

sealed class MainRoute(
    open val routeName: String,
    open val title: String,
    val iconResId: Int? = null
) {
    object Home: MainRoute("HOME", "홈", R.drawable.ic_home)
    object MyPage: MainRoute("MY_PAGE", "마이페이지", R.drawable.ic_profile)
    object AddPost: MainRoute("ADD_POST", "포스트 추가")
    class EditPost(val postId: String): MainRoute("EDIT_POST", "포스트 수정")
}


// 메인 관련 화면 라우트 액션
class MainRouteAction (navHostController: NavHostController) {

    //특정 라우트로 이동  : 람다로 만들어 놓는다
    val navTo: (MainRoute) -> Unit = { route ->
        navHostController.navigate(route.routeName) {
            popUpTo(route.routeName) { inclusive = true }
        }
    }
    // 뒤로가기 이동 세팅
    val goBack: () -> Unit = {
        navHostController.navigateUp()
    }

}