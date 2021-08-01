package com.sirius.sample.models.ui.message

import com.sirius.sample.R
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk_android.helpers.ChanelHelper
import com.sirius.sdk_android.helpers.ScenarioHelper

class ConnectItemMessage : BaseItemMessage() {

    var event: Event? =null

    override fun getLayoutRes(): Int {
        return R.layout.item_message_connect
    }

    override fun getType(): MessageType {
        return MessageType.Connect
    }

    override fun accept() {
        ScenarioHelper.getInstance().acceptScenario("Invitee",event?.id ?: "")
    }

    override fun cancel() {
        ScenarioHelper.getInstance().stopScenario("Invitee","Canceled By Me")
    }
}