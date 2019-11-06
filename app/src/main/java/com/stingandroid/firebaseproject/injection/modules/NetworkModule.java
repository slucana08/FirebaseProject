package com.stingandroid.firebaseproject.injection.modules;

import android.content.Context;

import com.stingandroid.firebaseproject.data.PreferenceManager;
import com.stingandroid.firebaseproject.injection.annotations.ApplicationScope;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

@Module
public class NetworkModule {

    @Provides
    @ApplicationScope
    public HttpLoggingInterceptor httpLoggingInterceptor() {

        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(message -> Timber.i(message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Provides
    @ApplicationScope
    public Cache cache(File cacheFile) {
        return new Cache(cacheFile, 10 * 1000 * 1000);
    }

    @Provides
    @ApplicationScope
    public File cacheFile(Context context) {
        return new File(context.getCacheDir(), "okhttp_cache");
    }

    @Provides
    @ApplicationScope
    public OkHttpClient okHttpClient(HttpLoggingInterceptor httpLoggingInterceptor, Cache cache,
                                     PreferenceManager preferenceManager) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .readTimeout(preferenceManager.getTimeOut(), TimeUnit.MILLISECONDS)
                .connectTimeout(preferenceManager.getTimeOut(), TimeUnit.MILLISECONDS)
                .cache(cache)
                .build();
    }
}
