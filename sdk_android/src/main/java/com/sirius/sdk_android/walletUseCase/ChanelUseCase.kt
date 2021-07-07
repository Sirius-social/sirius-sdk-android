package com.sirius.sdk_android.walletUseCase


import android.util.Log
import com.sirius.sdk.agent.aries_rfc.feature_0095_basic_message.Message
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnRequest
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.state_machines.Inviter
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.agent.listener.Listener
import com.sirius.sdk.agent.pairwise.Pairwise.Me
import com.sirius.sdk.hub.MobileContext
import org.hyperledger.indy.sdk.crypto.Crypto
import java.util.*


class ChanelUseCase {


    companion object {
        private var chanelUseCase: ChanelUseCase? = null

        @JvmStatic
        fun getInstance(): ChanelUseCase {
            if (chanelUseCase == null) {
                chanelUseCase = ChanelUseCase()
            }
            return chanelUseCase!!
        }
    }

    fun initListener() {
        Thread(Runnable {
            try {
                listener = context.subscribe()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }).start()
    }

    lateinit var context: MobileContext
     var listener: Listener? = null


    fun parseMessage(message: String) {
        Thread(Runnable {
            try {
                context.currentHub.agent.receiveMsg( message.toByteArray(charset("UTF-8")))
            }catch (e : java.lang.Exception){
                e.printStackTrace()
            }
        }).start()
    }

    private fun parseMessageByScenario(event: Event) {
        Log.d("mylog2090","event.message type" + event.message())
        if (event.message() is Invitation) {
            parseInvitation(event)
        } else if (event.message() is ConnRequest) {
            parseConnectionRequest(event)
        }
    }

    fun parseInvitation(event: Event){

    }
    fun parseConnectionRequest(event: Event) {
        // В рамках Samples интересны только запросы 0160 на установку соединения для connection_key нашего QR
            val request = event.message() as ConnRequest
            // Establish connection with Sirius Communicator via standard Aries protocol
            // https://github.com/hyperledger/aries-rfcs/blob/master/features/0160-connection-protocol/README.md#states
            val sm = Inviter(context, Me(InvitationUseCase.getInstance().myDid, InvitationUseCase.getInstance().myConnectionKey), event.recipientVerkey, context.endpoints.get(0))
            val p2p = sm.createConnection(request)
            if (p2p != null) {
                // Ensure pairwise is stored
                context.pairwiseList.ensureExists(p2p)
                val hello = Message.builder().setContent(
                    "Привет в новый МИР!!!" + Date()
                        .toString()
                ).setLocale("ru").build()
                println("Sending hello")
                context.sendTo(hello, p2p)
                println("sended")
            }

    }
}