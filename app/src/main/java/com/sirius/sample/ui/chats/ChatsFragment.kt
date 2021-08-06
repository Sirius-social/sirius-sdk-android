package com.sirius.sample.ui.chats

import android.os.Bundle
import androidx.core.widget.addTextChangedListener

import androidx.lifecycle.Observer
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.ui.BaseFragment
import com.sirius.sample.databinding.*
import com.sirius.sample.models.ui.ItemContacts
import com.sirius.sample.ui.chats.message.BaseItemMessage


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
        model.item = arguments?.getSerializable("item") as? ItemContacts
        super.setupViews()
        dataBinding.messagesRecyclerView.adapter = adapter
        dataBinding.messageText.addTextChangedListener {
            model.messageText = it.toString()
            model.enableSendIcon()
        }

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
        model.enableSendIconLiveData.observe(this, Observer {
            dataBinding.sendIcon.isEnabled = it
            if(it){
                dataBinding.sendIcon.setColorFilter(App.getContext().getColor(R.color.blue))
            }else{
                dataBinding.sendIcon.setColorFilter(App.getContext().getColor(R.color.gray_text_hint))
            }
        })

        model.clearTextLiveData.observe(this, Observer {
            if(it){
                model.clearTextLiveData.value = false
                dataBinding.messageText.setText("")
            }
        })

        model.eventStoreLiveData.observe(this, Observer {
            model.updateList()
            dataBinding.messagesRecyclerView.scrollToPosition(adapter.itemCount)
        })
    }

    private fun updateAdapter(data: List<BaseItemMessage>) {
        adapter.setDataList(data)
        adapter.notifyDataSetChanged()
    }



    override fun setModel() {
        dataBinding.viewModel = model
    }


}