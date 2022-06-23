package com.zibro.fooddeliveryapp.view.main

import android.nfc.Tag
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.zibro.fooddeliveryapp.R
import com.zibro.fooddeliveryapp.databinding.ActivityMainBinding
import com.zibro.fooddeliveryapp.view.base.BaseActivity
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import com.zibro.fooddeliveryapp.view.main.home.HomeFragment
import com.zibro.fooddeliveryapp.view.main.my.MyFragment

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }
    private fun initViews() = with(binding){
        bottomNavigation.run {
            setOnItemSelectedListener(this@MainActivity)
        }
        showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_home -> {
                showFragment(HomeFragment.newInstance(),HomeFragment.TAG)
                true
            }
            R.id.menu_my ->{
                showFragment(MyFragment.newInstance(),MyFragment.TAG)
                true
            }
            else -> false
        }
    }

    private fun showFragment(fragment : Fragment, tag: String){
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach{ fm ->
            supportFragmentManager.beginTransaction().hide(fm).commit()
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commit()
        } ?: run{
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container,fragment,tag)
                .commit()
        }
    }
}