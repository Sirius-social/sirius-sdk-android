package com.sirius.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sirius.sample.fragments.MenuFragment
import com.sirius.sample.fragments.PairwisesFragment
import com.sirius.sample.fragments.ValidatingFragment
import com.sirius.sample.service.WebSocketService
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnRequest
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.agent.pairwise.Pairwise
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.utils.HashUtils
import com.sirius.sdk_android.walletUseCase.ChanelHelper
import com.sirius.sdk_android.walletUseCase.InvitationHelper
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSdk()
        openWallet()
        startSocketService()
        showLoading()
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame, MenuFragment()).commit()
    }

    private fun showLoading(){
        SiriusSDK.getInstance().invitationHelper.invitationListener = object : InvitationHelper.InvitationListener{
            override fun onInvitationStart() {
                supportFragmentManager.beginTransaction().replace(R.id.mainFrame, ValidatingFragment()).commit()
            }

            override fun onInvitationEnd(pairwise: Pairwise?) {
                if(pairwise == null){
                    supportFragmentManager.beginTransaction().replace(R.id.mainFrame, MenuFragment()).commit()
                }else{
                    supportFragmentManager.beginTransaction().replace(R.id.mainFrame, PairwisesFragment()).commit()
                }
            }

        }
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
        SiriusSDK.getInstance().walletHelper.closeWallet()
    }

    fun openWallet(){
       //  SiriusSDK.getInstance().walletUseCase.ensureWalletOpen("123", "123")
    }
    fun initSdk() {
        val userJid = "igor";
        val pass = "1234";
        val mainDirPath= App.getContext().filesDir.absolutePath
        val walletDirPath = mainDirPath + File.separator + "wallet"
        val alias = HashUtils.generateHash(userJid)
        val passForWallet = HashUtils.generateHashWithoutStoredSalt(pass, alias)
        val projDir = File(walletDirPath)
        if (!projDir.exists()) {
            projDir.mkdirs()
        }
        val walletId = alias.substring(IntRange(0, 8))

        SiriusSDK.getInstance().
        initialize(this, "https://socialsirius.com/endpoint/48fa9281-d6b1-4b17-901d-7db9e64b70b1/",
            "https://socialsirius.com",walletId,passForWallet,mainDirPath,"Sirius Sample SDK")
    }
}