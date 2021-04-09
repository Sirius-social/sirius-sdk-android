package com.sirius.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sirius.sdk_android.SDK

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SDK().sampleMain()
    }
}