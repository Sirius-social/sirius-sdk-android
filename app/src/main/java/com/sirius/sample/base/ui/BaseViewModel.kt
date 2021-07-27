package com.sirius.sample.base.ui

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sirius.sample.base.providers.ResourcesProvider
import com.sirius.sample.design.BottomNavView


abstract class BaseViewModel(
    protected val resourceProvider: ResourcesProvider
) : ViewModel() {

    val onBackClickLiveData = MutableLiveData<Boolean>()
    val onShowToastLiveData = MutableLiveData<String>()
    val onShowAlertDialogLiveData = MutableLiveData<Pair<String,String>>()
    val onShowProgressLiveData = MutableLiveData<Boolean>()
    val onDestroyLiveData = MutableLiveData<Boolean>()
    val finishActivityLiveData = MutableLiveData<Boolean>()
    val titleLiveData = MutableLiveData<String>()
    val isHideActionBarLiveData = MutableLiveData<Boolean>(false)
    val openTabLiveData = MutableLiveData<BottomNavView.BottomTab?>()
    open fun  onCreateview(){

    }
    open fun onViewCreated() {}

    open fun onDestroy() {
        onShowProgressLiveData.value = false
        onDestroyLiveData.value = true
    }

    open fun onResume() {}

    open fun onBackButtonClick(v: View?) {
        onBackClickLiveData.postValue(true)
    }



    fun showProgressDialog() {
        onShowProgressLiveData.postValue(true)
    }

    fun hideProgressDialog() {
        onShowProgressLiveData.postValue(false)
    }

    open fun onRequestPermissionSuccess(requestCode: Int) {

    }

    open fun onRequestPermissionFail(requestCode: Int) {

    }

    open fun isVisibleUnauthBottomBar(): Boolean {
        return false
    }

    open fun setTitle(title: String?) {
        titleLiveData.postValue(title ?: "")
    }

    open fun onSearchText(searchText: String?) {

    }

    open fun setupViews(){

    }


}