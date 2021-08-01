package com.sirius.sample.repository

import com.sirius.sample.base.AppExecutors
import com.sirius.sample.base.AppPref
import com.sirius.sample.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val appExecutors: AppExecutors)  {

    var myUser = User()

    init {
        setupUserFromPref()
    }

    fun setupUserFromPref(){
        val user =  AppPref.getInstance().getUser()
        user?.let {
            myUser = it
        }
    }

    fun saveUserToPref(){
        AppPref.getInstance().setUser(myUser)
    }

    fun logout(){
        AppPref.getInstance().setUser(null)
    }
}