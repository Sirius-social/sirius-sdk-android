package com.sirius.sampleOld

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sirius.sampleOld.fragments.ErrorFragment
import com.sirius.sampleOld.fragments.MenuFragment
import com.sirius.sampleOld.fragments.PairwisesFragment
import com.sirius.sampleOld.fragments.ValidatingFragment
import com.sirius.sampleOld.repository.HolderRepository
import com.sirius.sampleOld.service.WebSocketService
import com.sirius.sampleOld.utils.Utils
import com.sirius.sdk.agent.BaseSender
import com.sirius.sdk.agent.MobileAgent
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.helpers.ChanelHelper
import com.sirius.sdk_android.helpers.ScenarioHelper
import com.sirius.sdk_android.scenario.impl.HolderWrapperScenario
import com.sirius.sdk_android.scenario.impl.InviteeScenario
import com.sirius.sdk_android.scenario.impl.InviterScenario
import com.sirius.sdk_android.utils.HashUtils
import com.sirius.sample.R
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
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
        ScenarioHelper.getInstance().addScenario("Holder", object : HolderWrapperScenario(){

            override fun holderEventStore(id: String, event: Event) {
                    HolderRepository.getInstance().storeHolder(id, event)
                    HolderRepository.getInstance().holderStoreLiveData.postValue(id)
            }

            override fun holderEventRemove(id: String) {
                HolderRepository.getInstance().removeHolder(id)
            }

            override fun getHolderEvent(id: String): Event {
                return  HolderRepository.getInstance().getHolder(id)
            }

        })
    }

    private fun startSocketService() {
        val intent = Intent(this, WebSocketService::class.java)
        startService(intent)
    }

    private fun connectToSocket(url : String,agent:MobileAgent) {
        val intent = Intent(this, WebSocketService::class.java)
        intent.setAction(WebSocketService.EXTRA_CONNECT)
        WebSocketService.agent = agent;
        intent.putExtra("url",url)
        startService(intent)
    }


    private fun closeSocket(){
        val intent = Intent(this, WebSocketService::class.java)
        intent.setAction(WebSocketService.EXTRA_CLOSE)
        startService(intent)
    }

    private fun sendMessToSocket(endpoint: String, data: ByteArray,agent:MobileAgent){
        val intent = Intent(this, WebSocketService::class.java)
        WebSocketService.agent = agent;
        intent.setAction(WebSocketService.EXTRA_SEND)
        intent.putExtra("data",data)
        intent.putExtra("url",endpoint)
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

        val poolDirPath = mainDirPath + File.separator + "pool"
        Utils.copyRawFile(this, R.raw.pool_transactions_genesis,poolDirPath)
   //     val path = "android.resource://" + getPackageName().toString() + "/" + R.raw.pool_transactions_genesis
       val mediatorAddress =  "ws://mediator.socialsirius.com:8000/ws"

       val sender =  object : BaseSender(){
            override fun sendTo(endpoint: String, data: ByteArray, agent:MobileAgent): Boolean {
                if(endpoint.startsWith("http")){
                    Thread(Runnable {
                        //content-type
                        val ssiAgentWire: MediaType = "application/ssi-agent-wire".toMediaType()
                        var client: OkHttpClient = OkHttpClient()
                        Log.d("mylog200","requset="+String(data))
                        val body: RequestBody = RequestBody.create(ssiAgentWire, data)
                        val request: Request = Request.Builder()
                            .url(endpoint)
                            .post(body)
                            .build()
                        client.newCall(request).execute().use { response ->
                            Log.d("mylog200","response="+response.body?.string())
                            response.isSuccessful }
                    }).start()
                }else if(endpoint.startsWith("ws")){
                    sendMessToSocket(endpoint,data,agent)
                }

                return false
            }

            override fun open(endpoint: String,agent:MobileAgent) {
                connectToSocket(endpoint,agent)
            }

            override fun close() {
                closeSocket()
            }

            override fun create() {
                //TODO create socket
            }

        }

       /* SiriusSDK.getInstance().initialize(
            this, "https://socialsirius.com/endpoint/48fa9281-d6b1-4b17-901d-7db9e64b70b1/",
            "https://socialsirius.com", walletId, passForWallet, mainDirPath, "Sirius Sample SDK"
        )*/
        Thread(Runnable {
            SiriusSDK.getInstance().initialize(
                mycontext = this, alias = walletId, pass = passForWallet, mainDirPath =  mainDirPath,
                genesisPath = poolDirPath, networkName = "Sirius Sample", mediatorAddress = mediatorAddress,label = "Sirius Sample SDK",baseSender = sender
            )
            ChanelHelper.getInstance().initListener()
        }).start()


    }
}