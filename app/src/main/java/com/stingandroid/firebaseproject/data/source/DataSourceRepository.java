package com.stingandroid.firebaseproject.data.source;

import com.stingandroid.firebaseproject.injection.annotations.ApplicationScope;

import javax.inject.Inject;

@ApplicationScope
public class DataSourceRepository {

    private DataSourceRemote remote;

    @Inject
    public DataSourceRepository(DataSourceRemote remote){
        this.remote = remote;
    }
}
