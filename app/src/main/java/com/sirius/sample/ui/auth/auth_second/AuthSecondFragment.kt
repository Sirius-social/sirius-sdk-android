package com.sirius.sample.ui.auth.auth_second

import android.text.Editable
import android.text.TextWatcher

import androidx.lifecycle.Observer
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.ui.BaseFragment

import com.sirius.sample.databinding.FragmentAuthSecondBinding
import com.sirius.sample.ui.auth.auth_third.AuthThirdFragment


class AuthSecondFragment : BaseFragment<FragmentAuthSecondBinding, AuthSecondViewModel>() {


    override fun setupViews() {
        super.setupViews()
        dataBinding.indicatorView.selectPage(2)
        dataBinding.phoneEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                model.setPhone(s.toString())
                model.isNextEnabled()
            }

        })

        dataBinding.emailEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                model.setUserEmail(s.toString())
                model.isNextEnabled()
            }

        })
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_auth_second
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun subscribe() {
        model.goToNextScreenLiveData.observe(this, Observer {
            if (it) {
                model.goToNextScreenLiveData.value = false
                baseActivity.pushPage(AuthThirdFragment())
            }
        })
    }

    override fun setModel() {
        dataBinding.viewModel = model
    }


}