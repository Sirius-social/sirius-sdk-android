package com.sirius.sample.base;

import android.content.Context;
import android.os.StrictMode;

import androidx.multidex.MultiDexApplication;

import com.sirius.sample.base.dagger.AppComponent;
import com.sirius.sample.base.dagger.DaggerAppComponent;


public class App extends MultiDexApplication {

    private static App instance;
    private AppComponent appComponent;
    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return getInstance().getApplicationContext();
    }




    public static void initialize() {
        initializeDagger();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        initialize();
    }

    private static void initializeDagger() {
        getInstance().appComponent = DaggerAppComponent
            .builder()
            .withApplication(getInstance())
            .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
