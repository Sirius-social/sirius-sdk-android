package com.sirius.sample.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import com.sirius.sample.R
import com.sirius.sample.service.WebSocketService
import com.sirius.sample.utils.Utils
import com.sirius.sdk.agent.BaseSender
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.helpers.ChanelHelper
import com.sirius.sdk_android.helpers.ScenarioHelper
import com.sirius.sdk_android.scenario.impl.HolderWrapperScenario
import com.sirius.sdk_android.scenario.impl.InviteeScenario
import com.sirius.sdk_android.scenario.impl.InviterScenario
import com.sirius.sdk_android.utils.HashUtils
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SDKUseCase @Inject constructor(private val eventRepository: EventRepository){


    public fun startSocketService(context: Context) {
        val intent = Intent(context, WebSocketService::class.java)
        context.startService(intent)
    }

    private fun connectToSocket(context: Context,url : String) {
        val intent = Intent(context, WebSocketService::class.java)
        intent.setAction(WebSocketService.EXTRA_CONNECT)
        intent.putExtra("url",url)
        context.startService(intent)
    }


    private fun closeSocket(context: Context){
        val intent = Intent(context, WebSocketService::class.java)
        intent.setAction(WebSocketService.EXTRA_CLOSE)
        context.startService(intent)
    }


    private fun sendMessToSocket(context: Context,endpoint: String, data: ByteArray){
        val intent = Intent(context, WebSocketService::class.java)
        intent.setAction(WebSocketService.EXTRA_SEND)
        intent.putExtra("data",data)
        intent.putExtra("url",endpoint)
        context.startService(intent)
    }


    interface  OnInitListener{
        fun initStart()
        fun initEnd()
    }
    fun initSdk(context: Context,userJid : String, pass : String, onInitListener: OnInitListener?) {
        onInitListener?.initStart()
        val mainDirPath = context.filesDir.absolutePath
        val walletDirPath = mainDirPath + File.separator + "wallet"
        val alias = HashUtils.generateHash(userJid)
        val passForWallet = HashUtils.generateHashWithoutStoredSalt(pass, alias)
        val projDir = File(walletDirPath)
        if (!projDir.exists()) {
            projDir.mkdirs()
        }
        val walletId = alias.substring(IntRange(0, 8))

        val poolDirPath = mainDirPath + File.separator + "pool"
        Utils.copyRawFile(context, R.raw.pool_transactions_genesis,poolDirPath)
        //     val path = "android.resource://" + getPackageName().toString() + "/" + R.raw.pool_transactions_genesis
        val mediatorAddress =  "ws://mediator.socialsirius.com:8000/ws"

        val sender =  object : BaseSender(){
            override fun sendTo(endpoint: String, data: ByteArray): Boolean {
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
                    sendMessToSocket(context,endpoint,data)
                }

                return false
            }

            override fun open(endpoint: String) {
                connectToSocket(context,endpoint)
            }

            override fun close() {
                closeSocket(context)
            }


        }

        /* SiriusSDK.getInstance().initialize(
             this, "https://socialsirius.com/endpoint/48fa9281-d6b1-4b17-901d-7db9e64b70b1/",
             "https://socialsirius.com", walletId, passForWallet, mainDirPath, "Sirius Sample SDK"
         )*/
        Thread(Runnable {
            SiriusSDK.getInstance().initialize(
                mycontext = context, alias = walletId, pass = passForWallet,
                mainDirPath =  mainDirPath,
                genesisPath = poolDirPath, networkName = "Sirius Sample",
                mediatorAddress = mediatorAddress,
                label = "Sirius Sample SDK",baseSender = sender
            )
            ChanelHelper.getInstance().initListener()
            SiriusSDK.getInstance().connectToMediator()
            initScenario()
            eventRepository.reloadEventsFromWallet()
            onInitListener?.initEnd()
        }).start()
    }


    private fun initScenario() {
        ScenarioHelper.getInstance().addScenario("Inviter", object : InviterScenario() {
            override fun onScenarioStart() {
                eventRepository.invitationStartLiveData.postValue(true)
            }

            override fun onScenarioEnd(success: Boolean, error: String?) {
                eventRepository.invitationStopLiveData.postValue(Pair(success,error))
            }
        })

        ScenarioHelper.getInstance().addScenario("Invitee", object : InviteeScenario() {
            override fun onScenarioStart() {
                eventRepository.invitationStartLiveData.postValue(true)
            }
            override fun onScenarioEnd(success: Boolean, error: String?) {
                eventRepository.invitationStopLiveData.postValue(Pair(success,error))
            }

            override fun eventStore(id: String, event: Event) {
                super.eventStore(id, event)
                eventRepository.reloadEventsFromWallet()
                eventRepository.eventStoreLiveData.postValue(id)
            }

        })
        ScenarioHelper.getInstance().addScenario("Holder", object : HolderWrapperScenario(){
            override fun holderEventStore(id: String, event: Event) {
                eventRepository.storeEvent(id, event)
                eventRepository.eventStoreLiveData.postValue(id)
            }

            override fun holderEventRemove(id: String) {
                eventRepository.removeEvent(id)
            }

            override fun getHolderEvent(id: String): Event? {
                return  eventRepository.getEvent(id)
            }

        })
    }
}