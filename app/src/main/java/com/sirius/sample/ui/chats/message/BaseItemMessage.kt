package com.sirius.sample.ui.chats.message

import android.util.Log
import com.sirius.sample.utils.DateUtils
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.messaging.Message

import java.util.*

abstract class BaseItemMessage {


    constructor(){}
    constructor(event: Event?){
        setupFromEvent(event)
    }

    var isAccepted: Boolean = false
    var isMine: Boolean = false
    var message: Message? = null
    var date: Date? = null


   open fun setupFromEvent(event: Event?){
        event?.let {
            if(it.messageObjectHasKey("tags")){
                val tags = it.messageObj.optJSONObject("tags")
                isAccepted =  tags.optBoolean("isAccepted")
            }
            if(it.messageObjectHasKey("me")){
                isMine = true
            }
            if(it.message().messageObjectHasKey("sent_time")){
                val sentTime = it.message().getStringFromJSON("sent_time")
                date = DateUtils.getDateFromString(
                    sentTime,
                  DateUtils.PATTERN_ROSTER_STATUS_RESPONSE2,true)

            }

            message = it.message()
        }

    }

    enum class MessageType{
        Base,
        Text,
        Connect,
        Connected,
        Offer,
        OfferAccepted,
        Prover,
        ProverAccepted
    }

    abstract fun getType() : MessageType

    abstract fun accept()

    abstract fun cancel()


    abstract fun getText() : String

    abstract fun getTitle() : String

}