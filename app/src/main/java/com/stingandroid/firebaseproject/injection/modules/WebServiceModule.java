package com.stingandroid.firebaseproject.injection.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stingandroid.firebaseproject.data.PreferenceManager;
import com.stingandroid.firebaseproject.data.source.remote.WebService;
import com.stingandroid.firebaseproject.injection.annotations.ApplicationScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module (includes = NetworkModule.class)
public class WebServiceModule {

    @Provides
    @ApplicationScope
    public WebService webServices(Retrofit retrofit) {
        return retrofit.create(WebService.class);
    }

    @Provides
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    @ApplicationScope
    public Retrofit retrofit(Gson gson, OkHttpClient okHttpClient, PreferenceManager preferenceManager) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(preferenceManager.getBaseURL())
                .build();
    }
}
