package com.taetae.taetaesocialservice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.taetae.taetaesocialservice.ui.screens.main.AddPostScreen
import com.taetae.taetaesocialservice.ui.theme.TaetaeSocialServiceTheme
import com.taetae.taetaesocialservice.viewmodels.AddPostViewModel

class AddPostActivity : ComponentActivity () {

    private val addPostViewModel : AddPostViewModel by viewModels()

    companion object {
        fun newIntent(context: Context) = Intent(context, AddPostActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(
            WindowManager.
            LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
        setContent {
            TaetaeSocialServiceTheme {
                AddPostScreen(addPostViewModel)
            }
        }
    }
}