package com.stingandroid.firebaseproject.features.userlogin.login;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.stingandroid.firebaseproject.features.shared.BaseContractView;
import com.stingandroid.firebaseproject.features.shared.BasePresenter;

public interface LoginContract {

    interface View extends BaseContractView {
        void setUpViews();

        void onError(int type);

        void onSuccess();

        void verifyFields();
    }

    interface Presenter extends BasePresenter<LoginContract.View>{
        void setUpViews();

        void processLogin(Activity activity, FirebaseAuth firebaseAuth, FirebaseDatabase database,
                          String email, String password);

        void checkIfEmailVerified(FirebaseAuth firebaseAuth);

        void resendEmailVerification(FirebaseAuth firebaseAuth);

        void setLoginShowing();
    }

}
