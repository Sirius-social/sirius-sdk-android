package com.sirius.sdk_android.scenario.impl

import com.sirius.sdk.agent.aries_rfc.feature_0113_question_answer.mesages.AnswerMessage
import com.sirius.sdk.agent.aries_rfc.feature_0113_question_answer.mesages.QuestionMessage
import com.sirius.sdk.agent.aries_rfc.feature_0113_question_answer.mesages.Recipes
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.EventWalletStorage
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.helpers.PairwiseHelper
import com.sirius.sdk_android.scenario.*


abstract class QuestionAnswerScenario(val eventStorage : EventStorageAbstract) : BaseScenario(), EventActionAbstract {
    override fun initMessages(): List<Class<out Message>> {
        return listOf(QuestionMessage::class.java, AnswerMessage::class.java)
    }


    override fun start(event: Event): Pair<Boolean, String?> {
        val id = event.message().id
        if (event.message() is QuestionMessage) {
            val eventPair = EventTransform.eventToPair(event)
            eventStorage.eventStore(id, eventPair, false)
        } else {
            val answerMessage = event.message() as AnswerMessage
            val idQuestion = answerMessage.threadId
            val questionEvent = eventStorage.getEvent(idQuestion)
            questionEvent?.let {
                eventStorage.eventStore(id, questionEvent, true)
            }
        }
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
        val event = eventStorage.getEvent(id)
        val questionMessage = event?.second as? QuestionMessage
        val pairwise = PairwiseHelper.getInstance().getPairwise(event?.first)
        Recipes.makeAnswer(
            SiriusSDK.getInstance().context,
            comment,
            questionMessage,
            pairwise
        )
    }

    fun cancel(id: String, cause: String?,actionListener: EventActionListener?) {

    }
}