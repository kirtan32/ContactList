package com.kirtan.shah.mycontacts.loginsignup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.kirtan.shah.mycontacts.MainActivity
import com.kirtan.shah.mycontacts.R

class LoginFragment : Fragment() {

    lateinit var email : TextInputEditText
    lateinit var pass : TextInputEditText
    lateinit var submit : MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        email = view.findViewById(R.id.emailLogin)
        pass = view.findViewById(R.id.passwordLogin)
        submit = view.findViewById(R.id.loginBtn)

        submit.setOnClickListener {
            val email = email.text.toString()
            val password = pass.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty())
            {

                // Do Login
                //var isLogin = LoginSignup.presenter.DoLogin(email,password)
                var isLogin=false
                LoginSignup.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {

                        if (it.isSuccessful) {
                            //signUpFlag = true
                            isLogin=true
                            //Toast.makeText(context,"In Login",Toast.LENGTH_SHORT).show()
                            val myIntent = Intent(
                                activity?.applicationContext,
                                MainActivity::class.java
                            )
                            // Setting Login Prefs
                            setLoginPref(email)

                            activity?.startActivity(myIntent)
                            activity?.finish()
                            Toast.makeText(context, "Welcome: ${email}", Toast.LENGTH_LONG).show()
                        }

                    }.addOnFailureListener {
                        Toast.makeText(context, "Sign Up Failed: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
                        isLogin=false
                    }

            }
            else
            {
                Toast.makeText(context,"Invalid Login Inputs", Toast.LENGTH_LONG).show()
            }

        }

    }
    //contactPrefs
    fun setLoginPref(email: String)
    {
        val pref = requireActivity().application.applicationContext.getSharedPreferences("contactPrefs",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putBoolean("isLogin", true)
        editor.putString("email",email)
        editor.apply()
    }

}