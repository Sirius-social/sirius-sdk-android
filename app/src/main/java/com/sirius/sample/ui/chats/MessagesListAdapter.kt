package com.sirius.sample.ui.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sirius.sample.R
import com.sirius.sample.base.ui.BaseMultiRecyclerViewAdapter

import com.sirius.sample.databinding.ItemMessageConnectBinding
import com.sirius.sample.databinding.ItemMessageOfferBinding
import com.sirius.sample.databinding.ItemMessageTextBinding
import com.sirius.sample.ui.chats.holder.MessageViewHolder
import com.sirius.sample.ui.chats.message.BaseItemMessage
import com.sirius.sample.ui.chats.message.OfferItemMessage
import com.sirius.sample.ui.credentials.CredentialsDetailAdapter


class MessagesListAdapter :
    BaseMultiRecyclerViewAdapter<BaseItemMessage>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutRes = MessageViewHolder.getLayoutResFromType(viewType)
        val view =
            LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return MessageViewHolder.getHolderFromType(viewType, view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as? MessageViewHolder)?.bind(item)
    }


    override fun getItemViewType(position: Int): Int {
        return getItem(position).getType().ordinal
    }

}