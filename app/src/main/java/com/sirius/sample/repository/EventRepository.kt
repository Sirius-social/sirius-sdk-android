package com.sirius.sample.repository

import androidx.lifecycle.MutableLiveData
import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.state_machines.Holder
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.agent.pairwise.Pairwise
import com.sirius.sdk.agent.storages.InWalletImmutableCollection
import com.sirius.sdk.storage.abstract_storage.AbstractImmutableCollection
import com.sirius.sdk_android.EventWalletStorage
import com.sirius.sdk_android.SiriusSDK
import org.json.JSONObject
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
        return EventWalletStorage.getInstance().get(id)
    }

    fun storeEvent(id : String, event : Event?, type : String? ){
        event?.let {
            val tags = EventWalletStorage.EventTags()
            tags.pairwiseDid = event?.pairwise?.their?.did
            tags.type = type
            EventWalletStorage.getInstance().add(event,id,tags)
            eventStoreLiveData.postValue(id)
        }

    }

    fun removeEvent(id : String){

    }


    fun loadAllUnacceptedEventActions() : List<Event>{
        val tags = EventWalletStorage.EventTags()
        tags.isAccepted = false.toString()
        return EventWalletStorage.getInstance().fetch(tags = tags.serialize()).first
    }

    fun loadEventActionsForPairwise(pairwise: String){
        val tags = EventWalletStorage.EventTags()
        tags.pairwiseDid = pairwise
       // tags.type =
        EventWalletStorage.getInstance().fetch(tags = tags.serialize())
    }

    fun loadAllEventsForPairwise(pairwise: String) :  List<Event>{
        val tags = EventWalletStorage.EventTags()
        tags.pairwiseDid = pairwise
        return EventWalletStorage.getInstance().fetch(tags = tags.serialize()).first
    }

    val eventStartLiveData : MutableLiveData<Event> = MutableLiveData()
    val eventStoreLiveData : MutableLiveData<String> = MutableLiveData()
    val eventStopLiveData : MutableLiveData<Event> = MutableLiveData()
    val invitationStartLiveData : MutableLiveData<Boolean> = MutableLiveData()
    val invitationStopLiveData : MutableLiveData<Pair<Boolean,String?>>  = MutableLiveData()
}