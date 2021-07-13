package com.sirius.sample.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.sirius.sample.R
import com.sirius.sample.base.SimpleBaseRecyclerViewAdapter
import com.sirius.sdk.agent.pairwise.Pairwise


class PairwisesListAdapter() :
    SimpleBaseRecyclerViewAdapter<Pairwise, PairwisesListAdapter.CredentialsHistoryViewHolder>() {


    override fun onBind(holder: CredentialsHistoryViewHolder?, position: Int) {
        val item = getItem(position)
        holder?.title?.text = "ME.DID = "+item.me.did
        holder?.title2?.text =  "THEIR.DID = "+item.their.did
        holder?.itemView?.setOnClickListener {
            onAdapterItemClick?.onItemClick(item)
        }
    }


    class CredentialsHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title  : TextView= itemView.findViewById(R.id.title)
        var title2  : TextView= itemView.findViewById(R.id.title2)
        /*fun bind(item: ItemCredentials) {
            binding?.model = item
        }*/
    }

    override fun getLayoutRes(): Int {
        return R.layout.view_items_pairwise
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