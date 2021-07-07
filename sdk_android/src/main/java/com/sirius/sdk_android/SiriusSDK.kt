package com.sirius.sdk_android


import com.sirius.sdk.hub.Context
import com.sirius.sdk.hub.MobileContext
import com.sirius.sdk.hub.MobileHub
import com.sirius.sdk_android.walletUseCase.ChanelUseCase
import com.sirius.sdk_android.walletUseCase.InvitationUseCase
import com.sirius.sdk_android.walletUseCase.WalletUseCase
import shadow.org.json.JSONObject


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


    val walletUseCase = WalletUseCase.getInstance();
    val chanelUseCase = ChanelUseCase.getInstance();
    val invitationUseCase = InvitationUseCase.getInstance()



    private lateinit var context: MobileContext

    fun createContext(indyEndpoint: String, serverUri: String,config : String, credential: String ) {
        context =  MobileContext.builder().
        setIndyEndpoint(indyEndpoint).setServerUri(serverUri).
        setWalletConfig(JSONObject(config)).
        setWalletCredentials(JSONObject(credential)).build() as MobileContext
    }

    fun initialize(indyEndpoint: String, myHost: String, alias : String, pass :  String, mainDirPath: String ) {
        val config  =  WalletUseCase.getInstance().createWalletConfig(alias,mainDirPath)
        val credential = WalletUseCase.getInstance().createWalletCredential(pass)
        createContext(indyEndpoint, myHost,config,credential)
        walletUseCase.context = context
        walletUseCase.setDirsPath(mainDirPath)
        invitationUseCase.context =  context
        chanelUseCase.context = context
        chanelUseCase.initListener()
    }


    fun proposeTest() {

        examples.propose_credential.Main.main(arrayOf(""))
    }

    fun QrTest() {

        examples.Main.main(null)
    }
}