package com.sirius.sample.ui.menu

import androidx.lifecycle.Observer
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.ui.BaseFragment

import com.sirius.sample.databinding.FragmentMenuBinding
import com.sirius.sample.models.ui.ItemActions
import com.sirius.sample.models.ui.ItemCredentials
import com.sirius.sample.transform.EventTransform
import com.sirius.sample.ui.chats.ChatsFragment
import com.sirius.sample.ui.credentials.CredentialsListAdapter
import com.sirius.sample.ui.qrcode.ShowQrFragment


class MenuFragment : BaseFragment<FragmentMenuBinding, MenuViewModel>() {


    val adapter  = ActionsAdapter()



    override fun setupViews() {
        super.setupViews()
        dataBinding.actionsRecycler.adapter = adapter
        adapter.setOnAdapterItemClick {
           val event =  EventTransform.itemActionToEvent(it,model.eventRepository )
           val itemContact =   EventTransform.eventToItemContacts(event)
           baseActivity.pushPage(ChatsFragment.newInstance(itemContact))
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_menu
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun subscribe() {
        model.goToShowQrScreenLiveData.observe(this, Observer {
            if (it) {
                model.goToShowQrScreenLiveData.value = false
                baseActivity.pushPage(ShowQrFragment())
            }
        })

        model.eventLiveData.observe(this, Observer {
            model.updateList()
        })

        model.adapterListLiveData.observe(this, Observer {
            updateAdapter(it)
        })
    }

    private fun updateAdapter(data: List<ItemActions>) {
        adapter.setDataList(data)
        adapter.notifyDataSetChanged()
    }


    override fun setModel() {
        dataBinding.viewModel = model
    }


}