package com.sirius.sampleOld

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sirius.sampleOld.fragments.MenuFragment
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sample.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame, MenuFragment()).commit()
    }


    override fun onDestroy() {
        super.onDestroy()
        closeWallet()
    }


    fun closeWallet() {
        SiriusSDK.getInstance().walletHelper.closeWallet()
    }

}