package com.sirius.sdk_android

import android.content.Context

import com.sirius.sdk.Main
import com.sirius.sdk_android.walletUseCase.WalletUseCase


class SDK {
    val walletUseCase =   WalletUseCase.getInstance();

    fun initialize(context: Context){
        walletUseCase.setDirPath(context)
    }

    fun sampleMain(){
      //  val lazySodium = LazySodiumAndroid(SodiumAndroid())
        Thread(){
            Main.main(null)
        }.start()
    }


    fun createWallet(userJid: String?, pin: String?){
        walletUseCase.createWallet(userJid, pin)
    }


    fun ceWallet(userJid: String?, pin: String?){
        walletUseCase.createWallet(userJid, pin)
    }
}