package com.sirius.sample.ui.chats.message

import com.sirius.sample.R
import com.sirius.sdk.agent.aries_rfc.feature_0095_basic_message.Message
import com.sirius.sdk.agent.listener.Event

class TextItemMessage(event: Event?) : BaseItemMessage(event) {



    override fun getType(): MessageType {
        return MessageType.Text
    }

    override fun accept() {

    }

    override fun cancel() {

    }

    override fun getText() : String {
        val messageBasic = message as? Message
        return messageBasic?.content ?: ""
    }

    override fun getTitle() : String {
        return ""
    }
}