package com.stingandroid.firebaseproject.features.main.account;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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

public class AccountPresenter implements AccountContract.Presenter {

    private AccountContract.view view;
    private PreferenceManager preferenceManager;

    private String names,surname;

    @Inject
    public AccountPresenter(PreferenceManager preferenceManager){
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void setUpViews() {
        getView().setUpViews();
    }

    @Override
    public void getDataPrefs() {
        getView().showData(preferenceManager.getUserData());
    }

    @Override
    public void saveData(FirebaseAuth firebaseAuth, FirebaseDatabase database, String names,
                         String surname, String newEmail, String password, String newPassword) {
        DatabaseReference userRef = database.getReference(Constants.FIREBASE_USER_INFO)
                .child(preferenceManager.getUserData().getUID());
        User userToSave = new User(names,surname);
        userRef.setValue(userToSave).addOnCompleteListener(task -> {
            if (!task.isSuccessful()){
                getView().onError(3,null);
                return;
            }

            this.names = names;
            this.surname = surname;

            User userPrefs = preferenceManager.getUserData();
            userPrefs.setNames(names);
            userPrefs.setSurname(surname);
            preferenceManager.setUserData(userPrefs);
            verifyEmail(firebaseAuth, newEmail, password, newPassword);
        });
    }

    @Override
    public void verifyEmail(FirebaseAuth firebaseAuth, String newEmail, String password, String newPassword) {
        String prefsEmail = preferenceManager.getUserData().getEmail();
        String prefsPassword = preferenceManager.getUserData().getPassword();

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){
            getView().onError(2, null);
            return;
        }

        if (!TextUtils.equals(prefsEmail,newEmail)) {
            if (TextUtils.isEmpty(password)) {
                getView().requestPassword();
                return;
            }
            if (!TextUtils.equals(password, prefsPassword)) {
                getView().onError(0, null);
                return;
            }
            updateEmail(firebaseAuth, prefsEmail, newEmail, password, newPassword);
        } else {
            if (!TextUtils.isEmpty(newPassword)){
                updatePassword(firebaseAuth,prefsEmail,newEmail,prefsPassword,newPassword);
            } else {
                getView().onSuccess();
            }
        }
    }

    @Override
    public void updateEmail(FirebaseAuth firebaseAuth, String email, String newEmail, String password, String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        getView().onError(3, null);
                        return;
                    }
                    FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                    user1.updateEmail(newEmail)
                            .addOnCompleteListener(task1 -> {
                                if (!task1.isSuccessful()){
                                    if (task1.getException() instanceof FirebaseAuthUserCollisionException)
                                        getView().onError(1, null);
                                    if (task1.getException() instanceof FirebaseAuthInvalidCredentialsException)
                                        getView().onError(2, null);
                                    else
                                        getView().onError(3, null);
                                    return;
                                }
                                User userPrefs = preferenceManager.getUserData();
                                userPrefs.setEmail(newEmail);
                                preferenceManager.setUserData(userPrefs);
                                sendVerificationEmail(firebaseAuth, email, newEmail, password, newPassword);
                            });
                });
    }

    @Override
    public void sendVerificationEmail(FirebaseAuth firebaseAuth, String email, String newEmail,
                                      String password, String newPassword) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                getView().onError(4, () -> sendVerificationEmail(firebaseAuth, email, newEmail, password, newPassword));
                return;
            }
            if (!TextUtils.isEmpty(newPassword)){
                updatePassword(firebaseAuth, email, newEmail, password, newPassword);
            } else {
                getView().onSuccess();
            }
        });
    }

    @Override
    public void updatePassword(FirebaseAuth firebaseAuth, String email, String newEmail, String password, String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        getView().onError(5,
                                () -> updatePassword(firebaseAuth, email, newEmail, password, newPassword));
                        return;
                    }
                    FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                    user1.updatePassword(newPassword)
                            .addOnCompleteListener(task1 -> {
                                if (!task1.isSuccessful()){
                                    getView().onError(5 ,
                                            () -> updatePassword(firebaseAuth, email, newEmail, password, newPassword));
                                    return;
                                }
                                User userPrefs = preferenceManager.getUserData();
                                userPrefs.setPassword(newPassword);
                                preferenceManager.setUserData(userPrefs);
                                getView().onSuccess();
                            });
                });
    }

    @Override
    public void attachView(AccountContract.view mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public AccountContract.view getView() {
        return view;
    }
}
