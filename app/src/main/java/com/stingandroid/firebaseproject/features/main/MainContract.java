package com.stingandroid.firebaseproject.features.main;

import com.google.firebase.auth.FirebaseAuth;
import com.stingandroid.firebaseproject.features.shared.BaseContractView;
import com.stingandroid.firebaseproject.features.shared.BasePresenter;

public interface MainContract {

    interface View extends BaseContractView{
        void showLogin();

        void setUpNavigation();
    }

    interface Presenter extends BasePresenter<MainContract.View>{
        void isLogged(FirebaseAuth firebaseAuth);

        boolean isLoginShowingRestart();
    }
}
