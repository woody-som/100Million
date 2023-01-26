package com.taetae.taetaesocialservice.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.taetae.taetaesocialservice.routes.AuthRoute
import com.taetae.taetaesocialservice.routes.AuthRouteAction
import com.taetae.taetaesocialservice.ui.components.SnsButtonType
import com.taetae.taetaesocialservice.ui.components.BaseButton
import com.taetae.taetaesocialservice.ui.theme.Border
import com.taetae.taetaesocialservice.ui.theme.Dark
import com.taetae.taetaesocialservice.ui.theme.Gray

@Composable
fun WelcomeScreen(routeAction: AuthRouteAction) {
    Column(
        modifier = Modifier.padding(horizontal = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LottieAnimationView()

        Text("태태 SNS 앱",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.weight(1f))

        BaseButton(
            title = "로그인",
            onClick = {
                Log.d("웰컴스크린","로그인 버튼 클릭!!")
                routeAction.navTo(AuthRoute.LOGIN)
        })

        Spacer(modifier = Modifier.height(22.dp))

        BaseButton(type = SnsButtonType.OUTLINE,
            title = "회원가입",
            onClick = {
                Log.d("웰컴스크린","회원가입 버튼 클릭!!")
                routeAction.navTo(AuthRoute.REGISTER)
        })
        
        Spacer(modifier = Modifier.height(40.dp))

    }
}


@Composable
fun LottieAnimationView() {
    val composition by rememberLottieComposition(LottieCompositionSpec
        .Asset("travel_title.json")
    )

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.height(400.dp)
    )
}