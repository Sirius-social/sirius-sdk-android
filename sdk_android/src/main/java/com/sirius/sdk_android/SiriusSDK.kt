package com.sirius.sdk_android


import com.sirius.sdk_android.hub.ContextMobile
import com.sirius.sdk_android.walletUseCase.ChanelUseCase
import com.sirius.sdk_android.walletUseCase.InvitationUseCase
import com.sirius.sdk_android.walletUseCase.WalletUseCase


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



    private lateinit var context: ContextMobile

    fun createContext(indyEndpoint: String, serverUri: String) {
        context =
            ContextMobile.builderMobile().setEndpoint(indyEndpoint).setServerUri(serverUri).build()
    }

    fun initialize(indyEndpoint: String, myHost: String, mainDirPath: String) {
        createContext(indyEndpoint, myHost)
        walletUseCase.context = context
        walletUseCase.setDirsPath(mainDirPath)
        invitationUseCase.context = context
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