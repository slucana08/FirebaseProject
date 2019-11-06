package com.stingandroid.firebaseproject.features.main.movies;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stingandroid.firebaseproject.data.PreferenceManager;
import com.stingandroid.firebaseproject.data.models.User;
import com.stingandroid.firebaseproject.utils.Constants;

import javax.inject.Inject;

public class MoviesPresenter implements MoviesContract.Presenter {

    private MoviesContract.View view;
    private PreferenceManager preferenceManager;

    @Inject
    public MoviesPresenter(PreferenceManager preferenceManager){
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void getUserDataFirebase(FirebaseDatabase database) {
        User userPrefs = preferenceManager.getUserData();
        String UID = userPrefs.getUID();
        if (UID != null) {
            DatabaseReference userRef = database.getReference(Constants.FIREBASE_USER_INFO).child(UID);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()){
                        getView().showAccountInfo();
                        return;
                    }
                    User userFireBase = dataSnapshot.getValue(User.class);
                    userPrefs.setNames(userFireBase.getNames());
                    userPrefs.setSurname(userFireBase.getSurname());
                    preferenceManager.setUserData(userPrefs);

                    if (TextUtils.isEmpty(userFireBase.getNames()) ||
                        TextUtils.isEmpty(userFireBase.getSurname())){
                        getView().showAccountInfo();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void attachView(MoviesContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public MoviesContract.View getView() {
        return view;
    }
}
