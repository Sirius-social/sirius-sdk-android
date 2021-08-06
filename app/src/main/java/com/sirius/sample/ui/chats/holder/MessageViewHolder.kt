package com.sirius.sample.ui.chats.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sirius.sample.R
import com.sirius.sample.ui.chats.message.BaseItemMessage

open class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        open fun bind(item: BaseItemMessage) {}

        companion object {
            fun getHolderFromType(type: Int, itemView: View): MessageViewHolder {

                if (type == BaseItemMessage.MessageType.Text.ordinal) {
                    return TextMessageViewHolder(itemView)
                }
                if (type == BaseItemMessage.MessageType.Connect.ordinal) {
                    return ConnectMessageViewHolder(itemView)
                }
                if (type == BaseItemMessage.MessageType.Connected.ordinal) {
                    return ConnectedMessageViewHolder(itemView)
                }
                if (type == BaseItemMessage.MessageType.Offer.ordinal) {
                    return OfferMessageViewHolder(itemView)
                }
                if (type == BaseItemMessage.MessageType.OfferAccepted.ordinal) {
                    return OfferAcceptedMessageViewHolder(itemView)
                }
                if (type == BaseItemMessage.MessageType.Prover.ordinal) {
                    return ProverMessageViewHolder(itemView)
                }
                if (type == BaseItemMessage.MessageType.ProverAccepted.ordinal) {
                    return ProverAcceptedMessageViewHolder(itemView)
                }
                return MessageViewHolder(itemView)
            }

            fun getLayoutResFromType(type: Int): Int {
                if (type == BaseItemMessage.MessageType.Text.ordinal) {
                    return R.layout.item_message_text
                }
                if (type == BaseItemMessage.MessageType.Connect.ordinal) {
                    return R.layout.item_message_connect
                }
                if (type == BaseItemMessage.MessageType.Connected.ordinal) {
                    return R.layout.item_message_connect_accepted
                }
                if (type == BaseItemMessage.MessageType.Offer.ordinal) {
                    return R.layout.item_message_offer
                }
                if (type == BaseItemMessage.MessageType.OfferAccepted.ordinal) {
                    return R.layout.item_message_offer_accepted
                }

                if (type == BaseItemMessage.MessageType.Prover.ordinal) {
                    return R.layout.item_message_prover
                }

                if (type == BaseItemMessage.MessageType.ProverAccepted.ordinal) {
                    return R.layout.item_message_prover_accepted
                }
                
                return R.layout.item_message_text
            }
        }

    }