package com.sirius.sample.ui.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goterl.lazycode.lazysodium.interfaces.Base
import com.sirius.sample.R
import com.sirius.sample.base.ui.BaseMultiRecyclerViewAdapter
import com.sirius.sample.databinding.ItemMessageConnectBinding
import com.sirius.sample.databinding.ViewItemsContactsBinding
import com.sirius.sample.models.ui.ItemContacts
import com.sirius.sample.models.ui.message.BaseItemMessage
import com.sirius.sample.models.ui.message.ConnectItemMessage
import com.sirius.sample.models.ui.message.TextItemMessage


class MessagesListAdapter :
    BaseMultiRecyclerViewAdapter<BaseItemMessage>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //BaseItemMessage.getLayoutFromType(BaseItemMessage.MessageType.valueOf(viewType))
        val view =
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return MessageViewHolder.getHolderFromType(viewType,view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as? MessageViewHolder)?. bind(item)
    }


    override fun getItemViewType(position: Int): Int {
        return getItem(position).getLayoutRes()
    }


    class TextMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
      //  var binding: ViewItemsContactsBinding? = DataBindingUtil.bind<ViewItemsContactsBinding>(itemView)
        override fun bind(item: BaseItemMessage) {
         //   binding?.model = item
        }
    }

    class ConnectMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        var binding: ItemMessageConnectBinding? = DataBindingUtil.bind<ItemMessageConnectBinding>(itemView)
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

    open class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         open fun bind(item: BaseItemMessage) {}

        companion object {
            fun getHolderFromType(type : Int,itemView: View) :  MessageViewHolder{
                if(type == TextItemMessage().getLayoutRes()){
                    return TextMessageViewHolder(itemView)
                }
                if(type == ConnectItemMessage().getLayoutRes()){
                    return ConnectMessageViewHolder(itemView)
                }
                return MessageViewHolder(itemView)
            }
        }

    }


}