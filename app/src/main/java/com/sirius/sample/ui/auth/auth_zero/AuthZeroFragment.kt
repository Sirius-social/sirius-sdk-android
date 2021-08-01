package com.sirius.sample.ui.auth.auth_zero

import android.text.Editable
import android.text.TextWatcher

import androidx.lifecycle.Observer
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.ui.BaseFragment
import com.sirius.sample.databinding.FragmentAuthZeroBinding
import com.sirius.sample.ui.activities.loader.LoaderActivity
import com.sirius.sample.ui.auth.auth_second.AuthSecondFragment


class AuthZeroFragment : BaseFragment<FragmentAuthZeroBinding, AuthZeroViewModel>() {



    override fun setupViews() {
        super.setupViews()

        dataBinding.indicatorView.selectPage(1)
        dataBinding.lastnameEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                model.setUserPass(s.toString())
                model.isNextEnabled()
            }

        })

        dataBinding.nameEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                model.setUserName(s.toString())
                model.isNextEnabled()
            }

        })
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_auth_zero
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun subscribe() {
        model.goToNextScreenLiveData.observe(this, Observer {
            if (it) {
                model.goToNextScreenLiveData.value = false
                model.saveUser()
                LoaderActivity.newInstance(baseActivity)
            }
        })
    }

    override fun setModel() {
        dataBinding.viewModel = model
    }



}