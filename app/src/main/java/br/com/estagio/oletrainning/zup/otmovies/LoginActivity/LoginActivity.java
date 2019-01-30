package br.com.estagio.oletrainning.zup.otmovies.LoginActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import br.com.estagio.oletrainning.zup.otmovies.InformTokenAndNewPasswordActivity.InformTokenAndNewPasswordActivity;
import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.SyncProgressBar;


public class LoginActivity extends AppCompatActivity {

    private LoginViewHolder loginViewHolder;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_login, null);
        this.loginViewHolder = new LoginViewHolder(view);
        setContentView(view);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        String emailAdd = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
        loginViewHolder.textViewEmailEntered.setText(emailAdd);

        setupObservers();
    }

    private void colorStatusBarBackground(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getColor(R.color.colorBackground));
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void colorStatusBarRed(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.GONE);
        window.setStatusBarColor(getColor(R.color.colorRed));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBarBackground();
        setupListeners();
    }

    private void setupObservers() {
        loginViewModel.getPasswordContainsErrorStatus().observe(this, passwordContainsErrorObserver);
        loginViewModel.getIsLoading().observe(this, progressBarObserver);
    }

    private void setupListeners() {
        loginViewHolder.buttonSignIn.setOnClickListener(buttonSignInOnClickListener);
        loginViewHolder.imageViewBackArrow.setOnClickListener(backArrowOnClickListener);
        loginViewHolder.buttonSignIn.setOnClickListener(buttonSignInOnClickListener);
        loginViewHolder.errorEditTextPassword.getEditText().addTextChangedListener(editTextPasswordTextChangedListener);
        loginViewHolder.textViewForgetPassword.setOnClickListener(textViewForgetPasswordOnClickListener);
    }

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
                if (isLoading) {
                    loginViewHolder.progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new SyncProgressBar(LoginActivity.this, loginViewHolder.progressBar).execute();
                } else {
                    loginViewHolder.progressBar.setProgress(100);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    loginViewHolder.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    private View.OnClickListener buttonSignInOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
            String password = loginViewHolder.errorEditTextPassword.getText().toString().trim();
            loginViewModel.passwordEntered(password);
            if (loginViewModel.isValidPassword(password)) {
                loginViewModel.serviceStarting();
                loginViewModel.passwordValidation(email, password)
                        .observe(LoginActivity.this, serviceCallPassValidationObserver);
            }
        }
    };

    Observer<ResponseModel> serviceCallPassValidationObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            loginViewModel.serviceEnding();
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    loginViewModel.setPasswordContainsErrorStatus(false);
                    Toast.makeText(LoginActivity.this, getString(R.string.success_message_login), Toast.LENGTH_LONG).show();
                } else {
                    String key = responseModel.getKey();
                    String message = responseModel.getMessage();
                    if (loginViewModel.isMessageToPutInTopToast(key)) {
                        loginViewHolder.textViewRedToast.setText(message);
                        loginViewModel.setPasswordContainsErrorStatus(true);
                    } else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.service_or_connection_error_login), Toast.LENGTH_LONG).show();
            }
        }

    };

    Observer<ResponseModel> serviceCallResendObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            loginViewModel.serviceEnding();
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    Toast.makeText(LoginActivity.this, getString(R.string.success_message_email), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, InformTokenAndNewPasswordActivity.class);
                    String emailInput = loginViewHolder.textViewEmailEntered.getText().toString().trim();
                    intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, responseModel.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.service_or_connection_error_token), Toast.LENGTH_LONG).show();
            }
        }
    };


    View.OnClickListener backArrowOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(LoginActivity.this, PreLoginActivity.class);
                startActivity(intent);
            }
        }
    };

    View.OnClickListener textViewForgetPasswordOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callTokenResend();
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

    private void callTokenResend() {
        String email = loginViewHolder.textViewEmailEntered.getText().toString().trim();
        loginViewModel.serviceStarting();
        loginViewModel.resendToken(email)
                .observe(LoginActivity.this, serviceCallResendObserver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, PreLoginActivity.class);
        startActivity(intent);
    }
}