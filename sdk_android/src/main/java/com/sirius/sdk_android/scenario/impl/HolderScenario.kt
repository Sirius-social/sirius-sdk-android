package com.sirius.sdk_android.scenario.impl

import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.messages.IssueProblemReport
import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.messages.OfferCredentialMessage
import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.state_machines.Holder
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.scenario.BaseScenario
import com.sirius.sdk_android.utils.HashUtils

abstract class HolderScenario : BaseScenario() {

    var holderMachine  : Holder? = null

    override fun initMessages(): List<Class<out Message>> {
        return listOf(OfferCredentialMessage::class.java)
    }

    override fun stop(cause: String) {
        //TODO send problem report
       val  problemReport = IssueProblemReport.builder().setExplain(cause).build()
        onScenarioEnd( false, cause)
    }

    override fun start(event: Event) {
      val  locale : String = "en"
        val masterSecretId: String =
            HashUtils.generateHash(SiriusSDK.getInstance().label)
        holderMachine = Holder(SiriusSDK.getInstance().context,event.pairwise,masterSecretId)
        val offer = event.message() as OfferCredentialMessage
        acceptOffer(offer)
    }


    fun acceptOffer(message : OfferCredentialMessage, comment:String = ""){
        val result = holderMachine?.accept(message,comment)
        onScenarioEnd(result?.first ?: false, result?.second)
    }

}