package com.sirius.sdk_android


import android.content.Context
import android.system.Os
import android.util.Log
import com.sirius.sdk.agent.BaseSender
import com.sirius.sdk.agent.MobileContextConnection
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation

import com.sirius.sdk.hub.MobileContext
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.helpers.WalletHelper
import com.sirius.sdk_android.utils.ClassScanner
import com.sirius.sdk_android.utils.PoolMobile
import com.sirius.sdk_android.utils.PoolUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.hyperledger.indy.sdk.IndyJava
import org.hyperledger.indy.sdk.LibIndy
import org.json.JSONObject

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
    var label: String? = null

    lateinit var context: MobileContext


    private fun createContext(
        indyEndpoint: String,
        serverUri: String,
        config: String,
        credential: String
    ) {

        context = MobileContext.builder().setIndyEndpoint(indyEndpoint).setServerUri(serverUri)
            .setWalletConfig(JSONObject(config)).setWalletCredentials(JSONObject(credential))
            .setMediatorInvitation(Invitation.builder().setLabel(label).build())
            .setSender(object : BaseSender() {
                override fun sendTo(endpoint: String, data: ByteArray): Boolean {
                    Thread(Runnable {
                        //content-type
                        val ssiAgentWire: MediaType = "application/ssi-agent-wire".toMediaType()
                        var client: OkHttpClient = OkHttpClient()
                        Log.d("mylog200", "requset=" + String(data))
                        val body: RequestBody = RequestBody.create(ssiAgentWire, data)
                        val request: Request = Request.Builder()
                            .url(endpoint)
                            .post(body)
                            .build()
                        client.newCall(request).execute().use { response ->
                            Log.d("mylog200", "response=" + response.body?.string())
                            response.isSuccessful
                        }
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

    private fun initAllMessages(mycontext: Context) {
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
        label: String
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
        mediatorAddress: String,
        recipientKeys: List<String>,
        label: String, baseSender: BaseSender
    ) {
        this.label = label
        initAllMessages(mycontext)

        //   LibIndy.setRuntimeConfig("{\"collect_backtrace\": true }")
        var config = WalletHelper.getInstance().createWalletConfig(alias, mainDirPath)
        val credential = WalletHelper.getInstance().createWalletCredential(pass)
        //  Os.setenv("TMPDIR",mainDirPath,true)
//        PoolUtils.createPoolLedgerConfig(networkName, genesisPath)
        //   MobileContext.addPool(networkName, genesisPath)
        createContextWitMediator(config, credential, mediatorAddress, recipientKeys, baseSender)
        walletHelper.context = context
        walletHelper.setDirsPath(mainDirPath)
    }


    private fun createContextWitMediator(
        config: String,
        credential: String,
        mediatorAddress: String,
        recipientKeys: List<String>,
        baseSender: BaseSender
    ) {
        val mediatorLabel = "Mediator"
        context = MobileContext.builder()
            .setWalletConfig(JSONObject(config)).setWalletCredentials(JSONObject(credential))
            .setMediatorInvitation(
                Invitation.builder().setLabel(mediatorLabel)
                    .setEndpoint(mediatorAddress)
                    .setRecipientKeys(recipientKeys).build()
            )
            .setSender(baseSender)
            .build() as MobileContext

    }

    fun connectToMediator(firebaseId: String? = null) {
        val fcmConnection = MobileContextConnection("FCMService", 1, listOf(), firebaseId)
        context.connectToMediator(this.label, listOf(fcmConnection))
    }

}