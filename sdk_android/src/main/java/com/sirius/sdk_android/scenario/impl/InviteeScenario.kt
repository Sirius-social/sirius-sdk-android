package com.sirius.sdk_android.scenario.impl

import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnProblemReport
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.state_machines.Invitee
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.agent.pairwise.Pairwise
import com.sirius.sdk.agent.storages.InWalletImmutableCollection
import com.sirius.sdk.messaging.Message
import com.sirius.sdk.storage.abstract_storage.AbstractImmutableCollection
import com.sirius.sdk_android.EventWalletStorage
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.helpers.PairwiseHelper
import com.sirius.sdk_android.scenario.BaseScenario
import com.sirius.sdk_android.scenario.EventActionAbstract
import com.sirius.sdk_android.scenario.EventStorageAbstract
import shadow.org.json.JSONObject

abstract class InviteeScenario() : BaseScenario(), EventStorageAbstract, EventActionAbstract {


    override fun initMessages(): List<Class<out Message>> {
        return listOf(Invitation::class.java)
    }

    override fun stop(cause: String) {

        // eventRemove(id)
        //   cancel(id,cause)
        onScenarioEnd(false, cause)
    }


    override fun eventStore(id: String, event: Event, accepted : Boolean) {
      /*  val tags = JSONObject()
        val recipentKey = event.recipientVerkey
        val pairwisedid = event.pairwise?.their?.did
        tags.put("type", "invitee")
        tags.put("isAccepted", accepted)*/
        val tags = EventWalletStorage.EventTags()
        tags.isAccepted = accepted.toString()
        tags.type = "connect"
        EventWalletStorage.getInstance().add(event,id, tags)
    }

   fun eventStorePairwise(id: String, event: Event, accepted : Boolean){
       val tags = EventWalletStorage.EventTags()
       tags.isAccepted = accepted.toString()
       tags.type = "connect"
       tags.pairwiseDid = event?.pairwise?.their?.did
       EventWalletStorage.getInstance().add(event,id, tags)
    }

    override fun getEvent(id: String): Event? {
        return EventWalletStorage.getInstance().get(id)
    }

    override fun eventRemove(id: String) {
        EventWalletStorage.getInstance().delete(id)
    }

    override fun cancel(id: String, cause: String?) {
      //  eventRemove(id)
        //TODO cancel
        // cancel(id,cause)
        onScenarioEnd(false, cause)
    }

    override fun accept(id: String, comment : String?) {
        onScenarioStart()
        val event = getEvent(id)
        val invitation = event?.message() as? Invitation
        val didVerkey = SiriusSDK.getInstance().context.did.createAndStoreMyDid()
        var myDid = didVerkey.first
        var myConnectionKey = didVerkey.second
        val me = Pairwise.Me(myDid, myConnectionKey)
        val machine = Invitee(
            SiriusSDK.getInstance().context,
            me,
            SiriusSDK.getInstance().context.getEndpointWithEmptyRoutingKeys()
        )
        val pairwise: Pairwise? =
            machine.createConnection(invitation, SiriusSDK.getInstance().label)
        pairwise?.let {
            SiriusSDK.getInstance().context.pairwiseList.ensureExists(it)
        }
        var error: String? = null
        if (pairwise == null) {
            val problemReport: ConnProblemReport? = machine.problemReport
            problemReport?.let {
                error = problemReport.explain
            }
        }
        event?.let {
            val eventPairwise = Event(pairwise, it.messageObj.toString())
            eventStorePairwise(invitation?.id ?: "",eventPairwise,pairwise != null)
        }
        onScenarioEnd(pairwise != null, error)
    }


    override fun start(event: Event) {
        val id = event.message().id
        eventStore(id, event,false)
        onScenarioEnd(true, null)
    }


}