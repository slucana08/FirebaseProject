package com.stingandroid.firebaseproject.features.main.movies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.database.FirebaseDatabase;
import com.stingandroid.firebaseproject.R;
import com.stingandroid.firebaseproject.databinding.FragmentMoviesBinding;
import com.stingandroid.firebaseproject.features.shared.BaseActivity;
import com.stingandroid.firebaseproject.features.shared.BaseFragment;

import javax.inject.Inject;

public class MoviesFragment extends BaseFragment implements MoviesContract.View{

    private FragmentMoviesBinding binding;
    private FirebaseDatabase database;

    @Inject
    MoviesPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
        database = ((BaseActivity) getActivity()).getDatabase();
        presenter.getUserDataFirebase(database);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out: {
                ((BaseActivity) getActivity()).getFirebaseAuth().signOut();
                ((BaseActivity) getActivity()).getNavController().popBackStack();
                ((BaseActivity) getActivity()).getNavController().navigate(R.id.navigation_login);
                return true;
            }
            default:
                return NavigationUI.onNavDestinationSelected(item, ((BaseActivity) getActivity()).getNavController())
                        || super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies,container,false);
        return binding.getRoot();
    }

    @Override
    public void showAccountInfo() {
        ((BaseActivity) getActivity()).getNavController().navigate(R.id.action_moviesFragment_to_accountFragment);
    }
}
