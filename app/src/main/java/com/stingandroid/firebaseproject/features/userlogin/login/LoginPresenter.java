package com.stingandroid.firebaseproject.features.userlogin.login;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.stingandroid.firebaseproject.data.PreferenceManager;
import com.stingandroid.firebaseproject.data.models.User;

import javax.inject.Inject;

import timber.log.Timber;

public class LoginPresenter implements LoginContract.Presenter{

    private LoginContract.View view;
    private PreferenceManager preferenceManager;

    @Inject
    public LoginPresenter(PreferenceManager preferenceManager){
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void setUpViews() {
        getView().setUpViews();
    }

    @Override
    public void processLogin(Activity activity,FirebaseAuth firebaseAuth, FirebaseDatabase database,
                             String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(activity, task -> {
                    if (!task.isSuccessful()){
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException ||
                                task.getException() instanceof FirebaseAuthInvalidUserException)
                            getView().onError(0);
                        else
                            getView().onError(1);
                        return;
                    }
                    User user = new User();
                    user.setUID(firebaseAuth.getCurrentUser().getUid());
                    user.setEmail(email);
                    user.setPassword(password);
                    preferenceManager.setUserData(user);
                    checkIfEmailVerified(firebaseAuth);
                });
    }

    @Override
    public void checkIfEmailVerified(FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        user.reload();
        if (!user.isEmailVerified()){
            getView().onError(2);
            return;
        }
        getView().onSuccess();
    }

    @Override
    public void resendEmailVerification(FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                if (task.getException().getMessage().contains("Try again later"))
                    getView().onError(3);
                else
                    getView().onError(1);
                return;
            }
            firebaseAuth.signOut();
        });
    }

    @Override
    public void setLoginShowing() {
        preferenceManager.setLoginShowing(true);
    }

    @Override
    public void attachView(LoginContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public LoginContract.View getView() {
        return view;
    }
}
