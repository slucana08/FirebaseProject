package com.stingandroid.firebaseproject.data.source;

import com.stingandroid.firebaseproject.data.source.remote.WebService;
import com.stingandroid.firebaseproject.injection.annotations.ApplicationScope;

import javax.inject.Inject;

@ApplicationScope
public class DataSourceRemote {

    private WebService webService;

    @Inject
    public DataSourceRemote(WebService webService){
        this.webService = webService;
    }

}
