package com.sirius.sdk_android.scenario.impl

import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.messages.IssueProblemReport
import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.messages.OfferCredentialMessage
import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.state_machines.Holder
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.errors.indy_exceptions.DuplicateMasterSecretNameException
import com.sirius.sdk.hub.coprotocols.CoProtocolP2P
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.EventTags
import com.sirius.sdk_android.EventWalletStorage
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.helpers.PairwiseHelper
import com.sirius.sdk_android.scenario.*
import com.sirius.sdk_android.utils.HashUtils
import org.json.JSONObject

abstract class HolderScenario(val eventStorage: EventStorageAbstract) : BaseScenario(),
    EventActionAbstract {

    var holderMachine: Holder? = null

    override fun initMessages(): List<Class<out Message>> {
        return listOf(OfferCredentialMessage::class.java)
    }

/*    override fun stop(cause: String) {
        //TODO send problem report*/
    /*getEvent()
    val coprotocol =  CoProtocolP2P(SiriusSDK.getInstance().context, event?.pairwise, protocols(), timeToLiveSec)) {*/

    /* problemReport = IssueProblemReport.builder().setProblemCode(ex.getProblemCode())
         .setExplain(ex.getExplain()).setDocUri(docUri).build()
     log.info("100% - Terminated with error. " + ex.getProblemCode() + " " + ex.getExplain())
     if (ex.isNotify()) coprotocol.send(problemReport)*/

    /*     val problemReport = IssueProblemReport.builder().setExplain(cause).build()

         onScenarioEnd(id,false, cause)
     }*/

    override fun start(event: Event): Pair<Boolean, String?> {
        try {
            val masterSecretId: String =
                HashUtils.generateHash(SiriusSDK.getInstance().label)
            SiriusSDK.getInstance().context.anonCreds
                .proverCreateMasterSecret(masterSecretId)
        } catch (ignored: DuplicateMasterSecretNameException) {
        }
        val pair = EventTransform.eventToPair(event)
        eventStorage.eventStore(pair.second.id, pair, false)
        return Pair(true, "")
    }


    override fun actionStart(
        action: EventAction,
        id: String,
        comment: String?,
        actionListener: EventActionListener?
    ) {
        if (action == EventAction.accept) {
            accept(id, comment, actionListener)
        } else if (action == EventAction.cancel) {
            cancel(id, comment, actionListener)
        }
    }

    fun accept(id: String, comment: String?, eventActionListener: EventActionListener?) {
        eventActionListener?.onActionStart(EventAction.accept, id, comment)
        val locale: String = "en"
        val event = eventStorage.getEvent(id)
        val pairwise = PairwiseHelper.getInstance().getPairwise(event?.first)
        val masterSecretId: String =
            HashUtils.generateHash(SiriusSDK.getInstance().label)
        holderMachine = Holder(SiriusSDK.getInstance().context, pairwise, masterSecretId)
        val offer = event?.second as? OfferCredentialMessage
        var error: String? = null
        var result: com.sirius.sdk.utils.Pair<Boolean, String>? =
            com.sirius.sdk.utils.Pair(false, error)
        try {
            result = holderMachine?.accept(offer, comment)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        event?.let {
            eventStorage.eventStore(id, event, result?.first ?: false)
        }
        eventActionListener?.onActionEnd(
            EventAction.accept,
            id,
            comment,
            result?.first ?: false,
            result?.second
        )
    }

    fun cancel(id: String, cause: String?, eventActionListener: EventActionListener?) {
        eventActionListener?.onActionStart(EventAction.cancel, id, cause)
        val event = eventStorage.getEvent(id)
        //TODO send problem report
        event?.let {
            eventStorage.eventStore(id, event, false)
        }
        eventActionListener?.onActionEnd(EventAction.cancel, id, null, false, cause)
    }


    override fun onScenarioStart(id: String) {

    }

    override fun onScenarioEnd(id: String, success: Boolean, error: String?) {
    }
}