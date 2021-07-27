package com.sirius.sdk_android.scenario.impl

import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnRequest
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.state_machines.Inviter
import com.sirius.sdk.agent.connections.Endpoint
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.agent.pairwise.Pairwise
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.scenario.BaseScenario

abstract class InviterScenario() : BaseScenario() {

    var connectionKey : String? =null


    fun generateInvitation()  : String{
        val verkey = SiriusSDK.getInstance().context.crypto.createKey()
        connectionKey =  verkey
        val myEndpoint : Endpoint = SiriusSDK.getInstance().context.endpointWithEmptyRoutingKeys
            ?: return ""
        val invitation = Invitation.builder()
            .setLabel(SiriusSDK.getInstance().label)
            .setRecipientKeys(listOf( verkey)).setEndpoint(myEndpoint.address).build()
        val qrContent =SiriusSDK.getInstance().context.currentHub.serverUri + invitation.invitationUrl()
        return qrContent
    }

    override fun initMessages(): List<Class<out Message>> {
       return listOf(ConnRequest::class.java)
    }

    override fun stop(cause: String) {
        //TODO send problem report
        onScenarioEnd(false,cause)
    }


    override fun start(event: Event) {
        val request = event.message() as ConnRequest
        val didVerkey = SiriusSDK.getInstance().context.did.createAndStoreMyDid()
        var did = didVerkey.first
        var verkey = didVerkey.second
        val inviterMe = Pairwise.Me(did, verkey)
        val machine = Inviter(SiriusSDK.getInstance().context, inviterMe, connectionKey, SiriusSDK.getInstance().context.endpointWithEmptyRoutingKeys)
        val pairwise : Pairwise? = machine.createConnection(request)
        pairwise?.let {
            SiriusSDK.getInstance().context.pairwiseList.ensureExists(it)
        }
        onScenarioEnd(pairwise!=null,null)
    }


}