package com.sirius.sample.repository

import androidx.lifecycle.MutableLiveData
import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.state_machines.Holder
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.agent.storages.InWalletImmutableCollection
import com.sirius.sdk.storage.abstract_storage.AbstractImmutableCollection
import com.sirius.sdk_android.EventWalletStorage
import com.sirius.sdk_android.SiriusSDK
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(){

/*    companion object {
        private var eventRepository: EventRepository? = null

        @JvmStatic
        fun getInstance(): EventRepository {
            if (eventRepository == null) {
                eventRepository = EventRepository()
            }
            return eventRepository!!
        }
    }*/

    fun getEvent(id : String) : Event?{
        return eventMap[id]   ?: Event(null,"{}")
    }

    fun storeEvent(id : String, holder : Event){
        //eventMap.put(id, holder)
        reloadEventsFromWallet()
    }

    fun removeEvent(id : String){
          eventMap.remove(id)
    }
    var storage: EventWalletStorage? = null

    fun initStorage() {
        if (storage == null && SiriusSDK.getInstance().context != null) {
            storage = EventWalletStorage( SiriusSDK.getInstance().context.nonSecrets)
        }
    }

    fun reloadEventsFromWallet(){
        initStorage()
        val result = storage!!.fetch()
        result.first.forEach {
            eventMap.put(it.message().id,it)
        }
    }

    val eventMap : MutableMap<String, Event>  = HashMap()
    val eventStartLiveData : MutableLiveData<Event> = MutableLiveData()
    val eventStoreLiveData : MutableLiveData<String> = MutableLiveData()
    val eventStopLiveData : MutableLiveData<Event> = MutableLiveData()
    val invitationStartLiveData : MutableLiveData<Boolean> = MutableLiveData()
    val invitationStopLiveData : MutableLiveData<Pair<Boolean,String?>>  = MutableLiveData()
}