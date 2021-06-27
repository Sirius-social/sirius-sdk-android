package com.sirius.sdk_android


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

    fun initialize(indyEndpoint : String, mainDirPath: String ) {
        walletUseCase.setDirsPath(mainDirPath)
        walletUseCase.createContext(indyEndpoint)
    }


    fun ensureWalletOpen(userJid: String, pin: String): Wallet? {
        return if (walletUseCase.isWalletExist(userJid)) {
            walletUseCase.openWallet(userJid, pin)
        } else {
            walletUseCase.createWallet(userJid, pin)
            walletUseCase.openWallet(userJid, pin)
        }
    }

    fun closeWallet(wallet: Wallet?) {
        wallet?.closeWallet()
    }

    fun openWallet(userJid: String, pin: String?): Wallet {
        return IndyWallet.openWallet(userJid, pin)
    }

    fun createWallet(userJid: String?, pin: String?) {
        walletUseCase.createWallet(userJid, pin)
    }

    fun generateInvitation(label: String): String? {
        return walletUseCase.generateQrCodeInvitation(label)
    }

    fun proposeTest() {

        examples.propose_credential.Main.main(arrayOf(""))
    }

    fun QrTest() {

        examples.Main.main(null)
    }
}