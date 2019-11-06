package com.stingandroid.firebaseproject.features.shared;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.stingandroid.firebaseproject.App;
import com.stingandroid.firebaseproject.injection.ActivityComponent;
import com.stingandroid.firebaseproject.injection.DaggerActivityComponent;

public class BaseActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;
    private NavController navController;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        activityComponent = DaggerActivityComponent.builder()
                .appComponent(App.get().getAppComponent())
                .build();

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    protected void buildComponent() {
        activityComponent = DaggerActivityComponent.builder()
                .appComponent(App.get().getAppComponent())
                .build();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }


    public NavController getNavController() {
        return navController;
    }

    public void setNavController(Activity activity,int navHost){
        navController = Navigation.findNavController(activity,navHost);
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }


    public FirebaseDatabase getDatabase() {
        return database;
    }
}
