package com.stingandroid.firebaseproject.features.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseUser;
import com.stingandroid.firebaseproject.R;
import com.stingandroid.firebaseproject.features.shared.BaseActivity;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainContract.View{

    private boolean isLoginShowing;

    @Inject
    MainPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNavController(this,R.id.fragment_main);
        getActivityComponent().inject(this);
        presenter.attachView(this);
        setUpNavigation();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isLoginShowing = presenter.isLoginShowingRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isLoginShowing) presenter.isLogged(getFirebaseAuth());
    }

    @Override
    public void showLogin() {
        getNavController().popBackStack();
        getNavController().navigate(R.id.navigation_login);
    }

    @Override
    public void setUpNavigation() {
        NavigationUI.setupActionBarWithNavController(this,getNavController());
    }

    @Override
    public boolean onSupportNavigateUp() {
        return getNavController().navigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
