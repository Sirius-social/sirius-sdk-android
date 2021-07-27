package com.sirius.sample.ui.menu

import androidx.lifecycle.Observer
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.ui.BaseFragment

import com.sirius.sample.databinding.FragmentMenuBinding
import com.sirius.sample.ui.qrcode.ShowQrFragment


class MenuFragment : BaseFragment<FragmentMenuBinding, MenuViewModel>() {

    override fun setupViews() {
        super.setupViews()
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
    }

    override fun setModel() {
        dataBinding.viewModel = model
    }


}