package com.stingandroid.firebaseproject.injection.modules;

import android.content.Context;

import com.stingandroid.firebaseproject.injection.annotations.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Context context;

    public AppModule(Context context){
        this.context = context.getApplicationContext();
    }

    @Provides
    @ApplicationScope
    public Context provideContext(){
        return context;
    }
}
