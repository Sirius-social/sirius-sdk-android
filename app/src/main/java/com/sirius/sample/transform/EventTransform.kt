package com.sirius.sample.transform

import android.telecom.ConnectionRequest
import com.sirius.sample.models.ui.ItemActions
import com.sirius.sample.models.ui.ItemContacts
import com.sirius.sample.models.ui.message.BaseItemMessage
import com.sirius.sample.models.ui.message.ConnectItemMessage
import com.sirius.sample.models.ui.message.TextItemMessage
import com.sirius.sample.repository.EventRepository
import com.sirius.sample.repository.SDKUseCase
import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.messages.OfferCredentialMessage
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnRequest
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.sdk.agent.listener.Event
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


class EventTransform  () {

    companion object {
        fun eventToItemContacts(event :Event?) : ItemContacts{
            if(event==null){
                return ItemContacts()
            }
           val message =  event.message()
            if(message is Invitation){
               val title =  message.label()
              val contact =   ItemContacts(message.id,title, Date())
                return contact
            }
            return ItemContacts()
        }

        fun eventToBaseItemMessage(event :Event?) : BaseItemMessage{
            if(event==null){
                return TextItemMessage()
            }
            val message =  event.message()
            if(message is Invitation){
                val message  = ConnectItemMessage()
                message.event = event
                return message
            }
            if(message is ConnRequest){
                return ConnectItemMessage()
            }
            return TextItemMessage()
        }

        fun eventToItemActions(event :Event?) :  ItemActions{
            if(event==null){
                return ItemActions()
            }
            val message = event.message()
            var type : ItemActions.ActionType? = null
            var hint = ""
            if(message is OfferCredentialMessage){
                type = ItemActions.ActionType.Offer
                hint =  event.pairwise?.their?.label ?:""
            }
            if(message is Invitation){
                type = ItemActions.ActionType.Connect
                hint =  event.pairwise?.their?.label ?:""
            }
           return  ItemActions(message.id,type, hint)
        }

        fun itemActionToEvent(itemActions: ItemActions?, eventRepository: EventRepository) : Event?{
            return eventRepository.getEvent(itemActions?.id ?:"")
        }

        fun itemContactsToEvent(itemContacts: ItemContacts?, eventRepository: EventRepository) : Event?{
            return eventRepository.getEvent(itemContacts?.id ?:"")
        }
    }
}