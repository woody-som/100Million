package com.taetae.taetaesocialservice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.taetae.taetaesocialservice.ui.screens.main.AddPostScreen
import com.taetae.taetaesocialservice.ui.screens.main.EditPostScreen
import com.taetae.taetaesocialservice.ui.theme.TaetaeSocialServiceTheme
import com.taetae.taetaesocialservice.viewmodels.EditPostViewModel

class EditPostActivity : ComponentActivity () {

    lateinit var editPostViewModel: EditPostViewModel

    companion object {
        private const val POST_ID = "post_id"
        fun newIntent(context: Context, postId: String) =
            Intent(context, EditPostActivity::class.java).apply {
                putExtra(POST_ID, postId)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(
            WindowManager.
            LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )

        val postId: String = intent.getStringExtra(POST_ID) ?: ""

        editPostViewModel = EditPostViewModel(postId)

        setContent {
            TaetaeSocialServiceTheme {
                EditPostScreen(editPostViewModel)
            }
        }
    }
}