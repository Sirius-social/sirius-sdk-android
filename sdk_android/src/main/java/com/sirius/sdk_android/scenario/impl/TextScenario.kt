package com.sirius.sdk_android.scenario.impl

import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.EventWalletStorage
import com.sirius.sdk_android.scenario.BaseScenario
import com.sirius.sdk_android.scenario.EventStorageAbstract

abstract class TextScenario : BaseScenario() , EventStorageAbstract {

    override fun initMessages(): List<Class<out Message>> {
        return listOf(com.sirius.sdk.agent.aries_rfc.feature_0095_basic_message.Message::class.java)
    }

    override fun stop(cause: String) {

    }

    override fun onScenarioEnd(success: Boolean, error: String?) {

    }

    override fun onScenarioStart() {

    }
    override fun start(event: Event) {
        eventStore(event?.message().id,event, false)
        onScenarioEnd(true, null)
    }



    override fun eventStore(id: String, event: Event, accepted: Boolean) {
        val tags = EventWalletStorage.EventTags()
        tags.pairwiseDid = event?.pairwise?.their?.did
        tags.type = "text"
        EventWalletStorage.getInstance().add(event,id,tags)
    }

    override fun eventRemove(id: String) {
        EventWalletStorage.getInstance().delete(id)
    }

    override fun getEvent(id: String): Event? {
       return EventWalletStorage.getInstance().get(id)
    }
}