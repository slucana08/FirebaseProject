package com.stingandroid.firebaseproject;

import android.app.Application;

import com.stingandroid.firebaseproject.injection.AppComponent;
import com.stingandroid.firebaseproject.injection.DaggerAppComponent;
import com.stingandroid.firebaseproject.injection.modules.AppModule;

import timber.log.Timber;

public class App extends Application {

    public static App instance;

    public static App get() {
        return (App) instance;
    }

    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        buildDependencyInjection();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static void buildDependencyInjection() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(instance))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
