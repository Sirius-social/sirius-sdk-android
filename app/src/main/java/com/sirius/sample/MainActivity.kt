package com.sirius.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            //generateQr()
            purposeTest()
        }
    }

    fun purposeTest(){
        SDK.getInstance().proposeTest()
    }

    fun generateQr(){
        val wallet = SDK.getInstance().ensureWalletOpen("123", "123")
        val inviteText = SDK.getInstance().generateInvitation("SampleLabale", "https")
        text.text = inviteText
        SDK.getInstance().closeWallet(wallet)
    }

    fun initSdk() {
        SDK.getInstance().initialize(this)

    }
}