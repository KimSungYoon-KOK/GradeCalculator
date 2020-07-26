package com.kok.grade_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kok.grade_calculator.home.HomeFragment
import com.kok.grade_calculator.mypage.MyPageFragment
import com.kok.grade_calculator.setting.SettingFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val mypageFragment = MyPageFragment()
    private val settingFragment = SettingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss()
        nav_view.setOnNavigationItemSelectedListener {
            val transaction = supportFragmentManager.beginTransaction()
            when(it.itemId) {
                R.id.navigation_home -> {
                    transaction.replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_mypage -> {
                    transaction.replace(R.id.nav_host_fragment, mypageFragment).commitAllowingStateLoss()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_setting -> {
                    transaction.replace(R.id.nav_host_fragment, settingFragment).commitAllowingStateLoss()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

}
