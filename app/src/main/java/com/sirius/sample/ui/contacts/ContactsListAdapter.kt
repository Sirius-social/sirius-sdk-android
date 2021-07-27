package com.sirius.sample.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sirius.sample.R
import com.sirius.sample.base.ui.SimpleBaseRecyclerViewAdapter
import com.sirius.sample.databinding.ViewItemsContactsBinding
import com.sirius.sample.models.ui.ItemContacts


class ContactsListAdapter(private val chatsClickAction:  (ItemContacts) -> Unit,
                          private val detailsClickAction: (ItemContacts) -> Unit ) :
    SimpleBaseRecyclerViewAdapter<ItemContacts, ContactsListAdapter.ContactsViewHolder>() {


    override fun onBind(holder: ContactsViewHolder?, position: Int) {
        val item = getItem(position)
        holder?.bind(item,chatsClickAction,detailsClickAction)
    }


    class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ViewItemsContactsBinding? = DataBindingUtil.bind<ViewItemsContactsBinding>(itemView)
        fun bind(item: ItemContacts,
                 chatsClickAction: (ItemContacts) -> Unit,
                 detailsClickAction: (ItemContacts) -> Unit) {

            binding?.model = item
            binding?.detailsBtn?.setOnClickListener {
                detailsClickAction .invoke(item)
            }
            binding?.chatBtn?.setOnClickListener {
                chatsClickAction.invoke(item)
            }
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.view_items_contacts
    }

    override fun getViewHolder(
        parent: ViewGroup?,
        layoutRes: Int,
        viewType: Int
    ): ContactsViewHolder {
        val view =
            LayoutInflater.from(parent?.context).inflate(getLayoutRes(), parent, false)
        return ContactsViewHolder(view)
    }


}