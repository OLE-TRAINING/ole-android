package br.com.estagio.oletrainning.zup.otmovies.LoginActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonActivity;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.HomeActivity;
import br.com.estagio.oletrainning.zup.otmovies.InformTokenAndNewPasswordActivity.InformTokenAndNewPasswordActivity;
import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;


public class LoginActivity extends CommonActivity {

    private LoginViewHolder loginViewHolder;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_login, null);
        this.loginViewHolder = new LoginViewHolder(view);
        setContentView(view);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        setupObservers();

        Bundle bundle = getIntent().getExtras();

        loginViewModel.setBundle(bundle);

        hideKeyword(getWindow());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBarBackground();
        setupListeners();
    }

    private void colorStatusBarBackground(){
        colorStatusBar(this.getWindow(),R.color.colorBackground,true);
    }

    private void colorStatusBarRed(){
        colorStatusBar(this.getWindow(),R.color.colorRed,false);
    }

    private void setupObservers() {
        loginViewModel.getPasswordContainsErrorStatus().observe(this, passwordContainsErrorObserver);
        loginViewModel.getIsLoading().observe(this, progressBarObserver);
        loginViewModel.getEmailChanged().observe(this,emailChangedObserver);
        loginViewModel.getIsValidatedPassword().observe(this,isValidatedPasswordObserver);
        loginViewModel.getMessageErrorChanged().observe(this,messageErrorChangedObserver);
        loginViewModel.getIsErrorMessageForToast().observe(this,isErrorMessageForToastObserver);
        loginViewModel.getForwardedToken().observe(this,forwardedTokenObserver);
    }

    private void setupListeners() {
        loginViewHolder.buttonSignIn.setOnClickListener(buttonSignInOnClickListener);
        loginViewHolder.imageViewBackArrow.setOnClickListener(backArrowOnClickListener);
        loginViewHolder.buttonSignIn.setOnClickListener(buttonSignInOnClickListener);
        loginViewHolder.errorEditTextPassword.getEditText().addTextChangedListener(editTextPasswordTextChangedListener);
        loginViewHolder.textViewForgetPassword.setOnClickListener(textViewForgetPasswordOnClickListener);
    }

    private Observer<String> forwardedTokenObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String message) {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, InformTokenAndNewPasswordActivity.class);
            String emailInput = loginViewHolder.textViewEmailEntered.getText().toString().trim();
            intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
            startActivity(intent);
        }
    };

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };

    private Observer<String> messageErrorChangedObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            loginViewHolder.textViewRedToast.setText(message);
            loginViewModel.setPasswordContainsErrorStatus(true);
        }
    };

    private Observer<String> isValidatedPasswordObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            loginViewModel.setPasswordContainsErrorStatus(false);
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            String emailInput = loginViewHolder.textViewEmailEntered.getText().toString().trim();
            intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
            startActivity(intent);
        }
    };

    private Observer<String> emailChangedObserver = new Observer<String>() {
        @Override
        public void onChanged(String email) {
            loginViewHolder.textViewEmailEntered.setText(email);
        }
    };


    private Observer<Boolean> passwordContainsErrorObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                loginViewHolder.errorEditTextPassword.setErrorVisibility(containsErrorStatus);
                loginViewHolder.errorEditTextPassword.setMessageError("");
                if (loginViewModel.getPasswordContainsErrorStatus().getValue() != null) {
                    if (loginViewModel.getPasswordContainsErrorStatus().getValue()) {
                        loginViewHolder.linearLayout.setVisibility(View.VISIBLE);
                        colorStatusBarRed();
                    } else {
                        loginViewHolder.linearLayout.setVisibility(View.GONE);
                        colorStatusBarBackground();
                    }
                }
            }
        }
    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean isLoading) {
            if (isLoading != null) {
                loadingExecutor(
                        isLoading,
                        loginViewHolder.progressBar,
                        getWindow(),
                        LoginActivity.this);
            }
        }
    };

    private View.OnClickListener buttonSignInOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboardFrom(LoginActivity.this,
                    loginViewHolder.errorEditTextPassword);
            String password = loginViewHolder.errorEditTextPassword.getText().toString().trim();
            loginViewModel.passwordEntered(password);
        }
    };

    private View.OnClickListener backArrowOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(LoginActivity.this, PreLoginActivity.class);
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener textViewForgetPasswordOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loginViewModel.tokenForwardingRequested();
        }
    };

    private TextWatcher editTextPasswordTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            loginViewModel.passwordTextChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, PreLoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginViewModel.removeObserver();
    }
}