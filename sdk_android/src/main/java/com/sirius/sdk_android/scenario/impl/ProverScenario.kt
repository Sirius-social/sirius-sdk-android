package com.sirius.sdk_android.scenario.impl

import com.sirius.sdk.agent.aries_rfc.feature_0037_present_proof.messages.RequestPresentationMessage
import com.sirius.sdk.agent.aries_rfc.feature_0037_present_proof.state_machines.Prover
import com.sirius.sdk.agent.ledger.Ledger
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.EventWalletStorage
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.scenario.BaseScenario
import com.sirius.sdk_android.scenario.EventActionAbstract
import com.sirius.sdk_android.scenario.EventStorageAbstract
import com.sirius.sdk_android.utils.HashUtils

abstract class ProverScenario : BaseScenario() , EventStorageAbstract, EventActionAbstract{

    override fun initMessages(): List<Class<out Message>> {
        return listOf(RequestPresentationMessage::class.java)
    }

    override fun stop(cause: String) {

    }

    override fun start(event: Event) {
        val id = event.message().id
        eventStore(id, event,false)
        onScenarioEnd(true, null)
    }

    override fun onScenarioStart() {

    }

    override fun onScenarioEnd(success: Boolean, error: String?) {

    }

    override fun eventStore(id: String, event: Event, accepted: Boolean) {
        val tags = EventWalletStorage.EventTags()
        tags.isAccepted = accepted.toString()
        tags.type = "prover"
        tags.pairwiseDid = event.pairwise?.their?.did
        EventWalletStorage.getInstance().add(event, id,tags)
    }

    override fun eventRemove(id: String) {
        EventWalletStorage.getInstance().delete(id)
    }

    override fun getEvent(id: String): Event? {
       return EventWalletStorage.getInstance().get(id)
    }

    override fun accept(id: String, comment: String?) {
        val event =  getEvent(id)
        val requestPresentation = event?.message() as? RequestPresentationMessage
        val ttl = 60
        val masterSecretId: String =
            HashUtils.generateHash(SiriusSDK.getInstance().label)
        val proverLedger: Ledger? = SiriusSDK.getInstance().context.getLedgers().get("default")
        proverLedger?.let {
            val machine = Prover(SiriusSDK.getInstance().context, event?.pairwise, proverLedger, masterSecretId)
            machine.prove(requestPresentation)
        }
    }

    override fun cancel(id: String, cause: String?) {

    }
}