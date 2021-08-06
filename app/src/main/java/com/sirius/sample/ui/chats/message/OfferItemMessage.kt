package com.sirius.sample.ui.chats.message

import com.sirius.sample.models.ui.ItemCredentialsDetails
import com.sirius.sample.utils.DateUtils
import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.messages.OfferCredentialMessage
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk_android.helpers.ScenarioHelper


import java.util.*

class OfferItemMessage(event: Event) : BaseItemMessage(event) {


    override fun getType(): MessageType {
        return if (isAccepted) {
            MessageType.OfferAccepted
        } else {
            MessageType.Offer
        }
    }

    var hint: String? = null
    var expiresTime: Date? = null
    var detailList: List<ItemCredentialsDetails>? = null
    var name: String? = null
    override fun setupFromEvent(event: Event?) {
        super.setupFromEvent(event)
        val offerMessage = event?.message() as? OfferCredentialMessage
        val preview = offerMessage?.credentialPreview

        detailList = preview?.map {
            ItemCredentialsDetails(it.name, it.value)
        }
        hint = offerMessage?.comment

        val timeObj = offerMessage?.getJSONOBJECTFromJSON("expires_time")
        val timeString = timeObj?.getString("~timing")
        expiresTime = DateUtils.getDateFromString(timeString, "yyyy-MM-dd'T'HH:mm:ss.SSSZ", false)

        var offerAttaches = offerMessage?.getMessageObj()?.getJSONArray("~attach")
        if (offerAttaches != null) {
            val att = offerAttaches.optJSONObject(0)
            if (att != null) {
                val type = att.optString("@type") ?: ""
                if (type.endsWith("/issuer-schema")) {
                    val dataObject = att.optJSONObject("data")
                    val jsonObject = dataObject?.optJSONObject("json")
                    name = jsonObject?.optString("name")
                }
            }
        }
    }


    override fun accept() {
        ScenarioHelper.getInstance().acceptScenario("Holder", message?.id ?: "")
    }

    override fun cancel() {
        ScenarioHelper.getInstance().stopScenario("Holder", message?.id ?: "", "Canceled By Me")
    }

    override fun getText(): String {
        return "offer sample"
    }

    override fun getTitle(): String {
        return name ?: ""
    }
}