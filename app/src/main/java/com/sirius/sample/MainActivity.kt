package com.sirius.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sirius.sample.fragments.ErrorFragment
import com.sirius.sample.fragments.MenuFragment
import com.sirius.sample.fragments.PairwisesFragment
import com.sirius.sample.fragments.ValidatingFragment
import com.sirius.sample.service.WebSocketService
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.helpers.ChanelHelper
import com.sirius.sdk_android.utils.HashUtils
import com.sirius.sdk_android.helpers.ScenarioHelper
import com.sirius.sdk_android.scenario.impl.InviteeScenario
import com.sirius.sdk_android.scenario.impl.InviterScenario
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSdk()
        openWallet()
        startSocketService()
        initScenario()
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame, MenuFragment()).commit()
    }

    private fun initScenario() {

        ScenarioHelper.getInstance().addScenario("Inviter", object : InviterScenario() {
            override fun onScenarioStart() {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, ValidatingFragment()).commit()
            }

            override fun onScenarioEnd(success: Boolean, error: String?) {
                if (success) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, PairwisesFragment()).commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, ErrorFragment.newInstance(error)).commit()
                }
            }

        })

        ScenarioHelper.getInstance().addScenario("Invitee", object : InviteeScenario() {
            override fun onScenarioEnd(success: Boolean, error: String?) {
                if (success) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, PairwisesFragment()).commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, ErrorFragment.newInstance(error)).commit()
                }
            }

            override fun onScenarioStart() {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, ValidatingFragment()).commit()
            }

        })

    }

    private fun startSocketService() {
        val intent = Intent(this, WebSocketService::class.java)
        startService(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        closeWallet()
    }


    fun closeWallet() {
        SiriusSDK.getInstance().walletHelper.closeWallet()
    }

    fun openWallet() {
        //  SiriusSDK.getInstance().walletUseCase.ensureWalletOpen("123", "123")
    }

    fun initSdk() {
        val userJid = "igor";
        val pass = "1234";
        val mainDirPath = App.getContext().filesDir.absolutePath
        val walletDirPath = mainDirPath + File.separator + "wallet"
        val alias = HashUtils.generateHash(userJid)
        val passForWallet = HashUtils.generateHashWithoutStoredSalt(pass, alias)
        val projDir = File(walletDirPath)
        if (!projDir.exists()) {
            projDir.mkdirs()
        }
        val walletId = alias.substring(IntRange(0, 8))

        SiriusSDK.getInstance().initialize(
            this, "https://socialsirius.com/endpoint/48fa9281-d6b1-4b17-901d-7db9e64b70b1/",
            "https://socialsirius.com", walletId, passForWallet, mainDirPath, "Sirius Sample SDK"
        )
        ChanelHelper.getInstance().initListener()
    }
}