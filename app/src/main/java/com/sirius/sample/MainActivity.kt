package com.sirius.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sirius.sample.fragments.MenuFragment
import com.sirius.sample.service.WebSocketService
import com.sirius.sdk_android.SiriusSDK

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSdk()
        openWallet()
        startSocketService()
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame, MenuFragment()).commit()
    }

    private fun startSocketService() {
        val intent = Intent(this, WebSocketService::class.java)
        startService(intent)
    }
    fun QrTest(){
        Thread(Runnable {
            SiriusSDK.getInstance().QrTest()
        }).start()

    }

    fun purposeTest(){
        Thread(Runnable {
            SiriusSDK.getInstance().proposeTest()
        }).start()

    }

    override fun onDestroy() {
        super.onDestroy()
        closeWallet()
    }


    fun closeWallet(){
        SiriusSDK.getInstance().walletUseCase.closeWallet()
    }

    fun openWallet(){
         SiriusSDK.getInstance().walletUseCase.ensureWalletOpen("123", "123")
    }
    fun initSdk() {
        val dirPath= App.getContext().filesDir.absolutePath
        SiriusSDK.getInstance().
        initialize("https://socialsirius.com/endpoint/48fa9281-d6b1-4b17-901d-7db9e64b70b1/",
            "https://socialsirius.com",dirPath)
    }
}