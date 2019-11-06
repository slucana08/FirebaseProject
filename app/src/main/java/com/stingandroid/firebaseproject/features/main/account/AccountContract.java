package com.stingandroid.firebaseproject.features.main.account;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.stingandroid.firebaseproject.data.models.User;
import com.stingandroid.firebaseproject.features.shared.BaseContractView;
import com.stingandroid.firebaseproject.features.shared.BasePresenter;
import com.stingandroid.firebaseproject.features.shared.Error;

public interface AccountContract {

    interface view extends BaseContractView{
        void showData(User user);

        void setUpViews();

        void verifyFields();

        void onSuccess();

        void requestPassword();

        void onError(int type, Error error);

        void hideChangePassword();

        void gatherData();
    }

    interface Presenter extends BasePresenter<AccountContract.view>{

        void setUpViews();

        void getDataPrefs();

        void verifyEmail(FirebaseAuth firebaseAuth, String newEmail, String password, String newPassword);

        void updateEmail(FirebaseAuth firebaseAuth, String email, String newEmail,
                         String password, String newPassword);

        void sendVerificationEmail(FirebaseAuth firebaseAuth, String email, String newEmail,
                                   String password, String newPassword);

        void updatePassword(FirebaseAuth firebaseAuth, String email, String newEmail,
                         String password, String newPassword);

        void saveData(FirebaseAuth firebaseAuth, FirebaseDatabase database, String names,
                      String surname, String newEmail, String password, String newPassword);
    }
}
