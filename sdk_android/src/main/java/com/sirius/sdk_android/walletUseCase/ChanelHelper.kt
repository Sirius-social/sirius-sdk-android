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
import java.util.*
import java.util.concurrent.TimeUnit


class ChanelHelper {


    companion object {
        private var chanelHelper: ChanelHelper? = null

        @JvmStatic
        fun getInstance(): ChanelHelper {
            if (chanelHelper == null) {
                chanelHelper = ChanelHelper()
            }
            return chanelHelper!!
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


    interface EventListener{
        fun onEvent(event: Event)
    }


    fun addListener(eventListener: EventListener)  {
        Thread(Runnable {
            try {
               val listener = context.subscribe()
                while(true){
                    try{
                        val cf = listener!!.one
                        val event = cf[60, TimeUnit.SECONDS]
                        eventListener.onEvent(event)
                        Log.d("mylog2090","addListener event.message " + event?.message())
                        Log.d("mylog2090","addListener event.recipientVerkey " + event?.recipientVerkey)
                        Log.d("mylog2090","addListener event.senderVerkey " + event?.senderVerkey)
                    }catch (e : java.lang.Exception){
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }).start()
    }

    fun removeListener(){

    }


    lateinit var context: MobileContext
     var listener: Listener? = null


    fun parseMessage(message: String) {
        Thread(Runnable {
            try {
             //  val event =  listener?.one?.get()
                val cf = listener!!.one
                context.currentHub.agent.receiveMsg( message.toByteArray(charset("UTF-8")))
              //  mobileAgent.receiveMsg(bytesMsg)
                val event = cf[60, TimeUnit.SECONDS]
                Log.d("mylog2090","event.message " + event?.message())
                Log.d("mylog2090","event.recipientVerkey " + event?.recipientVerkey)
                Log.d("mylog2090","event.senderVerkey " + event?.senderVerkey)
                Log.d("mylog2090","event.pairwise " + event?.pairwise)
                parseMessageByScenario(event)

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
        val invitation = event.message() as Invitation
        InvitationHelper.getInstance().startInvitee(invitation)
    }


    fun parseConnectionRequest(event: Event) {
        Log.d("mylog2090","event.ConnRequest " + event?.messageObj)
        val request = event.message() as ConnRequest
        InvitationHelper.getInstance().startInviter(request)
    }
}