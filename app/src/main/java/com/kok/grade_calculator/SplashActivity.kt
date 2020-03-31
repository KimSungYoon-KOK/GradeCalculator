package com.kok.grade_calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if(MySharedPreferences(this).getStartFlag()){
            val Intent = Intent(this, MainActivity::class.java)
            startActivity(Intent)
            finish()
        }else{
            val Intent = Intent(this, FirststartActivity::class.java)
            startActivity(Intent)
            finish()
        }
    }
}
