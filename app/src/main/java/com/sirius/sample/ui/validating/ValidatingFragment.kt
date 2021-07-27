package com.sirius.sample.ui.validating

import androidx.lifecycle.Observer
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.ui.BaseFragment

import com.sirius.sample.databinding.FragmentValidatingBinding
import com.sirius.sample.ui.qrcode.ShowQrFragment


class ValidatingFragment : BaseFragment<FragmentValidatingBinding, ValidatingViewModel>() {

    override fun setupViews() {
        super.setupViews()
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_validating
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