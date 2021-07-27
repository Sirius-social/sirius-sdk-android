package com.sirius.sample.ui.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sirius.sample.R
import com.sirius.sample.base.ui.BaseMultiRecyclerViewAdapter
import com.sirius.sample.databinding.ViewItemsContactsBinding
import com.sirius.sample.models.ui.ItemContacts


class MessagesListAdapter :
    BaseMultiRecyclerViewAdapter<ItemContacts>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate( R.layout.view_items_contacts, parent, false)
        return ContactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as? ContactsViewHolder)?. bind(item)
    }



    class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ViewItemsContactsBinding? = DataBindingUtil.bind<ViewItemsContactsBinding>(itemView)
        fun bind(item: ItemContacts) {
            binding?.model = item
        }
    }


}