package com.sirius.sdk_android.scenario.impl

import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnProblemReport
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.state_machines.Invitee
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.agent.pairwise.Pairwise
import com.sirius.sdk.agent.storages.InWalletImmutableCollection
import com.sirius.sdk.messaging.Message
import com.sirius.sdk.storage.abstract_storage.AbstractImmutableCollection
import com.sirius.sdk_android.EventTags
import com.sirius.sdk_android.EventWalletStorage
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.helpers.PairwiseHelper
import com.sirius.sdk_android.scenario.*
import shadow.org.json.JSONObject

abstract class InviteeScenario(val eventStorage : EventStorageAbstract) : BaseScenario(), EventActionAbstract {


    override fun initMessages(): List<Class<out Message>> {
        return listOf(Invitation::class.java)
    }



    override fun actionStart(action: EventAction, id: String, comment: String?, actionListener: EventActionListener?) {
        if (action == EventAction.accept) {
            accept(id, comment,actionListener)
        } else if (action == EventAction.cancel) {
            cancel(id, comment,actionListener)
        }
    }

    fun cancel(id: String, cause: String?,actionListener: EventActionListener?) {
        actionListener?.onActionStart(EventAction.cancel, id, cause)
        val event = eventStorage.getEvent(id)
        //TODO send problem report
        event?.let {
            eventStorage.eventStore(id, event, false)
        }
        actionListener?.onActionEnd(EventAction.cancel, id, null, false, cause)
    }

    fun accept(id: String, comment: String?,actionListener: EventActionListener?) {
        actionListener?.onActionStart(EventAction.accept, id, comment)
        val event = eventStorage.getEvent(id)
        val invitation = event?.second as? Invitation
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
            eventStorage.eventStore(invitation?.id ?: "", Pair(pairwise?.their?.did, event.second), pairwise != null)
        }
        actionListener?.onActionEnd(EventAction.accept, id, comment, pairwise != null, error)
    }


    override fun start(event: Event): Pair<Boolean, String?> {
        val eventPair = EventTransform.eventToPair(event)
        val id = eventPair.second.id
        eventStorage.eventStore(id, eventPair, false)
        return Pair(true, null)
    }


}