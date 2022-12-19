package com.kirtan.shah.mycontacts.presenter

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.kirtan.shah.mycontacts.contract.LoginContract
import com.kirtan.shah.mycontacts.loginsignup.LoginSignup

class LoginPresenter : LoginContract.Presenter {

    override fun DoLogin(
        email: String,
        password: String
    ): Boolean {

        var loginFlag: Boolean = false
        LoginSignup.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if (it.isSuccessful) {

                    loginFlag = true
                    //Toast.makeText(context, "Welcome", Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener {
                //Toast.makeText(context,"Login Failed: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
                loginFlag = false
            }

        return loginFlag
    }

    override fun DoSignUp(
        email: String,
        password: String
    ): Boolean {

        var signUpFlag = false

        LoginSignup.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    signUpFlag = true
                    //Toast.makeText(context, "Welcome", Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener {
                //Toast.makeText(context, "Sign Up Failed: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
                signUpFlag=false
            }

        return signUpFlag
    }

    override fun DoSignOut(context: Context): Boolean {

        try {
            LoginSignup.auth.signOut()
        }
        catch(ex:Exception)
        {
            Toast.makeText(context, "Something Wrong : ${ex.message}", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }


}