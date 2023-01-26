package com.taetae.taetaesocialservice.routes

import androidx.navigation.NavHostController

//인증화면 라우트
enum class AuthRoute(val routeName : String) {
    LOGIN("LOGIN"),
    REGISTER("REGISTER"),
    WELCOME("WELCOME")
}

// 인증 관련 화면 라우트 (이동) 액션
class AuthRouteAction (navHostController: NavHostController) {

    //특정 라우트로 이동  : 람다로 만들어 놓는다
    val navTo: (AuthRoute) -> Unit = { authRoute ->
        navHostController.navigate(authRoute.routeName) {
            popUpTo(authRoute.routeName) { inclusive = true }
        }
    }
    // 뒤로가기 이동 세팅
    val goBack: () -> Unit = {
        navHostController.navigateUp()
    }

}