package com.sirius.sdk_android.scenario.impl

import com.sirius.sdk.agent.aries_rfc.feature_0037_present_proof.messages.RequestPresentationMessage
import com.sirius.sdk.agent.aries_rfc.feature_0037_present_proof.state_machines.Prover
import com.sirius.sdk.agent.ledger.Ledger
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.EventWalletStorage
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.helpers.PairwiseHelper
import com.sirius.sdk_android.scenario.*
import com.sirius.sdk_android.utils.HashUtils

abstract class ProverScenario(val eventStorage : EventStorageAbstract) : BaseScenario(), EventActionAbstract {

    override fun initMessages(): List<Class<out Message>> {
        return listOf(RequestPresentationMessage::class.java)
    }



    override fun start(event: Event): Pair<Boolean, String?> {
        val eventPair = EventTransform.eventToPair(event)
        val id = eventPair.second.id
        eventStorage.eventStore(id, eventPair, false)
        return Pair(true, null)
    }

    override fun onScenarioStart(id: String) {

    }

    override fun onScenarioEnd(id: String,success: Boolean, error: String?) {

    }


    override fun actionStart(action: EventAction, id: String, comment: String?, actionListener: EventActionListener?) {
        if (action == EventAction.accept) {
            accept(id, comment,actionListener)
        } else if (action == EventAction.cancel) {
            cancel(id, comment,actionListener)
        }
    }

    fun accept(id: String, comment: String?,actionListener: EventActionListener?) {
        actionListener?.onActionStart(EventAction.accept, id, comment)
        val event = eventStorage.getEvent(id)
        val requestPresentation = event?.second as? RequestPresentationMessage
        val ttl = 60
        val pairwise = PairwiseHelper.getInstance().getPairwise(event?.first)
        val masterSecretId: String =
            HashUtils.generateHash(SiriusSDK.getInstance().label)
        // val proverLedger: Ledger? = SiriusSDK.getInstance().context.getLedgers().get("default")
        // proverLedger?.let {
        val machine = Prover(SiriusSDK.getInstance().context, pairwise, masterSecretId)
        var isProved = false
        try{
             isProved = machine.prove(requestPresentation)
        }catch (e : Exception){
            e.printStackTrace()
        }
        val text = machine.problemReport
        event?.let {
            eventStorage.eventStore(id, event, isProved)
        }
        actionListener?.onActionEnd(EventAction.accept, id, comment, isProved, text?.explain)
    }

    fun cancel(id: String, cause: String?,actionListener: EventActionListener?) {
        actionListener?.onActionStart(EventAction.cancel, id, cause)
        val event = eventStorage.getEvent(id)
        //TODO send problem report
        event?.let {
            eventStorage.eventStore(id, event, false)
        }
        actionListener?.onActionEnd(EventAction.accept, id, null, false, cause)
    }
}