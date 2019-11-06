package com.stingandroid.firebaseproject.features.main.favourites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.stingandroid.firebaseproject.R;
import com.stingandroid.firebaseproject.databinding.FragmentFavouritesBinding;
import com.stingandroid.firebaseproject.features.shared.BaseFragment;

public class FavouritesFragment extends BaseFragment {

    private FragmentFavouritesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites,container,false);
        return binding.getRoot();
    }
}
