package com.sirius.sample.ui.auth.auth_third

import androidx.lifecycle.Observer
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.ui.BaseFragment

import com.sirius.sample.databinding.FragmentAuthThirdBinding
import com.sirius.sample.ui.auth.auth_fourth.AuthFourthFragment
import com.sirius.sample.ui.auth.auth_third_identity.AuthThirdIdentityFragment


class AuthThirdFragment : BaseFragment<FragmentAuthThirdBinding, AuthThirdViewModel>() {

    override fun setupViews() {
        super.setupViews()

        dataBinding.indicatorView.selectPage(3)

    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_auth_third
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun subscribe() {
        model.goToTypeInfoScreenLiveData.observe(this, Observer {
            if (it) {
                model.goToTypeInfoScreenLiveData.value = false
                baseActivity.pushPage(AuthThirdIdentityFragment())
            }
        })
        model.goToNextInfoScreenLiveData.observe(this, Observer {
            if (it) {
                model.goToNextInfoScreenLiveData.value = false
                baseActivity.pushPage(AuthFourthFragment())
            }
        })
    }

    override fun setModel() {
        dataBinding.viewModel = model
    }


}