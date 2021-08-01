package com.sirius.sdk_android.scenario.impl

import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.messages.OfferCredentialMessage
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.agent.pairwise.Pairwise
import com.sirius.sdk.errors.indy_exceptions.DuplicateMasterSecretNameException
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.scenario.BaseScenario
import com.sirius.sdk_android.utils.HashUtils

abstract class HolderWrapperScenario : BaseScenario() {


    interface HolderListener{
       fun onScenarioEnd(id : String , success: Boolean, error: String?)
       fun onScenarioStart(id : String)
    }

    abstract fun holderEventStore(id : String, event: Event)
    abstract fun holderEventRemove(id : String)
    abstract fun getHolderEvent(id : String) : Event?

    fun acceptHolder(id : String, holderListener : HolderListener ){
       val holder =  object : HolderScenario(){
           override fun onScenarioEnd(success: Boolean, error: String?) {
               holderListener.onScenarioEnd(id,success,error)
               holderEventRemove(id)
           }

           override fun onScenarioStart() {
               holderListener.onScenarioStart(id)
           }

       }
        val event = getHolderEvent(id)
        event?.let {
            holder.startScenario(it)
        }

    }

    fun cancelHolder(id : String,cause: String, holderListener : HolderListener ){
        val holder =  object : HolderScenario(){
            override fun onScenarioEnd(success: Boolean, error: String?) {
                holderListener.onScenarioEnd(id,success,error)
                holderEventRemove(id)
            }

            override fun onScenarioStart() {
                holderListener.onScenarioStart(id)
            }

        }
        val event = getHolderEvent(id)
        holder.stop(cause)
    }

    override fun initMessages(): List<Class<out Message>> {
        return listOf(OfferCredentialMessage::class.java)
    }

    override fun stop(cause: String) {

    }

    override fun start(event: Event) {
        try {
            val masterSecretId: String =
                HashUtils.generateHash(SiriusSDK.getInstance().label)
            SiriusSDK.getInstance().context.anonCreds
                .proverCreateMasterSecret(masterSecretId)
        } catch (ignored: DuplicateMasterSecretNameException) {
        }
        holderEventStore(event.message().id,event)
        onScenarioEnd(true,"")
    }


    override fun onScenarioEnd(success: Boolean, error: String?) {

    }

    override fun onScenarioStart() {

    }


}