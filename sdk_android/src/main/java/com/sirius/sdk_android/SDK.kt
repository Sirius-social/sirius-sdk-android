package com.sirius.sdk_android

import com.sirius.sdk.Main

class SDK {

    fun initialize(){
        Thread(Runnable {
            Main.main(null)
        }).start()
    }
}