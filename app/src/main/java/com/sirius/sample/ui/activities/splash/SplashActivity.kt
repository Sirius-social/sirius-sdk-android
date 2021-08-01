package com.sirius.sample.ui.activities.splash


import android.os.Bundle
import android.os.Handler
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.AppPref
import com.sirius.sample.base.ui.BaseActivity
import com.sirius.sample.databinding.ActivitySplashBinding
import com.sirius.sample.ui.activities.auth.AuthActivity
import com.sirius.sample.ui.activities.loader.LoaderActivity
import com.sirius.sample.ui.activities.main.MainActivity


class SplashActivity : BaseActivity<ActivitySplashBinding, SplashActivityModel>() {
    override fun getLayoutRes(): Int {
        return R.layout.activity_splash
    }

    override fun isBottomNavigationEnabled(): Boolean {
        return false
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //MainActivity.newInstance(this)
        if (AppPref.getInstance().isLoggedIn()) {
            LoaderActivity.newInstance(this)
        } else {
            AuthActivity.newInstance(this)
        }
        finish()
    }

    override fun onStart() {
        super.onStart()

    }
}