package com.sirius.sample.ui.chats

import android.os.Bundle

import androidx.lifecycle.Observer
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.ui.BaseFragment
import com.sirius.sample.databinding.*
import com.sirius.sample.models.ui.ItemContacts


class ChatsFragment : BaseFragment<FragmentChatsBinding, ChatsViewModel>() {


    companion object {
        fun newInstance(item: ItemContacts): ChatsFragment {
            val args = Bundle()
            args.putSerializable("item",item)
            val fragment = ChatsFragment()
            fragment.arguments = args
            return fragment
        }
    }


   val adapter  = MessagesListAdapter()

    override fun setupViews() {
        super.setupViews()

        /*   historyAdapter!!.setOnAdapterItemClick {
               it?.let {
                   model.onItemClick(it)
               }
           }*/
        dataBinding.messagesRecyclerView.adapter = adapter
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_chats
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun subscribe() {
        model.adapterListLiveData.observe(this, Observer {
            updateAdapter(it)
        })
    }

    private fun updateAdapter(data: List<ItemContacts>) {
        adapter.setDataList(data)
        adapter.notifyDataSetChanged()
    }



    override fun setModel() {
        dataBinding.viewModel = model
    }


}