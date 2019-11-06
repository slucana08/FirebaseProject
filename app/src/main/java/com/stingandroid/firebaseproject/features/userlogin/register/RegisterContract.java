package com.stingandroid.firebaseproject.features.userlogin.register;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.stingandroid.firebaseproject.features.shared.BaseContractView;
import com.stingandroid.firebaseproject.features.shared.BasePresenter;
import com.stingandroid.firebaseproject.features.shared.Error;

public interface RegisterContract {

    interface View extends BaseContractView {

        void setUpViews();

        void verifyFields();

        void onError(int type, Error error);

        void onSuccess();
    }

    interface Presenter extends BasePresenter<RegisterContract.View> {

        void setUpViews();

        void createAccount(Activity activity, FirebaseAuth firebaseAuth, FirebaseDatabase database, String email,
                           String password, String names, String surname);

        void createProfile(FirebaseDatabase database,FirebaseAuth firebaseAuth, String UID, String names, String surname);

        void sendEmailVerification(FirebaseAuth firebaseAuth);

        void setLoginShowing();
    }
}
