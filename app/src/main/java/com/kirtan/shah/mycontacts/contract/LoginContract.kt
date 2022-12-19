package com.kirtan.shah.mycontacts.contract

import android.content.Context
import androidx.fragment.app.FragmentActivity

public interface LoginContract
{
    interface View
    {
        fun OnSuccess()
        fun OnError()
    }

    interface Presenter{
        fun DoLogin(email:String,password : String) : Boolean
        fun DoSignUp(email: String,password: String) : Boolean
        fun DoSignOut(context: Context):Boolean
    }
}