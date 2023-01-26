package com.taetae.taetaesocialservice

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.taetae.taetaesocialservice.routes.*
import com.taetae.taetaesocialservice.ui.screens.WelcomeScreen
import com.taetae.taetaesocialservice.ui.screens.auth.LoginScreen
import com.taetae.taetaesocialservice.ui.screens.auth.RegisterScreen
import com.taetae.taetaesocialservice.ui.screens.main.HomeScreen
import com.taetae.taetaesocialservice.ui.screens.main.MyPageScreen
import com.taetae.taetaesocialservice.ui.theme.Dark
import com.taetae.taetaesocialservice.ui.theme.Gray
import com.taetae.taetaesocialservice.ui.theme.LightGray
import com.taetae.taetaesocialservice.ui.theme.TaetaeSocialServiceTheme
import com.taetae.taetaesocialservice.viewmodels.AuthViewModel
import com.taetae.taetaesocialservice.viewmodels.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    //뷰모델  두개를 선언해 준다
    private val authViewModel: AuthViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    //    액티비티가 닫아질때 이벤트
    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val getActionString = result.data?.getStringExtra(ActivityCloseActionName)
            val closeAction = ActivityCloseAction.getActionType(getActionString ?: "")
            closeAction?.let {
                Log.d("메인", "CLOSE_ACTION COMPLETE")
            }
        }
    }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            //화면 스크롤에 대한 내용
            window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
            )

            lifecycleScope.launch {
                homeViewModel.navAction.collectLatest {
                    when (it) {
                        is MainRoute.AddPost -> {
                            startActivity(AddPostActivity.newIntent(this@MainActivity))
//                        activityResultLauncher.launch(AddPostActivity.newIntent(this@MainActivity))
                        }
                        is MainRoute.EditPost -> {
                            startActivity(
                                EditPostActivity.newIntent(
                                    this@MainActivity, it.postId
                                )
                            )

                        }
                        else -> {
                            Log.d("else", "분기 안됨 ")
                        }
                    }
                }
            }

            setContent {
                TaetaeSocialServiceTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        AppScreen(authViewModel, homeViewModel)
                    }
                }
            }
        }
    }

@Composable
fun AppScreen(
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel
){
    val isLoggedIn = authViewModel.isLoggedIn.collectAsState()
    var mainNavController = rememberNavController()
    val mainRouteAction = remember(mainNavController) {
        MainRouteAction(mainNavController)
    }


    var authNavController = rememberNavController()
    val authRouteAction = remember(authNavController) {
        AuthRouteAction(authNavController)
    }

    val authBackStackEntry = authNavController.currentBackStackEntryAsState()
    val mainBackStack = mainNavController.currentBackStackEntryAsState()


    if (!isLoggedIn.value) {
        AuthNavHost(
            authNavController = authNavController,
            authViewModel = authViewModel,
            routeAction = authRouteAction
        )
    } else {
        Scaffold(bottomBar = {
            SnsBottomNav(mainRouteAction, mainBackStack.value)
        }) {
            Column(
                modifier = Modifier.padding(bottom = it.calculateBottomPadding())
            ) {
                MainNavHost(
                    mainNavController,
                    authViewModel = authViewModel,
                    homeViewModel = homeViewModel,
                    routeAction = mainRouteAction)
            }
        }
    }

}

@Composable
fun MainNavHost(
    mainNavController: NavHostController,
    startRouter: MainRoute = MainRoute.Home,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    routeAction: MainRouteAction
) {
    NavHost(navController = mainNavController,
        startDestination = startRouter.routeName) {
        composable(MainRoute.Home.routeName) {
            HomeScreen(routeAction,homeViewModel)
        }
        composable(MainRoute.MyPage.routeName) {
            MyPageScreen(homeViewModel, authViewModel, routeAction)
        }
    }
}


@Composable
fun AuthNavHost(
    authNavController: NavHostController,
    startRouter: AuthRoute = AuthRoute.WELCOME,
    authViewModel: AuthViewModel,
    routeAction: AuthRouteAction
) {
    NavHost(navController = authNavController,
    startDestination = startRouter.routeName) {
        composable(AuthRoute.WELCOME.routeName) {
            WelcomeScreen(routeAction)
        }
        composable(AuthRoute.LOGIN.routeName) {
            LoginScreen(routeAction, authViewModel)
        }
        composable(AuthRoute.REGISTER.routeName) {
            RegisterScreen(authViewModel, routeAction)
        }
    }
}

@Composable
fun SnsBottomNav(
    mainRouteAction: MainRouteAction,
    mainBackStack: NavBackStackEntry?
) {
    val bottomRoutes = listOf<MainRoute>(MainRoute.Home, MainRoute.MyPage)
    BottomNavigation(
        backgroundColor = LightGray,
        modifier = Modifier.fillMaxWidth()
    ) {
        bottomRoutes.forEach {
            BottomNavigationItem(
                label = { Text(text = it.title) },
                icon = {
                    it.iconResId?.let { iconID ->
                        Icon(painter = painterResource(iconID), contentDescription = it.title)
                    }
                },
                selectedContentColor = Dark,
                unselectedContentColor = Gray,
                selected = (mainBackStack?.destination?.route) == it.routeName,
                onClick = { mainRouteAction.navTo(it) }
            )
        }
    }
}



//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    TaetaeSocialServiceTheme {
//        WelcomeScreen()
//    }
//}