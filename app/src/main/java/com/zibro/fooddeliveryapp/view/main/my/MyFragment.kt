package com.zibro.fooddeliveryapp.view.main.my

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.zibro.fooddeliveryapp.R
import com.zibro.fooddeliveryapp.databinding.FragmentMyBinding
import com.zibro.fooddeliveryapp.extension.load
import com.zibro.fooddeliveryapp.model.order.OrderModel
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseFragment
import com.zibro.fooddeliveryapp.widget.adapter.ModelRecyclerAdapter
import com.zibro.fooddeliveryapp.widget.adapter.listener.AdapterListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.Exception

class MyFragment : BaseFragment<MyViewModel,FragmentMyBinding>() {
    override val viewModel by viewModel<MyViewModel>()

    override fun getViewBinding(): FragmentMyBinding = FragmentMyBinding.inflate(layoutInflater)

    private val gso : GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    private val adapter by lazy {
        ModelRecyclerAdapter<OrderModel,MyViewModel>(listOf(),viewModel,resourceProvider,object : AdapterListener{})
    }

    private val resourceProvider by inject<ResourceProvider>()

    private val gsc by lazy { GoogleSignIn.getClient(requireActivity(),gso) }

    //Firebase Authentication
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                task.getResult(ApiException::class.java)?.let { account ->
                    Log.e(TAG, "firebaseAuthWithGoogle: ${account.id}")
                    viewModel.saveToken(account.idToken ?: throw Exception())
                } ?:kotlin.run{throw Exception()}
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun initViews() = with(binding){
        loginButton.setOnClickListener {
            signInGoogle()
        }
        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            viewModel.signOut()
        }
        recyclerView.adapter = adapter
    }

    private fun signInGoogle(){
        val signInIntent = gsc.signInIntent
        loginLauncher.launch(signInIntent)
    }

    override fun observeData() = viewModel.myStateLivedata.observe(viewLifecycleOwner) {
        when(it){
            is MyState.Loading ->handleLoadingState()
            is MyState.Login -> handleLoginState(it)
            is MyState.Success -> handleSuccessState(it)
            is MyState.Error -> handleErrorState(it)
        }
    }

    private fun handleLoadingState() = with(binding){
        loginRequiredGroup.isGone = true
        progressbar.isVisible = true
    }
    private fun handleSuccessState(myState: MyState.Success) = with(binding){
        progressbar.isGone = true
        when(myState){
            is MyState.Success.Registerd -> {
                handleRegisteredState(myState)
            }
            is MyState.Success.NotRegistered ->{
                profileGroup.isGone = true
                loginRequiredGroup.isVisible = true
            }
        }
    }
    private fun handleRegisteredState (state: MyState.Success.Registerd)= with(binding){
        profileGroup.isVisible = true
        loginRequiredGroup.isGone = true
        profileImageView.load(state.profileImageUri.toString(), 60f)
        userNameTextView.text = state.userName

        adapter.submitList(state.orderList)
    }
    private fun handleLoginState(myState: MyState.Login) = with(binding){
        progressbar.isVisible = true
        val credential = GoogleAuthProvider.getCredential(myState.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task->
                if(task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    viewModel.setUserInfo(user)
                } else {
                    firebaseAuth.signOut()
                    viewModel.setUserInfo(null)
                }
            }
    }
    private fun handleErrorState(myState: MyState.Error){
        Toast.makeText(requireContext(), myState.messageId, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance() = MyFragment()

        const val  TAG = "MyFragment"
    }
}