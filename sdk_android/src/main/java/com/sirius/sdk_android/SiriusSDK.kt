package com.sirius.sdk_android


import android.content.Context
import android.util.Log
import com.sirius.sdk.agent.BaseSender
import com.sirius.sdk.agent.MobileAgent
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.sdk.agent.pairwise.TheirEndpoint
import com.sirius.sdk.hub.MobileContext
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.helpers.WalletHelper
import com.sirius.sdk_android.utils.ClassScanner
import examples.connect_to_mediator.Main
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import shadow.org.json.JSONObject
import java.lang.reflect.Modifier


class SiriusSDK {

    companion object {
        private var instanceSDK: SiriusSDK? = null

        @JvmStatic
        fun getInstance(): SiriusSDK {
            if (instanceSDK == null) {
                instanceSDK = SiriusSDK()
            }
            return instanceSDK!!
        }
    }


    val walletHelper = WalletHelper.getInstance();
    var label : String? =null

    lateinit var context: MobileContext


    private fun createContext(indyEndpoint: String, serverUri: String, config: String, credential: String) {

        context = MobileContext.builder().setIndyEndpoint(indyEndpoint).setServerUri(serverUri)
            .setWalletConfig(JSONObject(config)).
            setWalletCredentials(JSONObject(credential))
            .setMediatorInvitation(Invitation.builder().setLabel(label).build())
            .setSender(object : BaseSender(){
                override fun sendTo(endpoint: String, data: ByteArray): Boolean {
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
                    return false
                }

                override fun open(endpoint: String) {
                    //TODO open socket
                }

                override fun close() {
                    //TODO close socket
                }


            })
            .build() as MobileContext
    }

    private fun initAllMessages(mycontext: Context){
        object : ClassScanner(mycontext) {
            override fun isTargetClassName(className: String): Boolean {
                return (className.startsWith("com.sirius.sdk.") //I want classes under my package
                        && !className.contains("$") //I don't need none-static inner classes
                        )
            }

            override fun isTargetClass(clazz: Class<*>): Boolean {
                return (Message::class.java.isAssignableFrom(clazz) //I want subclasses of AbsFactory
                        && !Modifier.isAbstract(clazz.modifiers) //I don't want abstract classes
                        )
            }

            override fun onScanResult(clazz: Class<*>) {
                try {
                    Class.forName(clazz.name, true, clazz.classLoader)
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
            }
        }.scan()
    }

    fun initialize(
        mycontext: Context,
        indyEndpoint: String,
        myHost: String,
        alias: String,
        pass: String,
        mainDirPath: String,
        label:String
    ) {
        this.label = label
        initAllMessages(mycontext)
        val config = WalletHelper.getInstance().createWalletConfig(alias, mainDirPath)
        val credential = WalletHelper.getInstance().createWalletCredential(pass)
        createContext(indyEndpoint, myHost, config, credential)
        walletHelper.context = context
        walletHelper.setDirsPath(mainDirPath)
    }


    fun initialize(
        mycontext: Context,
        alias: String,
        pass: String,
        mainDirPath: String,
        genesisPath:String,
        networkName : String,
        mediatorAddress : String,
        label:String, baseSender: BaseSender
    ) {
        this.label = label
        initAllMessages(mycontext)
        var config = WalletHelper.getInstance().createWalletConfig(alias, mainDirPath)
        val credential = WalletHelper.getInstance().createWalletCredential(pass)
        MobileContext.addPool(networkName, genesisPath)
        createContextWitMediator( config, credential,mediatorAddress, baseSender)
        walletHelper.context = context
        walletHelper.setDirsPath(mainDirPath)
    }



    private fun createContextWitMediator(config: String, credential: String, mediatorAddress : String , baseSender: BaseSender) {

        context = MobileContext.builder()
            .setWalletConfig(JSONObject(config)).
            setWalletCredentials(JSONObject(credential))
            .setMediatorInvitation( Invitation.builder().setLabel(label)
                .setEndpoint(mediatorAddress )
                .setRecipientKeys(listOf("DjgWN49cXQ6M6JayBkRCwFsywNhomn8gdAXHJ4bb98im")).build())
            .setSender(baseSender)
            .build() as MobileContext

    }

    fun connectToMediator(){
        context.connectToMediator()
    }

}