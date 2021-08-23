package com.sirius.sdk_android.scenario

import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.messaging.Message

class EventTransform {

    companion object {

        fun eventToPair(event: Event): Pair<String?, Message> {
            var theirDid = event.pairwise?.their?.did
            val message = event.message()
            return Pair(theirDid, message)
        }

    }
}