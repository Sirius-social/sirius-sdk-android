package com.sirius.sdk_android.scenario


import com.sirius.sdk.agent.pairwise.Pairwise
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.EventTags
import org.json.JSONObject

interface EventStorageAbstract {
     fun eventStore(id : String, event: Pair<String?, Message>?, accepted : Boolean)
     fun eventRemove(id : String)
     fun getEvent(id : String) : Pair<String?, Message>?
}