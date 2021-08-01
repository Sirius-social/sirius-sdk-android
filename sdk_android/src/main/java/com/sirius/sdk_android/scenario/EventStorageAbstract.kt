package com.sirius.sdk_android.scenario

import com.sirius.sdk.agent.listener.Event

interface EventStorageAbstract {

     fun eventStore(id : String, event: Event)
     fun eventRemove(id : String)
     fun getEvent(id : String) : Event?

}