package com.sirius.sample.ui.qrcode

import androidx.lifecycle.Observer
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.ui.BaseFragment

import com.sirius.sample.databinding.FragmentShowQrBinding


class ShowQrFragment : BaseFragment<FragmentShowQrBinding, ShowQrViewModel>() {

    override fun setupViews() {
        super.setupViews()
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_show_qr
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun subscribe() {

        model.goToScanQrScreenLiveData.observe(this, Observer {
            if(it){
                model.goToScanQrScreenLiveData.value = false
                baseActivity.pushPage(ScanQrFragment())
            }
        })
    }

    override fun setModel() {
        dataBinding.viewModel = model
    }


}