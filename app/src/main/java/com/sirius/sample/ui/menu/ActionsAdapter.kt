package com.sirius.sample.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sirius.sample.R
import com.sirius.sample.base.ui.SimpleBaseRecyclerViewAdapter
import com.sirius.sample.databinding.ViewActionItemsBinding
import com.sirius.sample.databinding.ViewItemsCredentialsBinding
import com.sirius.sample.models.ui.ItemActions


import com.sirius.sample.models.ui.ItemCredentials


class ActionsAdapter :
    SimpleBaseRecyclerViewAdapter<ItemActions, ActionsAdapter.ActionsViewHolder>() {


    override fun onBind(holder: ActionsViewHolder?, position: Int) {
        val item = getItem(position)
        holder?.bind(item)
        holder?.itemView?.setOnClickListener {
            onAdapterItemClick?.onItemClick(item)
        }
    }


    class ActionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ViewActionItemsBinding? = DataBindingUtil.bind<ViewActionItemsBinding>(itemView)
        fun bind(item: ItemActions) {
            binding?.model = item
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.view_action_items
    }

    override fun getViewHolder(
        parent: ViewGroup?,
        layoutRes: Int,
        viewType: Int
    ): ActionsViewHolder {
        val view =
            LayoutInflater.from(parent?.context).inflate(getLayoutRes(), parent, false)
        return ActionsViewHolder(view)
    }


}