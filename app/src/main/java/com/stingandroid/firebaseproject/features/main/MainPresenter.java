package com.stingandroid.firebaseproject.features.main;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stingandroid.firebaseproject.data.PreferenceManager;

import javax.inject.Inject;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;
    private PreferenceManager preferenceManager;

    @Inject
    public MainPresenter(PreferenceManager preferenceManager){
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void isLogged(FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) getView().showLogin();
        else if (!firebaseAuth.getCurrentUser().isEmailVerified()) {
            firebaseAuth.signOut();
            getView().showLogin();
        }
    }

    @Override
    public boolean isLoginShowingRestart() {
        return preferenceManager.isLoginShowingRestart();
    }

    @Override
    public void attachView(MainContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public MainContract.View getView() {
        return view;
    }
}
