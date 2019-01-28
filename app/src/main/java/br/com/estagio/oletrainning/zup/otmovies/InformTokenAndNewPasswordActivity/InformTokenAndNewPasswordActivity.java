package br.com.estagio.oletrainning.zup.otmovies.InformTokenAndNewPasswordActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.SyncProgressBar;


public class InformTokenAndNewPasswordActivity extends AppCompatActivity {

    private InformTokenAndNewPasswordViewHolder informTokenAndNewPasswordViewHolder;
    private InformTokenAndNewPasswordViewModel informTokenAndNewPasswordViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_token_and_new_password);

        View view = this.getLayoutInflater().inflate(R.layout.activity_inform_token_and_new_password, null);
        this.informTokenAndNewPasswordViewHolder = new InformTokenAndNewPasswordViewHolder(view);
        setContentView(view);

        informTokenAndNewPasswordViewModel = ViewModelProviders.of(this).get(InformTokenAndNewPasswordViewModel.class);

        informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setVisibility(View.INVISIBLE);

        String emailAdd = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
        informTokenAndNewPasswordViewHolder.textViewEmail.setText(emailAdd);

        setupObservers();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setupListeners();
    }

    private void setupObservers(){
        informTokenAndNewPasswordViewModel.getIsLoading().observe(this, progressBarObserver);
        informTokenAndNewPasswordViewModel.getTokenContainsErrorStatus().observe(this,tokenErrorStatusObserver);
        informTokenAndNewPasswordViewModel.getPasswordContainsErrorStatus().observe(this,passwordContainsErrorObserver);
        informTokenAndNewPasswordViewModel.getConfirmPasswordContainsErrorStatus().observe(this,confirmPasswordContainsErrorObserver);
    }

    private void setupListeners() {
        informTokenAndNewPasswordViewHolder.imageView.setOnClickListener(backArrowOnClickListener);
        informTokenAndNewPasswordViewHolder.errorEditTextToken.getEditText().addTextChangedListener(errorEditTextTextWatcherToken);
        informTokenAndNewPasswordViewHolder.errorEditTextPassword.getEditText().addTextChangedListener(errorEditTextTextWatcherPassword);
        informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.getEditText().addTextChangedListener(errorEditTextTextWatcherConfirmPassword);
        informTokenAndNewPasswordViewHolder.textViewReSendToken.setOnClickListener(textViewOnClickListener);
        informTokenAndNewPasswordViewHolder.button.setOnClickListener(buttonOnClickListener);
    }

    private View.OnClickListener backArrowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(InformTokenAndNewPasswordActivity.this, PreLoginActivity.class);
                startActivity(intent);
            }

        }
    };

    private Observer<Boolean> tokenErrorStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                informTokenAndNewPasswordViewHolder.errorEditTextToken.setErrorVisibility(containsErrorStatus);
            }
        }

    };

    private Observer<Boolean> passwordContainsErrorObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                informTokenAndNewPasswordViewHolder.errorEditTextPassword.setErrorVisibility(containsErrorStatus);
            }
        }
    };

    private Observer<Boolean> confirmPasswordContainsErrorObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(containsErrorStatus);
            }
        }
    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean isLoading) {
            if (isLoading != null) {
                if (isLoading) {
                    informTokenAndNewPasswordViewHolder.progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new SyncProgressBar(InformTokenAndNewPasswordActivity.this, informTokenAndNewPasswordViewHolder.progressBar).execute();
                } else {
                    informTokenAndNewPasswordViewHolder.progressBar.setProgress(100);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    informTokenAndNewPasswordViewHolder.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    Observer<ResponseModel> serviceCallResendObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            informTokenAndNewPasswordViewModel.serviceEnding();
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    Toast.makeText(InformTokenAndNewPasswordActivity.this,getString(R.string.sucess_message_tokenresend), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(InformTokenAndNewPasswordActivity.this, responseModel.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(InformTokenAndNewPasswordActivity.this, getString(R.string.service_or_connection_error_tokenresend), Toast.LENGTH_LONG).show();
            }
        }
    };

    private TextWatcher errorEditTextTextWatcherToken = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            informTokenAndNewPasswordViewModel.tokenTextChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher errorEditTextTextWatcherPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            informTokenAndNewPasswordViewModel.passwordTextChanged();
            String password = informTokenAndNewPasswordViewHolder.errorEditTextPassword.getText().toString().trim();
            if(informTokenAndNewPasswordViewModel.isValidPassword(password)){
                informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(false);
                informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setVisibility(View.VISIBLE);
            } else {
                informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher errorEditTextTextWatcherConfirmPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            informTokenAndNewPasswordViewModel.confirmPasswordTextChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(InformTokenAndNewPasswordActivity.this, PreLoginActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callTokenResend();
        }
    };


    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
            String token = informTokenAndNewPasswordViewHolder.errorEditTextToken.getText().toString().trim();
            String password = informTokenAndNewPasswordViewHolder.errorEditTextPassword.getText().toString().trim();
            String confirmPassword = informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.getText().toString().trim();
            emptyInputShowErrorMessageControl(token,password,confirmPassword);
            passwordsShowErrorMessageControl(password, confirmPassword);
            informTokenAndNewPasswordViewModel.tokenEntered(token);
            informTokenAndNewPasswordViewModel.passwordEntered(password);
            informTokenAndNewPasswordViewModel.confirmPasswordEntered(confirmPassword);
            if(informTokenAndNewPasswordViewModel.isValidToken(token)
                    && informTokenAndNewPasswordViewModel.isValidPassword(password)
                    &&  informTokenAndNewPasswordViewModel.isValidConfirmPassword(password,confirmPassword)){
                informTokenAndNewPasswordViewModel.serviceStarting();
                informTokenAndNewPasswordViewModel.validateTokenAndChangePass(email,token,password,confirmPassword)
                        .observe(InformTokenAndNewPasswordActivity.this, serviceCallObserver);
            }
        }
    };

    Observer<ResponseModel> serviceCallObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            informTokenAndNewPasswordViewModel.serviceEnding();
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    Toast.makeText(InformTokenAndNewPasswordActivity.this,getString(R.string.success_message_change_pass), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(InformTokenAndNewPasswordActivity.this, LoginActivity.class);
                    String emailInput = informTokenAndNewPasswordViewHolder.textViewEmail.getText().toString().trim();
                    intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                    startActivity(intent);
                } else {
                    String key = responseModel.getKey();
                    String message = responseModel.getMessage();
                    if (informTokenAndNewPasswordViewModel.isErrorMessageKeyToPasswordInput(key)) {
                        informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setMessageError("");
                        informTokenAndNewPasswordViewHolder.errorEditTextPassword.setMessageError(message);
                        informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(true);
                        informTokenAndNewPasswordViewHolder.errorEditTextPassword.setErrorVisibility(true);
                    } else if (informTokenAndNewPasswordViewModel.isErrorMessageKeyToTokenInput(key)) {
                        informTokenAndNewPasswordViewHolder.errorEditTextToken.setMessageError(message);
                        informTokenAndNewPasswordViewHolder.errorEditTextToken.setErrorVisibility(true);
                    } else {
                        Toast.makeText(InformTokenAndNewPasswordActivity.this, message, Toast.LENGTH_LONG).show();

                    }
                }
            } else {
                Toast.makeText(InformTokenAndNewPasswordActivity.this, getString(R.string.service_or_connection_error_change_password), Toast.LENGTH_LONG).show();
            }
        }

    };


    private void callTokenResend(){
        String email = informTokenAndNewPasswordViewHolder.textViewEmail.getText().toString().trim();
        informTokenAndNewPasswordViewModel.serviceStarting();
        informTokenAndNewPasswordViewModel.resendToken(email)
                .observe(InformTokenAndNewPasswordActivity.this, serviceCallResendObserver);
    }

    private void emptyInputShowErrorMessageControl(String token, String password, String confirmPassword){
        if(informTokenAndNewPasswordViewModel.isEmptyTokenInput(token)){
            informTokenAndNewPasswordViewHolder.errorEditTextToken.setMessageError(getString(R.string.message_error_empty_input));
            informTokenAndNewPasswordViewHolder.errorEditTextToken.setErrorVisibility(true);
        }
        if(informTokenAndNewPasswordViewModel.isEmptyPasswordInput(password)){
            informTokenAndNewPasswordViewHolder.errorEditTextPassword.setMessageError(getString(R.string.message_error_empty_input));
            informTokenAndNewPasswordViewHolder.errorEditTextPassword.setErrorVisibility(true);
        }
        if(informTokenAndNewPasswordViewModel.isEmptyConfirmPasswordInput(confirmPassword)){
            informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setMessageError(getString(R.string.message_error_empty_input));
            informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(true);
        }

    }

    private void passwordsShowErrorMessageControl(String password, String confirmPassword){
        if(!informTokenAndNewPasswordViewModel.isValidConfirmPassword(password,confirmPassword)){
            informTokenAndNewPasswordViewHolder.errorEditTextPassword.setMessageError(getString(R.string.empty_text));
            informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setMessageError(getString(R.string.noMatchPassword));
            informTokenAndNewPasswordViewHolder.errorEditTextPassword.setErrorVisibility(true);
            informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(true);
        }
        if(!informTokenAndNewPasswordViewModel.isValidPassword(password)){
            informTokenAndNewPasswordViewHolder.errorEditTextPassword.setMessageError(getString(R.string.errorValidadePassword));
            informTokenAndNewPasswordViewHolder.errorEditTextPassword.setErrorVisibility(true);
        }
    }
}
