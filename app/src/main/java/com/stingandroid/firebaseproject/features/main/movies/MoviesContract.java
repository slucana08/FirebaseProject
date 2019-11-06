package com.stingandroid.firebaseproject.features.main.movies;

import com.google.firebase.database.FirebaseDatabase;
import com.stingandroid.firebaseproject.features.shared.BaseContractView;
import com.stingandroid.firebaseproject.features.shared.BasePresenter;

public interface MoviesContract {

    interface View extends BaseContractView {

        void showAccountInfo();

    }

    interface Presenter extends BasePresenter<MoviesContract.View>{

        void getUserDataFirebase(FirebaseDatabase database);

    }
}
