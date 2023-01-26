package com.taetae.taetaesocialservice.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel

class EditPostViewModel(val currentPostId: String): ViewModel() {

    companion object {
        const val TAG = "EditPostViewModel"
    }
    init {
        Log.d(TAG, "EditPostViewModel : Init / postId : $currentPostId")
    }
}