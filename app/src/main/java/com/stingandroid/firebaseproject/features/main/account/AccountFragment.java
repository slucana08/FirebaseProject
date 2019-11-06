package com.stingandroid.firebaseproject.features.main.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.stingandroid.firebaseproject.R;
import com.stingandroid.firebaseproject.data.models.User;
import com.stingandroid.firebaseproject.databinding.FragmentAccountBinding;
import com.stingandroid.firebaseproject.features.shared.BaseActivity;
import com.stingandroid.firebaseproject.features.shared.BaseFragment;
import com.stingandroid.firebaseproject.features.shared.Error;

import javax.inject.Inject;

public class AccountFragment extends BaseFragment implements AccountContract.view {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    private FragmentAccountBinding binding;

    private String names,surname,email,password,newPassword;
    private Snackbar snackbar;

    @Inject
    AccountPresenter presenter;

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account,container,false);
        presenter.setUpViews();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getDataPrefs();
    }

    @Override
    public void showData(User user) {
        if (TextUtils.isEmpty(user.getNames())){
            snackbar = Snackbar.make(binding.namesInputLayout,
                    getActivity().getString(R.string.complete_profile),
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getActivity().getString(R.string.save), view -> {
                verifyFields();
            });
            snackbar.show();
        }
        binding.namesInputLayout.getEditText().setText(user.getNames());
        binding.surnameTextInputLayout.getEditText().setText(user.getSurname());
        binding.emailTextInputLayout.getEditText().setText(user.getEmail());
    }

    @Override
    public void gatherData() {
        names = binding.namesInputLayout.getEditText().getText().toString();
        surname = binding.surnameTextInputLayout.getEditText().getText().toString();
        email = binding.emailTextInputLayout.getEditText().getText().toString();
        password = binding.passwordTextInputLayout.getEditText().getText().toString();
        newPassword = binding.newPasswordTextInputLayout.getEditText().getText().toString();
    }

    @Override
    public void setUpViews() {
        binding.changePasswordImageView.setOnClickListener(view -> {
            binding.passwordTextInputLayout.setVisibility(binding.topView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            binding.topView.setVisibility(binding.topView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            if (binding.newPasswordTextInputLayout.getVisibility() == View.VISIBLE) {
                binding.passwordTextInputLayout.getEditText().getText().clear();
                binding.newPasswordTextInputLayout.getEditText().getText().clear();
            }
            binding.newPasswordTextInputLayout.setVisibility(binding.newPasswordTextInputLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            binding.bottomView.setVisibility(binding.bottomView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            binding.labelTextView.setVisibility(binding.labelTextView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });
        binding.saveButton.setOnClickListener(view -> {
            verifyFields();
        });
    }

    @Override
    public void verifyFields() {
        gatherData();
        if (snackbar != null) snackbar.dismiss();
        binding.saveButton.setEnabled(false);

        if (TextUtils.isEmpty(names)) {
            binding.namesInputLayout.setError(getActivity().getString(R.string.field_required));
            binding.namesInputLayout.getEditText().requestFocus();
            binding.saveButton.setEnabled(true);
            return;
        } else binding.namesInputLayout.setError(null);

        if (TextUtils.isEmpty(surname)) {
            binding.surnameTextInputLayout.setError(getActivity().getString(R.string.field_required));
            binding.surnameTextInputLayout.getEditText().requestFocus();
            binding.saveButton.setEnabled(true);
            return;
        } else binding.surnameTextInputLayout.setError(null);

        if (TextUtils.isEmpty(email)) {
            binding.emailTextInputLayout.setError(getActivity().getString(R.string.field_required));
            binding.emailTextInputLayout.getEditText().requestFocus();
            binding.saveButton.setEnabled(true);
            return;
        } else binding.emailTextInputLayout.setError(null);

        if (binding.topView.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(password)) {
                binding.passwordTextInputLayout.setError(getActivity().getString(R.string.field_required));
                binding.passwordTextInputLayout.getEditText().requestFocus();
                binding.saveButton.setEnabled(true);
                return;
            } else binding.passwordTextInputLayout.setError(null);

            if (TextUtils.isEmpty(newPassword)) {
                binding.newPasswordTextInputLayout.setError(getActivity().getString(R.string.field_required));
                binding.newPasswordTextInputLayout.getEditText().requestFocus();
                binding.saveButton.setEnabled(true);
                return;
            } else binding.newPasswordTextInputLayout.setError(null);

            if (newPassword.length() < 6) {
                binding.newPasswordTextInputLayout.setError(getActivity().getString(R.string.minimum_pass_length));
                binding.newPasswordTextInputLayout.getEditText().requestFocus();
                binding.saveButton.setEnabled(true);
                return;
            } else binding.newPasswordTextInputLayout.setError(null);
        }

        presenter.saveData(firebaseAuth, database,names,surname,email,password,newPassword);
    }

    @Override
    public void onSuccess() {
        Toast.makeText(getActivity(),getActivity().getString(R.string.information_updated),Toast.LENGTH_SHORT).show();
        ((BaseActivity) getActivity()).getNavController().navigate(R.id.action_accountFragment_to_moviesFragment);
    }

    @Override
    public void requestPassword() {
        binding.passwordTextInputLayout.setVisibility(View.VISIBLE);
        binding.passwordTextInputLayout.getEditText().requestFocus();
        snackbar = Snackbar.make(binding.passwordTextInputLayout,
                getActivity().getString(R.string.input_password),
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getActivity().getString(R.string.save), view -> {
            verifyFields();
        });
        snackbar.show();
    }

    @Override
    public void onError(int type, Error error) {
        String errorMessage = "";

        switch (type){
            case 0:
                errorMessage = getActivity().getString(R.string.wrong_credentials);
                break;
            case 1:
                errorMessage = getActivity().getString(R.string.email_used);
                break;
            case 2:
                errorMessage = getActivity().getString(R.string.bad_email);
                break;
            case 3:
                errorMessage = getActivity().getString(R.string.error_occurred);
                break;
            case 4:
                errorMessage = getActivity().getString(R.string.error_verification_email);
                break;
            case 5:
                errorMessage = getActivity().getString(R.string.error_update_password);
                break;
        }

        snackbar = Snackbar.make(binding.emailTextInputLayout,errorMessage,Snackbar.LENGTH_LONG);

        switch (type){
            case 0:
                hideChangePassword();
                presenter.getDataPrefs();
                snackbar.show();
                break;
            case 1:
            case 2:
                hideChangePassword();
                binding.emailTextInputLayout.setError(errorMessage);
                binding.emailTextInputLayout.getEditText().requestFocus();
                break;
            case 3:
                snackbar.setAction(getActivity().getString(R.string.retry), view -> verifyFields());
                snackbar.show();
                break;
            case 4:
            case 5:
                snackbar.setAction(getActivity().getString(R.string.retry), view -> error.onError());
                snackbar.show();
                break;
        }
    }

    @Override
    public void hideChangePassword() {
        binding.passwordTextInputLayout.getEditText().getText().clear();
        binding.passwordTextInputLayout.setVisibility(View.GONE);
        binding.newPasswordTextInputLayout.getEditText().getText().clear();
        binding.newPasswordTextInputLayout.setVisibility(View.GONE);
        binding.topView.setVisibility(View.GONE);
        binding.bottomView.setVisibility(View.GONE);
        binding.labelTextView.setVisibility(View.GONE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (snackbar != null) snackbar.dismiss();
        presenter.detachView();
    }
}
