package com.sirius.sdk_android.scenario.impl

import com.sirius.sdk.agent.aries_rfc.feature_0113_question_answer.mesages.AnswerMessage
import com.sirius.sdk.agent.aries_rfc.feature_0113_question_answer.mesages.QuestionMessage
import com.sirius.sdk.agent.aries_rfc.feature_0113_question_answer.mesages.Recipes
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.EventWalletStorage
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.scenario.BaseScenario
import com.sirius.sdk_android.scenario.EventActionAbstract
import com.sirius.sdk_android.scenario.EventStorageAbstract


abstract class QuestionAnswerScenario : BaseScenario(), EventStorageAbstract, EventActionAbstract {
    override fun initMessages(): List<Class<out Message>> {
        return listOf(QuestionMessage::class.java, AnswerMessage::class.java)
    }

    override fun stop(cause: String) {

    }

    override fun start(event: Event) {
        val id = event.message().id
        if(event.message() is QuestionMessage){
            eventStore(id, event,false)
        }else{
           val answerMessage =  event.message() as AnswerMessage
           val idQuestion =  answerMessage.threadId
           val questionEvent = getEvent(idQuestion)
            questionEvent?.let {
                eventStore(id, questionEvent,true)
            }
        }
        onScenarioEnd(true, null)
    }

    override fun onScenarioStart() {

    }

    override fun onScenarioEnd(success: Boolean, error: String?) {

    }

    override fun eventStore(id: String, event: Event, accepted: Boolean) {
        val tags = EventWalletStorage.EventTags()
        tags.isAccepted = accepted.toString()
        tags.type = "question"
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
            val questionMessage = event?.message() as? QuestionMessage
            Recipes.makeAnswer(SiriusSDK.getInstance().context,comment,questionMessage,event?.pairwise)
    }

    override fun cancel(id: String, cause: String?) {

    }
}