package com.sirius.sample.ui.auth.auth_third

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sirius.sample.base.providers.ResourcesProvider
import com.sirius.sample.base.ui.BaseViewModel
import com.sirius.sample.repository.UserRepository

import javax.inject.Inject



open class AuthThirdViewModel @Inject constructor(
    val userRepository: UserRepository,
    resourcesProvider: ResourcesProvider
) : BaseViewModel(resourcesProvider) {


    val registerHintLiveData = MutableLiveData<String>("")
    val registerFieldHintLiveData = MutableLiveData<String>("")
    val registerBtnTextLiveData = MutableLiveData<String>("")
    val alreadyExistAccountTextLiveData = MutableLiveData<CharSequence>()
    val goToTypeInfoScreenLiveData = MutableLiveData<Boolean>()
    val goToNextInfoScreenLiveData = MutableLiveData<Boolean>()
    val nextVisibilityLiveData = MutableLiveData<Int>()
    val countryCodeLiveData = MutableLiveData<String>("+1")



    fun onRegisterClick(v: View) {
        goToTypeInfoScreenLiveData.postValue(true)
    }

    fun onNextClick(v: View){
        goToNextInfoScreenLiveData.postValue(true)
    }

    override fun setupViews() {
        super.setupViews()
        isNextEnabled()
    }



    fun isNextEnabled()  {
        val isNextEnabled = !userRepository.myUser.phone.isNullOrEmpty() && !userRepository.myUser.email.isNullOrEmpty()
        if(isNextEnabled){
            nextVisibilityLiveData.postValue( View.VISIBLE)
        }else{
            nextVisibilityLiveData.postValue(  View.INVISIBLE)
        }

    }

    fun setUserEmail(emai: String) {
        userRepository.myUser.email = emai
    }

    fun setPhone(phone: String) {
        userRepository.myUser.phone = phone
    }

}


