package com.sirius.sample.ui.activities.loader


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.AppPref
import com.sirius.sample.base.ui.BaseActivity
import com.sirius.sample.databinding.ActivityLoaderBinding
import com.sirius.sample.databinding.ActivitySplashBinding
import com.sirius.sample.ui.activities.auth.AuthActivity
import com.sirius.sample.ui.activities.main.MainActivity


class LoaderActivity : BaseActivity<ActivityLoaderBinding, LoaderActivityModel>() {


    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent = Intent(context, LoaderActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }


    override fun getLayoutRes(): Int {
        return R.layout.activity_loader
    }

    override fun isBottomNavigationEnabled(): Boolean {
        return false
    }
    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }


    override fun subscribe() {
        super.subscribe()
        model.initEndLiveData.observe(this, Observer {
            MainActivity.newInstance(this)
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.initSdk(this)
    }


}