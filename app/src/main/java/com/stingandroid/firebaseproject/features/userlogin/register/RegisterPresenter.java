package com.stingandroid.firebaseproject.features.userlogin.register;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stingandroid.firebaseproject.data.PreferenceManager;
import com.stingandroid.firebaseproject.data.models.User;
import com.stingandroid.firebaseproject.features.shared.Error;
import com.stingandroid.firebaseproject.utils.Constants;

import javax.inject.Inject;

public class RegisterPresenter implements RegisterContract.Presenter{

    private RegisterContract.View view;
    private PreferenceManager preferenceManager;

    @Inject
    public RegisterPresenter(PreferenceManager preferenceManager){
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void setUpViews() {
        getView().setUpViews();
    }

    @Override
    public void createAccount(Activity activity, FirebaseAuth firebaseAuth, FirebaseDatabase database, String email,
                              String password, String names, String surname) {
        firebaseAuth.createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(activity, task -> {
                    if (!task.isSuccessful()){
                        if (task.getException() instanceof FirebaseAuthUserCollisionException)
                            getView().onError(0,null);
                        else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            getView().onError(1,null);
                        else
                            getView().onError(2,
                                    () -> createAccount(activity, firebaseAuth, database, email, password, names, surname));
                        return;
                    }
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    String UID = user.getUid();
                    createProfile(database,firebaseAuth,UID,names,surname);
                });
    }

    @Override
    public void createProfile(FirebaseDatabase database, FirebaseAuth firebaseAuth, String UID,
                              String names, String surname) {
        User user = new User(names,surname);
        DatabaseReference userRef = database.getReference(Constants.FIREBASE_USER_INFO).child(UID);
        userRef.setValue(user).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                getView().onError(3, () -> createProfile(database, firebaseAuth, UID, names, surname));
                return;
            }
            sendEmailVerification(firebaseAuth);
        });
    }

    @Override
    public void sendEmailVerification(FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                getView().onError(4, () -> sendEmailVerification(firebaseAuth));
                return;
            }
            firebaseAuth.signOut();
            getView().onSuccess();
        });
    }

    @Override
    public void setLoginShowing() {
        preferenceManager.setLoginShowing(true);
    }

    @Override
    public void attachView(RegisterContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public RegisterContract.View getView() {
        return view;
    }
}
