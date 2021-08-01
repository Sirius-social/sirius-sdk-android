package com.sirius.sdk_android

import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.agent.wallet.abstract_wallet.AbstractNonSecrets
import com.sirius.sdk.agent.wallet.abstract_wallet.model.RetrieveRecordOptions
import com.sirius.sdk.storage.abstract_storage.AbstractImmutableCollection
import com.sirius.sdk.storage.abstract_storage.AbstractKeyValueStorage
import com.sirius.sdk.utils.Pair
import com.sirius.sdk_android.helpers.PairwiseHelper
import shadow.org.json.JSONObject

import java.util.*

class EventWalletStorage(val storage: AbstractNonSecrets) {

    var DEFAULT_FETCH_LIMIT = 1000
    var selectedDb: String = "event"


    fun add(event: Event, key: String, tags: String?) {
        val eventObject = serializeEvent(event)
        val eventGet = get(key)
        if (eventGet == null) {
            storage.addWalletRecord(selectedDb, key, eventObject.toString(), tags)
        } else {
            storage.updateWalletRecordValue(selectedDb, key, eventObject.toString())
        }
    }


    fun get(key: String?): Event? {
        var event: Event? = null

        val string =
            storage.getWalletRecord(selectedDb, key, RetrieveRecordOptions(false, true, false))
        if (string != null) {
            val jsonObject = JSONObject(string)
            val values = jsonObject.optString("value")
            event = restoreEvent(values)
        }
        return event
    }

    fun delete(key: String?) {
        storage.deleteWalletRecord(selectedDb, key)
    }

    fun serializeEvent(event: Event): JSONObject {
        val messageObject = event.serializeToJSONObject()
        val eventObject = JSONObject()
        val theirVerkey = event.pairwise?.their?.verkey
        val theirDid = event.pairwise?.their?.did
        eventObject.put("message", messageObject)
        eventObject.put("pairwiseVerkey", theirVerkey)
        eventObject.put("pairwiseDid", theirDid)
        return eventObject
    }

    fun restoreEvent(eventString: String?): Event? {
        if (eventString == null) {
            return null
        }
        val eventObject = JSONObject(eventString)
        val message = eventObject.optJSONObject("message")
        val pairwiseVerkey = eventObject.optString("pairwiseVerkey")
        val pairwiseDid = eventObject.optString("pairwiseDid")
        val pairwise = PairwiseHelper.getInstance().getPairwise(theirDid = pairwiseDid)
        return Event(pairwise, message.toString())
    }


    fun fetch(limit: Int = DEFAULT_FETCH_LIMIT, tags: String? = null): Pair<List<Event>, Int> {
        var searchString = "{}"
        if(!tags.isNullOrEmpty()){
            searchString = tags
        }
        val result = storage.walletSearch(
            selectedDb, searchString,
            RetrieveRecordOptions(false, true, false), limit
        )
        return if (result.first != null && result.first !== org.json.JSONObject.NULL) {
            val listValue: MutableList<Event> = ArrayList()
            for (i in result.first!!.indices) {
                val `object`: Any = result.first!![i]
                val jsonObject = JSONObject(`object`.toString())
                val values = jsonObject.optString("value")
                val event = restoreEvent(values)
                if (event != null) {
                    listValue.add(event)
                }
            }
            Pair(listValue, result.second)
        } else {
            Pair(ArrayList(), result.second)
        }
    }

}