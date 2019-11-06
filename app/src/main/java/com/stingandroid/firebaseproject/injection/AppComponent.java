package com.stingandroid.firebaseproject.injection;

import android.content.Context;

import com.stingandroid.firebaseproject.data.PreferenceManager;
import com.stingandroid.firebaseproject.data.source.DataSourceRepository;
import com.stingandroid.firebaseproject.data.source.remote.WebService;
import com.stingandroid.firebaseproject.injection.annotations.ApplicationScope;
import com.stingandroid.firebaseproject.injection.modules.AppModule;
import com.stingandroid.firebaseproject.injection.modules.WebServiceModule;

import dagger.Component;

@ApplicationScope
@Component(modules = {AppModule.class, WebServiceModule.class})
public interface AppComponent {

    Context context();

    PreferenceManager preferenceManager();

    DataSourceRepository dataSourceRepository();

    WebService webService();

}
