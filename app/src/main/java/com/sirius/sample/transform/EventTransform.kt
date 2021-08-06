package com.sirius.sample.transform

import com.sirius.sample.models.ui.ItemActions
import com.sirius.sample.models.ui.ItemContacts
import com.sirius.sample.repository.EventRepository
import com.sirius.sample.ui.chats.message.*
import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.messages.OfferCredentialMessage
import com.sirius.sdk.agent.aries_rfc.feature_0037_present_proof.messages.RequestPresentationMessage
import com.sirius.sdk.agent.aries_rfc.feature_0095_basic_message.Message
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnRequest
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.sdk.agent.listener.Event
import java.util.*


class EventTransform() {

    companion object {
        fun eventToItemContacts(event: Event?): ItemContacts {
            if (event == null) {
                return ItemContacts()
            }
            val message = event.message()
            var title = ""
            if (message is Invitation) {
                title = message.label()
            }
            var id = message.id
            if (event.pairwise != null) {
                id = event?.pairwise.their.did
                title = event?.pairwise.their.label
            }
            val contact = ItemContacts(id, title, Date())
            return contact
        }

        fun eventToBaseItemMessage(event: Event?): BaseItemMessage {
            if (event == null) {
                return TextItemMessage(null)
            }
            val message = event.message()
            if (message is Invitation) {
                val message = ConnectItemMessage(event)
                return message
            }
            /* if(message is ConnRequest){
                 return OfferItemMessage(event)
             }*/
            if (message is OfferCredentialMessage) {
                return OfferItemMessage(event)
            }
            if (message is RequestPresentationMessage) {
                return ProverItemMessage(event)
            }
            if (message is Message) {
                return TextItemMessage(event)
            }
            return TextItemMessage(null)
        }

        fun eventToItemActions(event: Event?): ItemActions {
            if (event == null) {
                return ItemActions()
            }
            val message = event.message()
            var type: ItemActions.ActionType? = null
            var hint = ""
            if (message is OfferCredentialMessage) {
                type = ItemActions.ActionType.Offer
                hint = event.pairwise?.their?.label ?: ""
            }
            if (message is Invitation) {
                type = ItemActions.ActionType.Connect
                hint = event.pairwise?.their?.label ?: ""
            }
            return ItemActions(message.id, type, hint)
        }

        fun itemActionToEvent(itemActions: ItemActions?, eventRepository: EventRepository): Event? {
            return eventRepository.getEvent(itemActions?.id ?: "")
        }

        fun itemContactsToEvent(
            itemContacts: ItemContacts?,
            eventRepository: EventRepository
        ): Event? {
            return eventRepository.getEvent(itemContacts?.id ?: "")
        }
    }
}