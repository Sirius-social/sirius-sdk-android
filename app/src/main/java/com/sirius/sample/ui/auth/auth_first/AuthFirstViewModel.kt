package com.sirius.sample.ui.auth.auth_first

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sirius.sample.base.providers.ResourcesProvider
import com.sirius.sample.base.ui.BaseViewModel
import com.sirius.sample.repository.UserRepository

import javax.inject.Inject

enum class RegisterStep {
    Name,
    Sms,
    Pin,
    TypeInfo,
    Info,
    Photo
}

open class AuthFirstViewModel @Inject constructor(
    val userRepository: UserRepository,
    resourcesProvider: ResourcesProvider
) : BaseViewModel(resourcesProvider) {

    val registerHintLiveData = MutableLiveData<String>("")
    val registerFieldHintLiveData = MutableLiveData<String>("")
    val registerBtnTextLiveData = MutableLiveData<String>("")
    val alreadyExistAccountTextLiveData = MutableLiveData<CharSequence>()
    val goToNextScreenLiveData = MutableLiveData<Boolean>()
    val nextVisibilityLiveData = MutableLiveData<Int>()



    fun onRegisterClick(v: View) {
        goToNextScreenLiveData.postValue(true)
    }

    override fun setupViews() {
        super.setupViews()
        isNextEnabled()
    }


    fun isNextEnabled()  {
        val isNextEnabled = !userRepository.myUser.name.isNullOrEmpty() && !userRepository.myUser.lastname.isNullOrEmpty()
        if(isNextEnabled){
            nextVisibilityLiveData.postValue( View.VISIBLE)
        }else{
            nextVisibilityLiveData.postValue(  View.INVISIBLE)
        }

    }

    fun setUserName(name: String) {
        userRepository.myUser.name = name
    }

    fun setUserLastname(lastname: String) {
        userRepository.myUser.lastname = lastname
    }

}


