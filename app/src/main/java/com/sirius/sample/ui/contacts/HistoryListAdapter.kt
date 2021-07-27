package com.sirius.sample.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sirius.sample.R
import com.sirius.sample.base.ui.SimpleBaseRecyclerViewAdapter
import com.sirius.sample.databinding.ViewItemsCredentialsHistoryBinding
import com.sirius.sample.models.ui.ItemCredentials


class HistoryListAdapter() :
    SimpleBaseRecyclerViewAdapter<ItemCredentials, HistoryListAdapter.CredentialsHistoryViewHolder>() {


    override fun onBind(holder: CredentialsHistoryViewHolder?, position: Int) {
        val item = getItem(position)
        holder?.bind(item)
    }


    class CredentialsHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ViewItemsCredentialsHistoryBinding? = DataBindingUtil.bind<ViewItemsCredentialsHistoryBinding>(itemView)
        fun bind(item: ItemCredentials) {
            binding?.model = item
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.view_items_credentials_history
    }

    override fun getViewHolder(
        parent: ViewGroup?,
        layoutRes: Int,
        viewType: Int
    ): CredentialsHistoryViewHolder {
        val view =
            LayoutInflater.from(parent?.context).inflate(getLayoutRes(), parent, false)
        return CredentialsHistoryViewHolder(view)
    }


}