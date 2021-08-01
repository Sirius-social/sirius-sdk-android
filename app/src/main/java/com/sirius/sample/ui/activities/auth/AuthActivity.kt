package com.sirius.sample.ui.activities.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.ui.BaseActivity
import com.sirius.sample.databinding.ActivityAuthBinding
import com.sirius.sample.ui.auth.auth_first.AuthFirstFragment
import com.sirius.sample.ui.auth.auth_zero.AuthZeroFragment


class AuthActivity : BaseActivity<ActivityAuthBinding, AuthActivityModel>() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent = Intent(context, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun isBottomNavigationEnabled(): Boolean {
        return false
    }
    override fun getLayoutRes(): Int {
        return R.layout.activity_auth
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun getRootFragmentId(): Int {
        return R.id.mainFrameAuth
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showPage(AuthZeroFragment())
    }

    override fun subscribe() {
        super.subscribe()

    }


}