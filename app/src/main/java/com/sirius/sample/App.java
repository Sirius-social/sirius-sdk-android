package com.sirius.sample;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.StrictMode;

import androidx.multidex.MultiDexApplication;

import com.sirius.sdk_android.IndyWallet;


public class App extends MultiDexApplication {

    public static String code;
    private static App instance;

    public static long downloadId = 0;

    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return getInstance().getApplicationContext();
    }

    public static CountDownTimer countDownTimer;

    public static void startCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        countDownTimer = new CountDownTimer(AppPref.getInstance().getTimeByBlockType(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                App.code = null;
                IndyWallet.closeMyWallet();
            }
        };
        countDownTimer.start();
    }

    public static void stopCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }



    public static void initialize() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        initialize();
    }


}
