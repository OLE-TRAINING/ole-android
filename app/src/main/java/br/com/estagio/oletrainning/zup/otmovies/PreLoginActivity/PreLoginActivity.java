package br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import android.widget.Toast;


import br.com.estagio.oletrainning.zup.otmovies.Common.CommonActivity;
import br.com.estagio.oletrainning.zup.otmovies.FinishYourRegistrationActivity.FinishYourRegistrationActivity;
import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity.RegisterNewUserActivity;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonEmail;


public class PreLoginActivity extends CommonActivity {

    private PreLoginViewHolder preLoginViewHolder;
    private PreLoginViewModel preLoginViewModel;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_pre_login, null);
        this.preLoginViewHolder = new PreLoginViewHolder(view);
        setContentView(view);

        preLoginViewModel = ViewModelProviders.of(this).get(PreLoginViewModel.class);

        hideKeyword(getWindow());

        setupObservers();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBar(this.getWindow(),R.color.colorBackground,true);
        setupListeners();
    }

    private void setupObservers() {
        preLoginViewModel.getEmailContainsErrorStatus().observe(this, errorStatusObserver);
        preLoginViewModel.getRegistrationStatus().observe(this,registrationStatusObserver);
        preLoginViewModel.getIsLoading().observe(this, progressBarObserver);
        preLoginViewModel.getIsInvalidEmail().observe(this, isInvalidEmailObserver);
        preLoginViewModel.getIsErrorMessageForToast().observe(this, isErrorMessageForToastObserver);
    }

    private void setupListeners() {
        preLoginViewHolder.buttonNextPreLogin.setOnClickListener(buttonNextPreLoginOnClickListener);
        preLoginViewHolder.errorEditTextEmail.getEditText().addTextChangedListener(editTextEmailTextChangedListener);
    }

    private View.OnClickListener buttonNextPreLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboardFrom(PreLoginActivity.this,
                    preLoginViewHolder.errorEditTextEmail);
            email = preLoginViewHolder.errorEditTextEmail.getEditText().getText().toString().trim();
            preLoginViewModel.emailEntered(email);
        }
    };

    private Observer<String> registrationStatusObserver = new Observer<String>() {
        @Override
        public void onChanged(String status) {
            if (status.equals(getString(R.string.registered))) {
                Intent intent = new Intent(PreLoginActivity.this, LoginActivity.class);
                startActivity(intent);
            } else if (status.equals(getString(R.string.pending))) {
                Intent intent = new Intent(PreLoginActivity.this, FinishYourRegistrationActivity.class);
                startActivity(intent);
            } else if (status.equals(getString(R.string.inexistent))) {
                Intent intent = new Intent(PreLoginActivity.this, RegisterNewUserActivity.class);
                startActivity(intent);
            }
        }
    };

    private Observer<Boolean> isInvalidEmailObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isInvalidEmail) {
            preLoginViewHolder.errorEditTextEmail.setErrorVisibility(true);
        }
    };

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            Toast.makeText(PreLoginActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };

    private Observer<Boolean> errorStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                preLoginViewHolder.errorEditTextEmail.setErrorVisibility(containsErrorStatus);
            }
        }

    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(
                    isLoading,
                    preLoginViewHolder.progressBar,
                    getWindow(),
                    PreLoginActivity.this);
        }
    };

    private TextWatcher editTextEmailTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            preLoginViewModel.textChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preLoginViewModel.removeObserver();
    }
}