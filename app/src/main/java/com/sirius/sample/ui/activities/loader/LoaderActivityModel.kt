package com.sirius.sample.ui.activities.loader



import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.sirius.sample.base.providers.ResourcesProvider
import com.sirius.sample.base.ui.BaseActivityModel
import com.sirius.sample.repository.SDKUseCase
import com.sirius.sample.repository.UserRepository

import javax.inject.Inject

class LoaderActivityModel @Inject constructor(
    resourceProvider: ResourcesProvider,
    private val sdkUseCase: SDKUseCase,
    private val userRepository: UserRepository
) :
    BaseActivityModel(resourceProvider) {

    val initStartLiveData = MutableLiveData<Boolean>()
    val initEndLiveData = MutableLiveData<Boolean>()

        fun initSdk(context: Context){
            val login = userRepository.myUser.name ?: ""
            val pass = userRepository.myUser.pass ?:""
            sdkUseCase.initSdk(context,login,pass, object : SDKUseCase.OnInitListener{
                override fun initStart() {
                    initStartLiveData.postValue(true)
                }

                override fun initEnd() {
                    initEndLiveData.postValue(true)
                }

            })
        }


}