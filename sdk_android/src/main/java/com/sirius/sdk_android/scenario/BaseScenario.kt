package com.sirius.sdk_android.scenario

import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.messaging.Message
import java.util.*

/**
 * This is the sample class just to show how SDK workflow is done. You can extend from it, or make yours
 * to make more complicated scenario.
 */
public abstract class BaseScenario() : ScenarioListener {

    /**
     * list of Message classes that initiate scenario
     */
    abstract fun initMessages() : List<Class<out Message>>

    var id : String = UUID.randomUUID().toString()
    public fun startScenario(event: Event) {
        val ist = initMessages()
        val classOfMessage = event.message()::class.java
        val list =  ist.filter { message->
            classOfMessage == message
        }
        if(list.isNotEmpty()){
            id =  event.message().id
            onScenarioStart(id)
            val pair = start(event)
            onScenarioEnd(id,pair.first, pair.second)
        }
    }

    abstract fun start(event: Event) : Pair<Boolean,String?>
}