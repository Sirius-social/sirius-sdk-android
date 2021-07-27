package com.sirius.sample.ui.credentials

import androidx.lifecycle.Observer
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.ui.BaseFragment

import com.sirius.sample.databinding.FragmentCredentialsBinding
import com.sirius.sample.models.ui.ItemCredentials


class CredentialsFragment : BaseFragment<FragmentCredentialsBinding, CredentialsViewModel>() {

    val adapter  = CredentialsListAdapter()


    override fun setupViews() {
        super.setupViews()
        dataBinding.credentialsRecyclerView.adapter = adapter
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_credentials
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun subscribe() {
        model.adapterListLiveData.observe(this, Observer {
            updateAdapter(it)
        })
    }

    private fun updateAdapter(data: List<ItemCredentials>) {
        adapter.setDataList(data)
        adapter.notifyDataSetChanged()
    }


    override fun setModel() {
        dataBinding.viewModel = model
    }


}