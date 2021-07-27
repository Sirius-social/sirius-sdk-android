package com.sirius.sample.repository

import androidx.lifecycle.MutableLiveData
import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.state_machines.Holder
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk_android.SiriusSDK

class HolderRepository {

    companion object {
        private var holderRepository: HolderRepository? = null

        @JvmStatic
        fun getInstance(): HolderRepository {
            if (holderRepository == null) {
                holderRepository = HolderRepository()
            }
            return holderRepository!!
        }
    }

    fun getHolder(id : String) : Event{
        return holderMap[id]   ?: Event(null,"{}")
    }

    fun storeHolder(id : String, holder : Event){
        holderMap.put(id, holder)
    }

    fun removeHolder(id : String){
          holderMap.remove(id)
    }

    val holderMap : MutableMap<String, Event>  = HashMap()
    val holderStartLiveData : MutableLiveData<Holder> = MutableLiveData()
    val holderStoreLiveData : MutableLiveData<String> = MutableLiveData()
    val holderStopLiveData : MutableLiveData<Holder> = MutableLiveData()
}