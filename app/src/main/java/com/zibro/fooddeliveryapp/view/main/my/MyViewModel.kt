package com.zibro.fooddeliveryapp.view.main.my

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.zibro.fooddeliveryapp.data.preference.AppPreferenceManager
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(
    private val prefs : AppPreferenceManager
) : BaseViewModel() {
    val myStateLivedata = MutableLiveData<MyState>(MyState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        myStateLivedata.value = MyState.Loading
        prefs.getIdToken()?.let {
            myStateLivedata.value = MyState.Login(it)
        } ?:kotlin.run {
            myStateLivedata.value = MyState.Success.NotRegistered
        }
    }

    fun saveToken(token: String) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            prefs.putIdToken(idToken = token)
            fetchData()
        }
    }

    fun setUserInfo(firebaseUser: FirebaseUser?)= viewModelScope.launch {
        firebaseUser?.let { user->
            myStateLivedata.value = MyState.Success.Registerd(
                userName = user.displayName ?: "익명",
                profileImageUri = user.photoUrl
            )
        }?:kotlin.run {
            myStateLivedata.value = MyState.Success.NotRegistered
        }

    }

    fun signOut() = viewModelScope.launch {
        withContext(Dispatchers.IO){
            prefs.removeIdToken()
        }
        fetchData()
    }
}