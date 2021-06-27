package com.sirius.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.sirius.sdk_android.SDK

class MainActivity : AppCompatActivity() {

    lateinit var text: TextView
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.generateQrBtn)
        text = findViewById(R.id.generateText)
        initSdk()
        button.setOnClickListener {
            generateQr()
          //  QrTest()
        }
    }

    fun QrTest(){
        Thread(Runnable {
            SDK.getInstance().QrTest()
        }).start()

    }

    fun purposeTest(){
        Thread(Runnable {
            SDK.getInstance().proposeTest()
        }).start()

    }

    fun generateQr(){
        val wallet = SDK.getInstance().ensureWalletOpen("123", "123")
        Log.d("mylog2090","wallet="+wallet);
        val inviteText = SDK.getInstance().generateInvitation("SampleLabale")
        text.text = inviteText
        SDK.getInstance().closeWallet(wallet)
    }

    fun initSdk() {
        val dirPath= App.getContext().filesDir.absolutePath
        SDK.getInstance().initialize("https",dirPath)
    }
}