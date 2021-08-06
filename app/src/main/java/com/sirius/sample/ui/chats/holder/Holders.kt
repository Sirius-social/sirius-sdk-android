package com.sirius.sample.ui.chats.holder

import android.view.View
import androidx.databinding.DataBindingUtil
import com.sirius.sample.databinding.ItemMessageConnectBinding
import com.sirius.sample.databinding.ItemMessageOfferBinding
import com.sirius.sample.databinding.ItemMessageProverBinding
import com.sirius.sample.databinding.ItemMessageTextBinding
import com.sirius.sample.ui.chats.message.BaseItemMessage
import com.sirius.sample.ui.chats.message.OfferItemMessage
import com.sirius.sample.ui.chats.message.ProverItemMessage
import com.sirius.sample.ui.credentials.CredentialsDetailAdapter

class TextMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
    var binding: ItemMessageTextBinding? =
        DataBindingUtil.bind<ItemMessageTextBinding>(itemView)

    override fun bind(item: BaseItemMessage) {
        binding?.item = item
    }
}

class OfferMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
    var binding: ItemMessageOfferBinding? =
        DataBindingUtil.bind<ItemMessageOfferBinding>(itemView)

    override fun bind(item: BaseItemMessage) {
        val offer = item as OfferItemMessage
        binding?.item = offer
        val adapter = CredentialsDetailAdapter()
        adapter.setDataList(offer.detailList)
        binding?.attachList?.isNestedScrollingEnabled = false
        binding?.attachList?.adapter = adapter
        binding?.cancelBtn?.setOnClickListener {
            item.cancel()
        }

        binding?.yesBtn?.setOnClickListener {
            item.accept()
        }
    }
}

class OfferAcceptedMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
    // var binding: ItemMessageTextBinding? = DataBindingUtil.bind<ItemMessageTextBinding>(itemView)
    override fun bind(item: BaseItemMessage) {
        //      binding?.item = item

    }
}

class ProverMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
    var binding: ItemMessageProverBinding? =
        DataBindingUtil.bind<ItemMessageProverBinding>(itemView)

    override fun bind(item: BaseItemMessage) {
        val prover = item as ProverItemMessage
        binding?.item = prover
        val adapter = CredentialsDetailAdapter()
        adapter.setDataList(prover.detailList)
        binding?.attachList?.isNestedScrollingEnabled = false
        binding?.attachList?.adapter = adapter
        binding?.cancelBtn?.setOnClickListener {
            prover.cancel()
        }

        binding?.yesBtn?.setOnClickListener {
            prover.accept()
        }
    }
}

class ProverAcceptedMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
    // var binding: ItemMessageTextBinding? = DataBindingUtil.bind<ItemMessageTextBinding>(itemView)
    override fun bind(item: BaseItemMessage) {
        //      binding?.item = item

    }
}



class ConnectMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
    var binding: ItemMessageConnectBinding? =
        DataBindingUtil.bind<ItemMessageConnectBinding>(itemView)

    override fun bind(item: BaseItemMessage) {
        binding?.cancelBtn?.setOnClickListener {
            item.cancel()
        }

        binding?.yesBtn?.setOnClickListener {
            item.accept()
        }
        //    binding?.model = item
    }
}

class ConnectedMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
    // var binding: ItemMessageConnectAcceptedBinding? = DataBindingUtil.bind<ItemMessageConnectAcceptedBinding>(itemView)
    override fun bind(item: BaseItemMessage) {
        /*  binding?.cancelBtn?.setOnClickListener {
              item.cancel()
          }

          binding?.yesBtn?.setOnClickListener {
              item.accept()
          }*/
        //    binding?.model = item
    }
}

