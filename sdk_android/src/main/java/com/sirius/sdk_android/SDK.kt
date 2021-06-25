package com.sirius.sdk_android

import android.content.Context
import com.sirius.sdk_android.walletUseCase.WalletUseCase
import org.hyperledger.indy.sdk.wallet.Wallet


class SDK {

    companion object {
        var instanceSDK: SDK? = null

        @JvmStatic
        fun getInstance(): SDK {
            if (instanceSDK == null) {
                instanceSDK = SDK()
            }
            return instanceSDK!!
        }
    }


    val walletUseCase = WalletUseCase.getInstance();

    fun initialize(context: Context) {
        walletUseCase.setDirPath(context)
        walletUseCase.createContext("")
    }




    fun ensureWalletOpen(userJid: String, pin: String): Wallet {
        return if (walletUseCase.isWalletExist(userJid)) {
            IndyWallet.openWallet(pin)
        } else {
            walletUseCase.createWallet(userJid, pin)
            IndyWallet.openWallet(pin)
        }
    }

    fun closeWallet(wallet: Wallet){
        wallet.closeWallet()
    }

    fun openWallet(pin: String?): Wallet {
        return IndyWallet.openWallet(pin)
    }

    fun createWallet(userJid: String?, pin: String?) {
        walletUseCase.createWallet(userJid, pin)
    }

    fun generateInvitation(label: String, myEndpoint : String): String? {
        return walletUseCase.generateQrCodeInvitation(label,myEndpoint)
    }

    fun proposeTest(){

        examples.propose_credential.Main.main(arrayOf(""))
    }
}