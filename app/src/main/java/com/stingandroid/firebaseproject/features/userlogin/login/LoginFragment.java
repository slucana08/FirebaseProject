package com.stingandroid.firebaseproject.features.userlogin.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.stingandroid.firebaseproject.R;
import com.stingandroid.firebaseproject.databinding.FragmentLoginBinding;
import com.stingandroid.firebaseproject.features.shared.BaseActivity;
import com.stingandroid.firebaseproject.features.shared.BaseFragment;

import javax.inject.Inject;

public class LoginFragment extends BaseFragment implements LoginContract.View {

    private FragmentLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    @Inject
    LoginPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
        firebaseAuth = ((BaseActivity) getActivity()).getFirebaseAuth();
        database = ((BaseActivity) getActivity()).getDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false);
        presenter.setUpViews();
        return binding.getRoot();
    }

    public void setUpViews(){
        binding.createUserImageView.setOnClickListener(view ->
                ((BaseActivity) getActivity()).getNavController().navigate(R.id.action_loginFragment_to_registerFragmemt));
        binding.loginButton.setOnClickListener(view -> verifyFields());
        binding.passwordTextInputLayout.getEditText().setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE){
                verifyFields();
            }
            return false;
        });
    }

    @Override
    public void onError(int type) {
        String error = "";
        binding.loginButton.setEnabled(true);

        switch (type){
            case 0:
                error = getActivity().getString(R.string.wrong_credentials);
                firebaseAuth.signOut();
                break;
            case 1:
                error = getActivity().getString(R.string.error_occurred);
                firebaseAuth.signOut();
                break;
            case 2:
                error = getActivity().getString(R.string.must_verify_email);
                break;
            case 3:
                error = getActivity().getString(R.string.try_later_open_app);
                break;
        }

        Snackbar snackbar = Snackbar.make(binding.loginButton,error,Snackbar.LENGTH_LONG);

        if (type == 0) snackbar.setAction(getActivity().getString(R.string.ok),
                view -> binding.emailTextInputLayout.getEditText().requestFocus());
        else if (type == 2) snackbar.setAction(getActivity().getString(R.string.resend),
                view -> presenter.resendEmailVerification(firebaseAuth));
        snackbar.show();
    }

    @Override
    public void onSuccess() {
        ((BaseActivity) getActivity()).getNavController().navigate(R.id.action_global_moviesFragment);
    }

    @Override
    public void verifyFields() {
        binding.loginButton.setEnabled(false);
        String email = binding.emailTextInputLayout.getEditText().getText().toString();
        String password = binding.passwordTextInputLayout.getEditText().getText().toString();

        if (TextUtils.isEmpty(email)) {
            binding.emailTextInputLayout.setError(getActivity().getString(R.string.field_required));
            binding.emailTextInputLayout.getEditText().requestFocus();
            binding.loginButton.setEnabled(true);
            return;
        } else binding.emailTextInputLayout.setError(null);

        if (TextUtils.isEmpty(password)) {
            binding.passwordTextInputLayout.setError(getActivity().getString(R.string.field_required));
            binding.passwordTextInputLayout.getEditText().requestFocus();
            binding.loginButton.setEnabled(true);
            return;
        } else  binding.passwordTextInputLayout.setError(null);

        presenter.processLogin(getActivity(),firebaseAuth,database,email,password);
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
