package com.sirius.sample.ui.auth.auth_third_identity

import androidx.lifecycle.Observer
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.ui.BaseFragment

import com.sirius.sample.databinding.FragmentAuthThirdIdentityBinding


class AuthThirdIdentityFragment : BaseFragment<FragmentAuthThirdIdentityBinding, AuthThirdIdentityViewModel>() {

    override fun setupViews() {
        super.setupViews()

        dataBinding.indicatorView.selectPage(1)

    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_auth_third_identity
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun subscribe() {


        model.goToTypeInfoScreenLiveData.observe(this, Observer {
            if (it) {
                model.goToTypeInfoScreenLiveData.value = false
                baseActivity.pushPage(AuthThirdIChooseIdFragment())
            }
        })
    }

    override fun setModel() {
        dataBinding.viewModel = model
    }


}