package com.sirius.sample.repository

import com.sirius.sample.base.AppExecutors
import com.sirius.sample.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val appExecutors: AppExecutors)  {

    var myUser = User()
}