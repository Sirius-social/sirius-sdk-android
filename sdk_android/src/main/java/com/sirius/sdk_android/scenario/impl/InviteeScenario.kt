package com.sirius.sdk_android.scenario.impl

import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.state_machines.Invitee
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.agent.pairwise.Pairwise
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.scenario.BaseScenario

abstract class InviteeScenario() : BaseScenario() {


    override fun initMessages(): List<Class< out Message>> {
       return listOf(Invitation::class.java)
    }

    override fun stop(cause: String) {

    }

    override fun start(event: Event) {
        val invitation =   event.message() as Invitation
        val didVerkey = SiriusSDK.getInstance().context.did.createAndStoreMyDid()
        var myDid =  didVerkey.first
        var myConnectionKey =  didVerkey.second
        val me = Pairwise.Me(myDid, myConnectionKey)
        val machine = Invitee(SiriusSDK.getInstance().context, me, SiriusSDK.getInstance().context.endpoints.get(0))
        val pairwise : Pairwise?= machine.createConnection(invitation, SiriusSDK.getInstance().label)
        pairwise?.let {
            SiriusSDK.getInstance().context.pairwiseList.ensureExists(it)
        }
        onScenarioEnd(pairwise!=null,null)
    }



}