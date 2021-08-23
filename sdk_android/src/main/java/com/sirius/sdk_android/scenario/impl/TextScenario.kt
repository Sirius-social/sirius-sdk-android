package com.sirius.sdk_android.scenario.impl

import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.EventWalletStorage
import com.sirius.sdk_android.scenario.BaseScenario
import com.sirius.sdk_android.scenario.EventStorageAbstract
import com.sirius.sdk_android.scenario.EventTransform

abstract class TextScenario(val eventStorage: EventStorageAbstract) : BaseScenario() {

    override fun initMessages(): List<Class<out Message>> {
        return listOf(com.sirius.sdk.agent.aries_rfc.feature_0095_basic_message.Message::class.java)
    }



    override fun onScenarioEnd(id: String,success: Boolean, error: String?) {

    }

    override fun onScenarioStart(id: String) {

    }

    override fun start(event: Event): Pair<Boolean, String?> {
        val eventPair = EventTransform.eventToPair(event)
        eventStorage.eventStore(eventPair.second.id, eventPair, false)
        return Pair(true, null)
    }

}