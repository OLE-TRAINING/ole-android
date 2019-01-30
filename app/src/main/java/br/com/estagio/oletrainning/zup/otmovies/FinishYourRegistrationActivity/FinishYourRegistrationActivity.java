package br.com.estagio.oletrainning.zup.otmovies.FinishYourRegistrationActivity;

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

import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.SyncProgressBar;

public class FinishYourRegistrationActivity extends AppCompatActivity {

    private FinishYourRegistrationViewHolder finishYourRegistrationViewHolder;
    private FinishYourRegistrationViewModel finishYourRegistrationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_finish_your_registration, null);
        this.finishYourRegistrationViewHolder = new FinishYourRegistrationViewHolder(view);
        setContentView(view);

        String emailAdd = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
        finishYourRegistrationViewHolder.textViewEmail.setText(emailAdd);

        finishYourRegistrationViewModel = ViewModelProviders.of(this).get(FinishYourRegistrationViewModel.class);

        setupObservers();
    }

    private void colorStatusBar() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getColor(R.color.colorBackground));
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setupListeners();
        colorStatusBar();
    }

    private void setupListeners() {
        finishYourRegistrationViewHolder.button.setOnClickListener(buttonOnClickListener);
        finishYourRegistrationViewHolder.imageView.setOnClickListener(backArrowOnClickListener);
        finishYourRegistrationViewHolder.errorEditText.getEditText().addTextChangedListener(errorEditTextTextWatcher);
        finishYourRegistrationViewHolder.textViewReSend.setOnClickListener(textViewOnClickListener);
    }

    private void setupObservers() {
        finishYourRegistrationViewModel.getTokenContainsErrorStatus().observe(this, tokenErrorStatusObserver);
        finishYourRegistrationViewModel.getIsLoading().observe(this, progressBarObserver);
    }

    private Observer<Boolean> tokenErrorStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                finishYourRegistrationViewHolder.errorEditText.setErrorVisibility(containsErrorStatus);
            }
        }

    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean isLoading) {
            if (isLoading != null) {
                if (isLoading) {
                    finishYourRegistrationViewHolder.progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new SyncProgressBar(FinishYourRegistrationActivity.this, finishYourRegistrationViewHolder.progressBar).execute();
                } else {
                    finishYourRegistrationViewHolder.progressBar.setProgress(100);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    finishYourRegistrationViewHolder.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    Observer<ResponseModel> serviceCallTokenValidation = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            finishYourRegistrationViewModel.serviceEnding();
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    Toast.makeText(FinishYourRegistrationActivity.this, getString(R.string.success_message_validate_token), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(FinishYourRegistrationActivity.this, LoginActivity.class);
                    String emailInput = finishYourRegistrationViewHolder.textViewEmail.getText().toString().trim();
                    intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                    startActivity(intent);
                } else {
                    ErrorMessage errorMessage = new ErrorMessage();
                    errorMessage.setKey(responseModel.getKey());
                    errorMessage.setMessage(responseModel.getMessage());
                    if (errorMessage.getKey().equals(getString(R.string.unauthorized_token_key))) {
                        finishYourRegistrationViewHolder.errorEditText.setMessageError(errorMessage.getMessage());
                        finishYourRegistrationViewHolder.errorEditText.setErrorVisibility(true);
                    } else if (errorMessage.getKey().equals(getString(R.string.invalid_token_key))) {
                        finishYourRegistrationViewHolder.errorEditText.setMessageError(errorMessage.getMessage());
                        finishYourRegistrationViewHolder.errorEditText.setErrorVisibility(true);
                    } else {
                        Toast.makeText(FinishYourRegistrationActivity.this, errorMessage.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(FinishYourRegistrationActivity.this, getString(R.string.service_or_connection_error_validate_token), Toast.LENGTH_LONG).show();
            }
        }
    };

    Observer<ResponseModel> serviceCallResendObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            finishYourRegistrationViewModel.serviceEnding();
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    Toast.makeText(FinishYourRegistrationActivity.this, getString(R.string.success_resend_token), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FinishYourRegistrationActivity.this, responseModel.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(FinishYourRegistrationActivity.this, getString(R.string.service_or_connection_error_resend_token), Toast.LENGTH_LONG).show();
            }
        }
    };


    View.OnClickListener backArrowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(FinishYourRegistrationActivity.this, PreLoginActivity.class);
                startActivity(intent);
            }

        }
    };

    View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callTokenResend();
        }
    };

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = finishYourRegistrationViewHolder.textViewEmail.getText().toString().trim();
            String code = finishYourRegistrationViewHolder.errorEditText.getEditText().getText().toString().trim();
            finishYourRegistrationViewModel.tokenEntered(code);
            if (finishYourRegistrationViewModel.isValidToken(code)) {
                finishYourRegistrationViewModel.serviceStarting();
                finishYourRegistrationViewModel.tokenValidation(email, code)
                        .observe(FinishYourRegistrationActivity.this, serviceCallTokenValidation);
            }
        }
    };

    TextWatcher errorEditTextTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            finishYourRegistrationViewModel.tokenTextChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void callTokenResend() {
        String email = finishYourRegistrationViewHolder.textViewEmail.getText().toString().trim();
        finishYourRegistrationViewModel.serviceStarting();
        finishYourRegistrationViewModel.resendToken(email)
                .observe(FinishYourRegistrationActivity.this, serviceCallResendObserver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FinishYourRegistrationActivity.this, PreLoginActivity.class);
        startActivity(intent);
    }
}