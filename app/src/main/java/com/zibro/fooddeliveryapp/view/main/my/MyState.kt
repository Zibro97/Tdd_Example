package com.zibro.fooddeliveryapp.view.main.my

import android.net.Uri
import androidx.annotation.StringRes

sealed class MyState{
    object Uninitialized : MyState()

    object Loading : MyState()

    data class  Login(
        val idToken : String
    ) : MyState()

    sealed class Success : MyState() {
        //로그인 된 상태
        data class Registerd(
            val userName : String,
            val profileImageUri: Uri?
        ) : Success()

        object NotRegistered: Success()
    }

    data class Error(
        @StringRes val messageId : Int,
        val e : Throwable
    ) : MyState()
}
