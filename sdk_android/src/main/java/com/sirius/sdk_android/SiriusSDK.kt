package com.sirius.sdk_android


import android.content.Context
import com.sirius.sdk.agent.BaseSender
import com.sirius.sdk.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.sdk.hub.MobileContext
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.utils.ClassScanner
import com.sirius.sdk_android.walletUseCase.ChanelHelper
import com.sirius.sdk_android.walletUseCase.InvitationHelper
import com.sirius.sdk_android.walletUseCase.PairwiseHelper
import com.sirius.sdk_android.walletUseCase.WalletHelper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
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
    val channelHelper = ChanelHelper.getInstance();
    val invitationHelper = InvitationHelper.getInstance()
    val pairwiseHelper = PairwiseHelper.getInstance()
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
                        val body: RequestBody = RequestBody.create(ssiAgentWire, data)
                        val request: Request = Request.Builder()
                            .url(endpoint)
                            .post(body)
                            .build()
                        client.newCall(request).execute().use { response -> response.isSuccessful }
                    }).start()
                    return false
                }

                override fun open() {
                    //TODO open socket
                }

                override fun close() {
                    //TODO close socket
                }

                override fun create() {
                    //TODO create socket
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
        invitationHelper.context = context
        channelHelper.context = context
        pairwiseHelper.context = context
        channelHelper.initListener()
    }


    fun proposeTest() {

        examples.propose_credential.Main.main(arrayOf(""))
    }

    fun QrTest() {

        examples.Main.main(null)
    }
}