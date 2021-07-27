package com.sirius.sample.ui.activities.splash


import android.os.Bundle
import android.os.Handler
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.ui.BaseActivity
import com.sirius.sample.databinding.ActivitySplashBinding
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
        Handler().postDelayed({
            MainActivity.newInstance(this)
           // MainActivity.newInstance(this)
          /*  if(AppPref.getInstance().isLoggedIn()){
                MainActivity.newInstance(this)
            }else{
                AuthActivity.newInstance(this)
            }*/
            finish()

        },2000)


    }

    override fun onStart() {
        super.onStart()

    }
}