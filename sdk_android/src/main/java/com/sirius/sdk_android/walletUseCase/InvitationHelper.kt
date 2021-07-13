package com.sirius.sdk_android.walletUseCase

import android.util.Base64
import android.util.Log
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnRequest
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.state_machines.Invitee
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.state_machines.Inviter
import com.sirius.sdk.agent.pairwise.Pairwise
import com.sirius.sdk.hub.MobileContext
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.SiriusSDK

class InvitationHelper {

    companion object {
        private var invitationHelper: InvitationHelper? = null

        @JvmStatic
        fun getInstance(): InvitationHelper {
            if (invitationHelper == null) {
                invitationHelper = InvitationHelper()
            }
            return invitationHelper!!
        }
    }

    lateinit var context: MobileContext


    fun parseInvitationLink(rawValue: String?): String? {
        var parsedString = ""
        if (rawValue != null) {
            var ciParam: String? = null
            Log.d("mylog500", "rawValue=$rawValue")
            if (rawValue.contains("?c_i=")) {
                val ciParamStart = rawValue.indexOf("?c_i=")
                ciParam = rawValue.substring(ciParamStart + 5)
            }
            if (ciParam != null) {
                try {
                    Log.d("mylog500", "ciParam=$ciParam")
                    parsedString = String(
                        Base64.decode(
                            ciParam.toByteArray(charset("UTF-8")),
                            Base64.NO_WRAP or Base64.URL_SAFE
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Log.d("mylog500", "decoded=$parsedString")
            }
        }
        if (validateInvitationUrl(parsedString)) {
            return parsedString
        }
        return null

    }



    fun startInvitee(invitation: Invitation) {
        val didVerkey = context.did.createAndStoreMyDid()
        var myDid =  didVerkey.first
        var myConnectionKey =  didVerkey.second
        val me = Pairwise.Me(myDid, myConnectionKey)
        val machine = Invitee(context, me, context.endpoints.get(0))
        val pairwise : Pairwise?= machine.createConnection(invitation, SiriusSDK.getInstance().label)
        pairwise?.let {
            context.pairwiseList.ensureExists(it)
        }
    }

    fun startInviter(request: ConnRequest){
        val didVerkey = context.did.createAndStoreMyDid()
        var did = didVerkey.first
        var verkey = didVerkey.second
        val inviterMe = Pairwise.Me(did, verkey)
        val machine = Inviter(context, inviterMe, verkey, context.endpoints.get(0))
        val pairwise : Pairwise? = machine.createConnection(request)
        pairwise?.let {
            context.pairwiseList.ensureExists(it)
        }
    }

    fun validateInvitationUrl(url: String): Boolean {
        try{
            val message = Message.restoreMessageInstance(url)
            if (message.first) {
                if (message.second is Invitation) {
                    return true
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
        return false
    }



    fun generateInvitation(): String {
        val verkey = context.crypto.createKey()
       // myDid =  didVerkey.first
        val endpoints = context.endpoints
        var myEndpoint: com.sirius.sdk.agent.connections.Endpoint? = null
        for (e in endpoints) {
            if (e.routingKeys.isEmpty()) {
                myEndpoint = e
                break
            }
        }
        System.out.println("mylog299 myEndpoint=" + myEndpoint)
        if (myEndpoint == null) return ""
        val invitation = Invitation.builder()
            .setLabel(SiriusSDK.getInstance().label)
            .setRecipientKeys(listOf( verkey)).setEndpoint(myEndpoint.address).build()
        System.out.println("mylog299 invitation=" + invitation)
        System.out.println("mylog299 invitation=" + invitation.messageObj)
        System.out.println("mylog299 invitation=" + invitation.endpoint())
        val qrContent =context.currentHub.serverUri + invitation.invitationUrl()
        System.out.println("mylog299 qrContent=" + qrContent)
        return qrContent

    }
}