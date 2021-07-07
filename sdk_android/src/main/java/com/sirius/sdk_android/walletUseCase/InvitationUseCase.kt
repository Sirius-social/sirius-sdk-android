package com.sirius.sdk_android.walletUseCase

import android.util.Base64
import android.util.Log
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.sdk.hub.Context
import com.sirius.sdk.hub.MobileContext
import com.sirius.sdk.messaging.Message

class InvitationUseCase {

    companion object {
        private var invitationUseCase: InvitationUseCase? = null

        @JvmStatic
        fun getInstance(): InvitationUseCase {
            if (invitationUseCase == null) {
                invitationUseCase = InvitationUseCase()
            }
            return invitationUseCase!!
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



    fun startConnectionWithInvitation(message: String) {
        Log.d("mylog500", "startConnectionWithInvitation=$message")
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

    var myConnectionKey : String? = null
    var myDid : String? = null
    fun generateInvitation(label: String): String {
        // Ключ установки соединения. Аналог Bob Pre-key
        //см. [2.4. Keys] https://signal.org/docs/specifications/x3dh/
       // System.out.println("mylog299 generateQrCodeInvitation context crypto=" + WalletUseCase.getInstance().context.crypto)
        val connectionKey =context.crypto.createKey()
        this.myConnectionKey = connectionKey
      //  this.myDid =  WalletUseCase.getInstance().DIDForKey(connectionKey)
        // val sm = Inviter(context, Pairwise.Me("myDid", "myVerkey"), connectionKey, myEndpoint)
        // Теперь сформируем приглашение для других через 0160
        // шаг 1 - определимся какой endpoint мы возьмем, для простоты возьмем endpoint без доп шифрования
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
        // шаг 2 - создаем приглашение
        val invitation = Invitation.builder()
            .setLabel(label)
            .setRecipientKeys(listOf(connectionKey)).setEndpoint(myEndpoint.address).build()

        // шаг 2 - создаем приглашение

        // шаг 3 - согласно Aries-0160 генерируем URL

        // Establish connection with Sirius Communicator via standard Aries protocol
        // https://github.com/hyperledger/aries-rfcs/blob/master/features/0160-connection-protocol/README.md#states

        System.out.println("mylog299 invitation=" + invitation)
        System.out.println("mylog299 invitation=" + invitation.messageObj)
        System.out.println("mylog299 invitation=" + invitation.endpoint())
        val qrContent =context.currentHub.serverUri + invitation.invitationUrl()
        System.out.println("mylog299 qrContent=" + qrContent)
        return qrContent

    }
}