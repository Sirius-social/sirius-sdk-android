package com.sirius.sdk_android

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sirius.sdk.agent.aries_rfc.concept_0017_attachments.Attach
import com.sirius.sdk.base.JsonSerializable
import org.json.JSONObject

/**
 * All tags should be Strings
 */
open class EventTags() : JsonSerializable<EventTags> {

    var id: String? = null
    var isAccepted: Boolean = false
    var pairwiseDid: String? = null

    override fun serialize(): String? {
        return Gson().toJson(this, EventTags::class.java)
    }

    override fun serializeToJSONObject(): shadow.org.json.JSONObject? {
        return null
    }

    override fun deserialize(string: String?): EventTags {
        return Gson().fromJson(string, EventTags::class.java);
    }

    override fun serializeToJsonObject(): JsonObject? {
        return Gson().toJsonTree(this, EventTags::class.java).asJsonObject
    }
}