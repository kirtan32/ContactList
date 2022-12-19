package com.kirtan.shah.mycontacts.loginsignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.kirtan.shah.mycontacts.MainActivity
import com.kirtan.shah.mycontacts.R
import com.kirtan.shah.mycontacts.presenter.LoginPresenter

class LoginSignup : AppCompatActivity() {

    lateinit var navigationView: BottomNavigationView
    companion object{
        lateinit var auth: FirebaseAuth
        lateinit var presenter : LoginPresenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)

        if(checkIfLogin())
        {
            val act = Intent(applicationContext, MainActivity::class.java)
            startActivity(act)
            finish()
        }

        //Firebase Authentication
        auth= FirebaseAuth.getInstance()
        presenter = LoginPresenter()

        navigationView = findViewById<BottomNavigationView>(R.id.bottomLogin_navigation)
        supportFragmentManager.beginTransaction().replace(R.id.bodyLogin_container, LoginFragment())
            .commit()
        navigationView.setSelectedItemId(R.id.nav_login)

        navigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var fragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_login -> fragment = LoginFragment()
                R.id.nav_signup -> fragment = SignupFragment()
            }
            if (fragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.bodyLogin_container, fragment)
                    .commit()
            }
            true
        })

    }

    fun checkIfLogin() : Boolean
    {
        val pref = applicationContext.getSharedPreferences("contactPrefs", AppCompatActivity.MODE_PRIVATE)
        return pref.getBoolean("isLogin", false)

    }

    //contactPrefs
    fun setLoginPref(email: String)
    {
        val pref = this.getSharedPreferences("contactPrefs",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putBoolean("isLogin", true)
        editor.putString("email",email)
        editor.apply()
    }
}