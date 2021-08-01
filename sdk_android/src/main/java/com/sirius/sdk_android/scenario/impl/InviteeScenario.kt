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

    var storage: EventWalletStorage? = null

    fun initStorage() {
        if (storage == null && SiriusSDK.getInstance().context != null) {
            storage = EventWalletStorage(SiriusSDK.getInstance().context.nonSecrets)
        }
    }

    override fun eventStore(id: String, event: Event) {
        initStorage()
        storage?.add(event,id, null)
    }

    override fun getEvent(id: String): Event? {
        initStorage()
        return storage?.get(id)
    }

    override fun eventRemove(id: String) {
        initStorage()
        storage?.delete(id)
    }

    override fun cancel(id: String, cause: String) {
      //  eventRemove(id)
        //TODO cancel
        // cancel(id,cause)
        onScenarioEnd(false, cause)
    }

    override fun accept(id: String) {
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
        onScenarioEnd(pairwise != null, error)
    }


    override fun start(event: Event) {
        val id = event.message().id
        eventStore(id, event)
        onScenarioEnd(true, null)
    }


}