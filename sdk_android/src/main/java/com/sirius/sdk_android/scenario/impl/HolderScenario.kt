package com.sirius.sdk_android.scenario.impl

import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.messages.IssueProblemReport
import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.messages.OfferCredentialMessage
import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.state_machines.Holder
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.errors.indy_exceptions.DuplicateMasterSecretNameException
import com.sirius.sdk.hub.coprotocols.CoProtocolP2P
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.EventWalletStorage
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.scenario.BaseScenario
import com.sirius.sdk_android.scenario.EventActionAbstract
import com.sirius.sdk_android.scenario.EventStorageAbstract
import com.sirius.sdk_android.utils.HashUtils

abstract class HolderScenario : BaseScenario() , EventStorageAbstract, EventActionAbstract {

    var holderMachine  : Holder? = null

    override fun initMessages(): List<Class<out Message>> {
        return listOf(OfferCredentialMessage::class.java)
    }

    override fun stop(cause: String) {
        //TODO send problem report
        /*getEvent()
        val coprotocol =  CoProtocolP2P(SiriusSDK.getInstance().context, event?.pairwise, protocols(), timeToLiveSec)) {*/

           /* problemReport = IssueProblemReport.builder().setProblemCode(ex.getProblemCode())
                .setExplain(ex.getExplain()).setDocUri(docUri).build()
            log.info("100% - Terminated with error. " + ex.getProblemCode() + " " + ex.getExplain())
            if (ex.isNotify()) coprotocol.send(problemReport)*/

        val  problemReport = IssueProblemReport.builder().setExplain(cause).build()

        onScenarioEnd( false, cause)
    }

    override fun start(event: Event) {
        try {
            val masterSecretId: String =
                HashUtils.generateHash(SiriusSDK.getInstance().label)
            SiriusSDK.getInstance().context.anonCreds
                .proverCreateMasterSecret(masterSecretId)
        } catch (ignored: DuplicateMasterSecretNameException) {
        }
        eventStore(event.message().id,event,false)
        onScenarioEnd(true,"")
    }


    override fun eventStore(id: String, event: Event, accepted: Boolean) {
        val tags = EventWalletStorage.EventTags()
        tags.isAccepted = accepted.toString()
        tags.type = "offer"
        tags.pairwiseDid = event.pairwise?.their?.did
     //   event.message().messageObjectHasKey("sent_time")
        EventWalletStorage.getInstance().add(event,id, tags)
    }

    override fun accept(id: String, comment : String?) {
        val  locale : String = "en"
        val event =  getEvent(id)
        val masterSecretId: String =
            HashUtils.generateHash(SiriusSDK.getInstance().label)
        holderMachine = Holder(SiriusSDK.getInstance().context,event?.pairwise,masterSecretId)
        val offer = event?.message() as? OfferCredentialMessage
        val result = holderMachine?.accept(offer,comment)
        event?.let {
            eventStore(id,event,result?.first ?: false)
        }
        onScenarioEnd(result?.first ?: false, result?.second)
    }

    override fun cancel(id: String, cause: String?) {
        //  eventRemove(id)
       val event =  getEvent(id)
        //TODO cancel
        // cancel(id,cause)
        event?.let {
            eventStore(id,it,false )
        }
        onScenarioEnd(false, cause)
    }

    override fun getEvent(id: String): Event? {
        return EventWalletStorage.getInstance().get(id)
    }

    override fun eventRemove(id: String) {
        EventWalletStorage.getInstance().delete(id)
    }

    override fun onScenarioStart() {

    }

    override fun onScenarioEnd(success: Boolean, error: String?) {
    }
}