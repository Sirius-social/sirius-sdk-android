package com.sirius.sample.ui.qrcode

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sirius.sample.base.providers.ResourcesProvider
import com.sirius.sample.base.ui.BaseViewModel
import com.sirius.sample.repository.UserRepository

import javax.inject.Inject



open class ScanQrViewModel @Inject constructor(
    val userRepository: UserRepository,
    resourcesProvider: ResourcesProvider
) : BaseViewModel(resourcesProvider) {


    val goToTypeInfoScreenLiveData = MutableLiveData<Boolean>()
    val emptyVisibilityLiveData = MutableLiveData<Int>()
    val actionsListVisibilityLiveData = MutableLiveData<Int>()




    fun onRevealClick(v: View) {
        onBackButtonClick(v)
    }

    open fun onCodeScanned(code: String) : Boolean {
      /*  if (validateInvitationUrl(code)) {
            parseInvitationLink(code)
            return true
        } else {
            val textError: String = resourcesProvider.getString(R.string.invite_qr_scan_hint_error)
            onShowToastLiveData.postValue(textError)
            return false
        }*/
        return true;
    }

    /*private fun validateInvitationUrl(rawValue: String): Boolean {
        //DEMO
        if (rawValue.startsWith("Order")) {
            return true;
        }
        try {
            //DEMO
            val uri = Uri.parse(rawValue)
            val demo_transcript_file = uri.getQueryParameter("demo_transcript_file")
            val demo_request_did = uri.getQueryParameter("demo_request_did")
            if (!TextUtils.isEmpty(demo_transcript_file) || !TextUtils.isEmpty(demo_request_did)) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //REAL WORLD
        val parsedString: String = Feature0160.parseInvitationLink(rawValue)
        Log.d("mylog500", "validateInvitationUrl parsedString=$parsedString")
        val protocolResponse: ProtocolResponse = ConnectionProtocol.parse(ConnectionProtocol.MESSAGE_CONTENT_TYPE, parsedString)
        return protocolResponse.getConnectionProtocol() != null
    }
    */
    fun showHideEmpty(show : Boolean){
        if(show){
            emptyVisibilityLiveData.postValue(View.VISIBLE)
            actionsListVisibilityLiveData.postValue(View.GONE)
        }else{
            emptyVisibilityLiveData.postValue(View.GONE)
            actionsListVisibilityLiveData.postValue(View.VISIBLE)
        }

    }


    override fun setupViews() {
        super.setupViews()
        showHideEmpty(true)
    }


}


