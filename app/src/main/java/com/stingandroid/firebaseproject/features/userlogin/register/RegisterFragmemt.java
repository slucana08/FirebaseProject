package com.stingandroid.firebaseproject.features.userlogin.register;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.stingandroid.firebaseproject.R;
import com.stingandroid.firebaseproject.databinding.FragmentRegisterBinding;
import com.stingandroid.firebaseproject.features.shared.BaseActivity;
import com.stingandroid.firebaseproject.features.shared.BaseFragment;
import com.stingandroid.firebaseproject.features.shared.Error;

import javax.inject.Inject;

public class RegisterFragmemt extends BaseFragment implements RegisterContract.View {

    private FragmentRegisterBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    private Snackbar snackbar;

    @Inject
    RegisterPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
        firebaseAuth = ((BaseActivity) getActivity()).getFirebaseAuth();
        database = ((BaseActivity) getActivity()).getDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_register,container,false);
        presenter.setUpViews();
        return binding.getRoot();
    }

    public void setUpViews(){
        binding.registerButton.setOnClickListener(view -> verifyFields());
    }

    @Override
    public void verifyFields() {
        binding.registerButton.setEnabled(false);
        String name = binding.namesInputLayout.getEditText().getText().toString();
        String surname = binding.surnameTextInputLayout.getEditText().getText().toString();
        String email = binding.emailTextInputLayout.getEditText().getText().toString();
        String password = binding.passwordTextInputLayout.getEditText().getText().toString();

        if (TextUtils.isEmpty(name)) {
            binding.namesInputLayout.setError(getActivity().getString(R.string.field_required));
            binding.namesInputLayout.getEditText().requestFocus();
            binding.registerButton.setEnabled(true);
            return;
        } else binding.namesInputLayout.setError(null);

        if (TextUtils.isEmpty(surname)) {
            binding.surnameTextInputLayout.setError(getActivity().getString(R.string.field_required));
            binding.surnameTextInputLayout.getEditText().requestFocus();
            binding.registerButton.setEnabled(true);
            return;
        } else binding.surnameTextInputLayout.setError(null);

        if (TextUtils.isEmpty(email)) {
            binding.emailTextInputLayout.setError(getActivity().getString(R.string.field_required));
            binding.emailTextInputLayout.getEditText().requestFocus();
            binding.registerButton.setEnabled(true);
            return;
        } else binding.emailTextInputLayout.setError(null);

        if (TextUtils.isEmpty(password)) {
            binding.passwordTextInputLayout.setError(getActivity().getString(R.string.field_required));
            binding.passwordTextInputLayout.getEditText().requestFocus();
            binding.registerButton.setEnabled(true);
            return;
        } else  binding.passwordTextInputLayout.setError(null);

        if (password.length() < 6) {
            binding.passwordTextInputLayout.setError(getActivity().getString(R.string.minimum_pass_length));
            binding.passwordTextInputLayout.getEditText().requestFocus();
            binding.registerButton.setEnabled(true);
            return;
        } else binding.passwordTextInputLayout.setError(null);

        presenter.createAccount(getActivity(),firebaseAuth,database,email,password,name,surname);
    }

    @Override
    public void onError(int type, Error error) {
        String errorMessage = "";

        switch (type){
            case 0:
                errorMessage = getActivity().getString(R.string.email_used);
                break;
            case 1:
                errorMessage = getActivity().getString(R.string.bad_email);
                break;
            case 2:
                errorMessage = getActivity().getString(R.string.error_creating_account);
                break;
            case 3:
                errorMessage = getActivity().getString(R.string.error_creating_profile);
                break;
            case 4:
                errorMessage = getActivity().getString(R.string.error_verification_email);
                break;
        }

        switch (type){
            case 0:
            case 1:
                binding.registerButton.setEnabled(true);
                snackbar = Snackbar.make(binding.emailTextInputLayout, errorMessage, Snackbar.LENGTH_LONG);
                String finalErrorMessage = errorMessage;
                snackbar.setAction(getActivity().getString(R.string.ok), view -> {
                    binding.emailTextInputLayout.getEditText().requestFocus();
                    binding.emailTextInputLayout.setError(finalErrorMessage);
                });
                break;
            default :
                snackbar = Snackbar.make(binding.emailTextInputLayout,errorMessage,Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(getActivity().getString(R.string.retry), view -> error.onError());
                break;
        }
        snackbar.show();
    }

    @Override
    public void onSuccess() {
        snackbar = Snackbar.make(binding.emailTextInputLayout,getActivity().getString(R.string.verify_email)
                ,Snackbar.LENGTH_LONG);
        snackbar.show();
        firebaseAuth.signOut();
        ((BaseActivity) getActivity()).getNavController().navigate(R.id.action_registerFragmemt_to_loginFragment);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.setLoginShowing();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
    }
}
