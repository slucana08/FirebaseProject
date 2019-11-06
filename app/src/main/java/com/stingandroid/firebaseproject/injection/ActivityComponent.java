package com.stingandroid.firebaseproject.injection;

import com.stingandroid.firebaseproject.features.main.MainActivity;
import com.stingandroid.firebaseproject.features.main.account.AccountFragment;
import com.stingandroid.firebaseproject.features.main.movies.MoviesFragment;
import com.stingandroid.firebaseproject.features.userlogin.login.LoginFragment;
import com.stingandroid.firebaseproject.features.userlogin.register.RegisterFragmemt;
import com.stingandroid.firebaseproject.injection.annotations.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent {

    void inject(LoginFragment fragment);

    void inject(RegisterFragmemt fragmemt);

    void inject(AccountFragment fragment);

    void inject(MoviesFragment fragment);

    void inject(MainActivity activity);

}
