package com.sirius.sample.ui.chats.message

import com.sirius.sample.R
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk_android.helpers.ScenarioHelper

class ConnectItemMessage(event: Event) : BaseItemMessage(event) {



    override fun getType(): MessageType {
        return if(isAccepted){
            MessageType.Connected
        }else{
            MessageType.Connect
        }
    }



    override fun accept() {
        ScenarioHelper.getInstance().acceptScenario("Invitee",message?.id ?: "")
    }

    override fun cancel() {
        ScenarioHelper.getInstance().stopScenario("Invitee",message?.id ?: "","Canceled By Me")
    }

    override fun getText(): String {
        return "Sample did label"
    }

    override fun getTitle(): String {
         return "Connect?"
    }
}